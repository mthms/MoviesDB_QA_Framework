package helpers.factories;

import helpers.BaseHelper;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.IOSMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import models.*;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.time.Duration;
import java.util.Arrays;

public class MobileDriversFactory extends BaseHelper {
    public MobileDriversFactory(Configs configs) {
        this.configs = configs;
    }

    Logger logger = LoggerFactory.getLogger(MobileDriversFactory.class);
    private static final Duration maxTimeOut = Duration.ofSeconds(120);
    Configs configs;

    public AppiumServer buildLocalAppiumServer(AppiumServer appiumServer){
        logger.info("Building appium server...");
        String localAppiumJSPath = getLocalAppiumPath();
        String localNodeJSPath = getLocalNodeJSPath();

        AppiumServiceBuilder builder = new AppiumServiceBuilder();
        builder.usingPort(generateRandomPortNumber());

        if (localAppiumJSPath != null){
            builder.withAppiumJS(new File(localAppiumJSPath));
        } else {
            logger.warn("Fetching appiumJS path failed. Trying to create an appium service without it...");
        }

        if (localNodeJSPath != null){
            builder.usingDriverExecutable(new File(localNodeJSPath));
        } else {
            logger.warn("Fetching NodeJS path failed. Trying to create an appium service without it...");
        }
        builder.withTimeout(maxTimeOut);
        AppiumDriverLocalService localService = builder.build();

        logger.info("Appium local service built on URL: " + localService.getUrl());

        appiumServer.setAppiumLocalService(localService);

        return appiumServer;
    }

    public AppiumServer startAppiumServer(AppiumServer appiumServer){
        logger.info("Starting appium server...");
        try {
            appiumServer.getAppiumLocalService().start();
            logger.info("Appium server should be started now.");
        } catch (Exception e){
            logger.error("Starting appium server crashed with exception: ", e);
        }
        if (appiumServer.getAppiumLocalService().isRunning()) {
            logger.info("Appium Server started on URL: " + appiumServer.getAppiumLocalService().getUrl());
        } else {
            // Handle failure of appium server starting
            logger.error("Appium server couldn't be started");
        }
        return appiumServer;
    }

    public String getLocalNodeJSPath() {
        logger.info("Getting local Node.js path...");
        String nodeJSPath = null;
        String osName = System.getProperty("os.name").toLowerCase();
        logger.info("Current OS Value is: " + osName);
        String[] command = (osName.contains("win")) ?
                new String[]{"cmd", "/c", "where node"}
                : new String[]{"command", "-v", "node"};
        try {
            Process process = Runtime.getRuntime().exec(command);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                nodeJSPath = line.trim();
                break; // Only retrieve the first path if multiple paths are returned
            }

            if (nodeJSPath != null) {
                logger.info("Node.js path: " + nodeJSPath);
            } else {
                logger.error("Node.js is not installed on the machine.");
            }
        } catch (IOException e) {
            logger.error("Failed to get Node.js path.", e);
        }
        return nodeJSPath;
    }

    public String getLocalAppiumPath(){
        logger.info("Getting local appium path...");
        String appiumJSPath = null;
        String appiumPath = null;
        String osName = System.getProperty("os.name").toLowerCase();
        logger.info("Current OS Value is: " + osName);

        // Getting the appium path to find out if appium is installed or not
        String[] command = (osName.contains("win")) ?
                new String[]{"cmd", "/c", "where appium"}
                : new String[]{"command", "-v", "appium"};
        try {
            Process process = Runtime.getRuntime().exec(command);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;

            while ((line = reader.readLine()) != null) {
                appiumPath = line.trim();
                break; // Only retrieve the first path if multiple paths are returned
            }
        } catch (IOException e) {
            logger.error("Getting appium path failed with Exception.", e);
        }

        // If appium path found, that means that appium is installed correctly on machine
        if (appiumPath != null && appiumPath.contains(File.separator + "appium")){
            String[] npmRootCommand = (osName.contains("win")) ?
                    new String[]{"cmd", "/c", "npm", "root", "-g"} :
                    new String[]{"npm", "root", "-g"};

            try {
                Process process = Runtime.getRuntime().exec(npmRootCommand);
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String npmRootPath = reader.readLine();

                logger.info("Result of the command \"" + Arrays.toString(npmRootCommand) +"\" is: " + npmRootPath);

                if (npmRootPath != null && npmRootPath.contains(File.separator + "node_modules")) {
                    appiumJSPath = npmRootPath.trim() + File.separator
                            + "appium" + File.separator
                            + "build" + File.separator
                            + "lib" + File.separator
                            + "main.js";
                } else {
                    logger.info("Value of the command: \"" + Arrays.toString(npmRootCommand) + "\" is empty or not a valid path");
                }
            } catch (IOException e) {
                logger.error("Getting the appium JS path failed with exception: ", e);
            }
        } else {
            logger.error("Appium is not installed correctly on machine. Try running \"where appium\" command");
        }
        logger.info("Final Appium JS path is: " + appiumJSPath);
        return appiumJSPath;
    }

    public AndroidDriver buildAndroidDriver(AppiumServer appiumServer, AndroidDevice androidDevice){
        logger.info("Building android driver...");
        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();

        desiredCapabilities.setCapability(CapabilityType.PLATFORM_NAME, androidDevice.getPlatformName());
        desiredCapabilities.setCapability("appium:platformVersion", androidDevice.getOsVersion());
        desiredCapabilities.setCapability("appium:deviceName", androidDevice.getAdbName());
        desiredCapabilities.setCapability("appium:automationName", androidDevice.getAutomationName());
        desiredCapabilities.setCapability("appium:fullReset", true);
        desiredCapabilities.setCapability("appium:appWaitActivity",
                "com.skydoves.themovies.view.ui.main.MainActivity");
        desiredCapabilities.setCapability("appium:systemPort", appiumServer.getListenerPort());

        //Increase setup timeouts to reduce flakiness of tests
        desiredCapabilities.setCapability("appium:uiautomator2ServerLaunchTimeout", maxTimeOut.toMillis());
        desiredCapabilities.setCapability("appium:avdLaunchTimeout", maxTimeOut.toMillis());
        desiredCapabilities.setCapability("appium:avdReadyTimeout", maxTimeOut.toMillis());
        desiredCapabilities.setCapability("appium:adbExecTimeout", maxTimeOut.toMillis());
        desiredCapabilities.setCapability("appium:ignoreHiddenApiPolicyError", true);

        desiredCapabilities.setCapability("appium:avd",
                androidDevice.getName().replace(" ", "_"));
        desiredCapabilities.setCapability("appium:avdArgs",
                "-port " + androidDevice.getPortNumber()
                        + " -read-only"
                        + " -no-boot-anim"
                        + " -no-snapshot-save");
        desiredCapabilities.setCapability("appium:app", "resources/builds/movies.apk");
        if (configs.getMobileRunMode().equals("localHeadless"))
            desiredCapabilities.setCapability("appium:isHeadless", true);
        logger.info("Desired capabilities are set successfully.");

        logger.info("Initiating the android driver on the below info\n");
        logger.info("SystemPort: " + desiredCapabilities.getCapability("appium:systemPort"));
        logger.info("avdArgs: " + desiredCapabilities.getCapability("appium:avdArgs"));
        return new AndroidDriver(appiumServer.getAppiumLocalService(), desiredCapabilities);
    }

    public IOSDriver buildIosDriver(AppiumServer appiumServer, IosDevice iosDevice){
        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();

        desiredCapabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, iosDevice.getPlatformName());
        desiredCapabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, iosDevice.getOsVersion());
        desiredCapabilities.setCapability(MobileCapabilityType.DEVICE_NAME, iosDevice.getDeviceName());
        desiredCapabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, iosDevice.getAutomationName());
        desiredCapabilities.setCapability(IOSMobileCapabilityType.WDA_LOCAL_PORT, appiumServer.getListenerPort());
        desiredCapabilities.setCapability("appium:app", "resources/builds/movies.zip");
        desiredCapabilities.setCapability(MobileCapabilityType.UDID, iosDevice.getDeviceUdid());
        desiredCapabilities.setCapability("appium:fullReset", true);
        if (configs.getMobileRunMode().equals("localHeadless"))
            desiredCapabilities.setCapability("appium:isHeadless", true);


        return new IOSDriver(appiumServer.getAppiumLocalService(), desiredCapabilities);
    }

    public AppiumServer generateRandomDriverPortNumber(AppiumServer appiumServer){
        appiumServer.setDriverPort(generateRandomPortNumber());
        return appiumServer;
    }

    public AppiumServer generateListenerPortNumber(AppiumServer appiumServer){
        appiumServer.setListenerPort(generateRandomPortNumber());
        return appiumServer;
    }

    public AndroidDevice runAndroidEmulator(AndroidDevice androidDevice){
        System.setProperty("user.dir", System.getProperty("user.home"));

        int emulatorPortNumber = generateRandomPortNumber();
        androidDevice.setName(configs.getAndroidDeviceName());
        androidDevice.setAdbName("emulator-"+emulatorPortNumber);
        androidDevice.setPortNumber(String.valueOf(emulatorPortNumber));
        androidDevice.setOsVersion(configs.getAndroidPlatformVersion());


        try {
            Runtime.getRuntime().exec(new String[]{"emulator", "@" + configs.getAndroidDeviceName()
                    , "-port", androidDevice.getPortNumber(), "-read-only"});
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        waitUntilDeviceIsEnabled("Android", androidDevice.getAdbName());

        return androidDevice;
    }

    public void runIosSimulator(){
        System.setProperty("user.dir", System.getProperty("user.home"));

        try {
            String deviceName = "\"" + configs.getiOSDeviceName() + "\"";
            String[] command = {"xcrun", "simctl", "boot", deviceName};
            Runtime.getRuntime().exec(command);
        } catch (IOException e){
            throw new RuntimeException(e);
        }
        waitUntilDeviceIsEnabled("iOS", configs.getiOSDeviceName());

        if (configs.getMobileRunMode().equals("local"))
            try{
                Runtime.getRuntime().exec(new String[]{"open", "-a", "Simulator"});
            } catch (Exception e){
                logger.error("Couldn't start simulator app..,");
            }
    }

    public void killIosSimulator(IosDevice iosDevice){
        ProcessBuilder processBuilder = new ProcessBuilder("xcrun", "simctl", "shutdown"
                , iosDevice.getDeviceUdid());
        try {
            Process process = processBuilder.start();
            process.waitFor();

            // Check the exit value of the process
            int exitCode = process.exitValue();
            if (exitCode == 0) {
                logger.info("Simulator with Name: " + iosDevice.getDeviceName()
                        + " and UDID " + iosDevice.getDeviceUdid() + " is terminated.");
            } else {
                logger.info("Simulator failed to exit.\nName:" + iosDevice.getDeviceName()
                        + "\nUDID: " + iosDevice.getDeviceUdid());
            }
        } catch (IOException | InterruptedException e){
            logger.error("Killing iOS simulator failed with an exception.");
        }
    }

    public void killAndroidEmulator(AndroidDevice androidDevice){
        ProcessBuilder processBuilder = new ProcessBuilder("adb", "-s", androidDevice.getAdbName()
                , "emu", "kill");
        try {
            Process process = processBuilder.start();
            process.waitFor();

            // Check the exit value of the process
            int exitCode = process.exitValue();
            if (exitCode == 0) {
                logger.info("Emulator with Name: " + androidDevice.getName()
                        + " and ADBName " + androidDevice.getAdbName() + " is terminated.");
            } else {
                logger.info("Emulator failed to exit.\nName:" + androidDevice.getName()
                        + "\nADBName: " + androidDevice.getAdbName());
            }
        } catch (IOException | InterruptedException e){
            logger.error("Killing android emulator failed with an exception.");
        }
    }

    //deviceName should be the ADBName in case of Android
    public void waitUntilDeviceIsEnabled(String deviceType, String deviceName){
        boolean emulatorStarted = false;
        boolean simulatorStarted = false;
        if (configs.getMobileRunMode().equals("local") || configs.getMobileRunMode().equals("localHeadless")) {
            long startTime;
            System.setProperty("user.dir", System.getProperty("user.home"));
            switch (deviceType) {
                case "Android" -> {
                    startTime = System.currentTimeMillis();
                    while (!emulatorStarted) {
                        try {
                            Process process = Runtime.getRuntime().
                                    exec(new String[]{"adb", "-s", deviceName, "shell", "getprop"
                                            , "sys.boot_completed"});
                            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                            String output = reader.readLine();
                            if (output != null) {
                                if (output.equals("1"))
                                    emulatorStarted = true;
                            }
                            reader.close();
                        } catch (IOException e) {
                            logger.error("Not able to check the android emulator status.", e);
                        }

                        // Sleep for 1 second to reduce resources usage in pooling
                        try {
                            Thread.sleep(Duration.ofSeconds(1).toMillis());
                        } catch (Exception e) {
                            logger.error("Thread sleep interrupted", e);
                        }

                        // Check if the waiting time exceeds the maximum wait time
                        if (System.currentTimeMillis() - startTime > maxTimeOut.toMillis()) {
                            logger.error("Timeout: Android emulator did not start within the specified time.");
                            break;
                        }
                    }
                }
                case "iOS" -> {
                    startTime = System.currentTimeMillis();
                    while (!simulatorStarted) {
                        try {
                            Process process = Runtime.getRuntime()
                                    .exec(new String[]{"xcrun simctl list devices booted | grep \""
                                            + deviceName + "\""});
                            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                            if (reader.readLine().contains(configs.getiOSDeviceName()
                                    + " (" + configs.getIosUDID() + ") (Booted)"))
                                simulatorStarted = true;
                            reader.close();
                        } catch (Exception e) {
                            logger.error("Not able to check the iOS simulator status.", e);
                        }

                        // Sleep for 1 second to reduce resources usage in pooling
                        try {
                            Thread.sleep(Duration.ofSeconds(1).toMillis());
                        } catch (Exception e) {
                            logger.error("Thread sleep interrupted", e);
                        }

                        // Check if the waiting time exceeds the maximum wait time
                        if (System.currentTimeMillis() - startTime > maxTimeOut.toMillis()) {
                            logger.error("Timeout: iOS simulator did not start within the specified time.");
                            break;
                        }
                    }
                }
            }
        } else {
            logger.warn("Remote Mobile drivers are not supported yet.");
        }
    }

    public IosDevice createStandAloneSimulatorImage(IosDevice iosDevice, TestData testData){
        String simulatorNameIdentifier = testData.getTestEmail().replaceAll("\\D", "");
        ProcessBuilder processBuilder = new ProcessBuilder("xcrun", "simctl", "clone", configs.getIosUDID()
                , simulatorNameIdentifier);

        try {
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();
            InputStream inputStream = process.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String udid = null;
            // Read the command output
            String line;
            while ((line = reader.readLine()) != null) {
                // Assuming the UDID is the last line of the output
                udid = line.trim();
                logger.info("Cloned device from UDID: " + configs.getIosUDID() + "And Created a new simulator." +
                        "\nName: " + simulatorNameIdentifier + "\nUDID: " + udid);
            }
            if (udid != null){
                iosDevice.setDeviceUdid(udid);
                iosDevice.setDeviceName(simulatorNameIdentifier);
            }
            int exitCode = process.waitFor();
            logger.info("Cloning process completed with exitCode: " + exitCode);
        } catch (IOException | InterruptedException e){
            logger.error("Creating an iOS Simulator failed with an exception: ", e);
        }

        return iosDevice;
    }

    public void deleteIosSimulator(IosDevice iosDevice){
        if (iosDevice.getDeviceUdid().equals(configs.getIosUDID())){
            logger.warn("Not Deleting simulator as creating a clone of the simulator failed." +
                    "\nNot Deleting master image with UDID: " + iosDevice.getDeviceUdid());
        }
        else {
            ProcessBuilder processBuilder = new ProcessBuilder("xcrun", "simctl", "delete"
                    , iosDevice.getDeviceUdid());
            try {
                Process process = processBuilder.start();
                process.waitFor();

                int exitCode = process.exitValue();
                if (exitCode == 0){
                    logger.info("Created simulator deleted successfully." +
                            "\nName: " + iosDevice.getDeviceName() + "\nUDID: " + iosDevice.getDeviceUdid());
                } else {
                    logger.error("Deleting simulator failed. MANUAL ACTION REQUIRED TO RUN THIS COMMAND:" +
                            "\nxcrun simctl delete " + iosDevice.getDeviceUdid());
                }
            } catch (IOException | InterruptedException e){
                logger.error("Deleting simulator failed with an exception. MANUAL ACTION REQUIRED TO RUN THIS COMMAND:" +
                        "\nxcrun simctl delete " + iosDevice.getDeviceUdid(), e);
            }
        }
    }
}
