package helpers;

import models.Configs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileReader;
import java.util.*;

public class DataHelper {
    private static final Logger logger = LoggerFactory.getLogger(DataHelper.class);

    //read configs
    public Configs readConfigs(Configs configs){
        File mainConfigs = new File("resources/environments/config.properties");
        File webConfigs = new File("resources/environments/webConfig.properties");

        // Read Main Configs
        try {
            FileReader fileReader = new FileReader(mainConfigs);
            Properties properties = new Properties();
            properties.load(fileReader);
            configs.setBaseURL(properties.getProperty("baseURL"));
            configs.setGridURL(properties.getProperty("gridURL"));
            configs.setTestEmail(properties.getProperty("testEmail"));
            configs.setTestPassword(properties.getProperty("testPassword"));
            configs.setTestMobileNumber(Integer.parseInt(properties.getProperty("testMobileNumber")));
            configs.setTestMobileCompany(properties.getProperty("testMobileCompany"));
            configs.setTestCreditCard(properties.getProperty("testCreditCard"));
            configs.setTestExpDate(properties.getProperty("testExpDate"));
            configs.setTestCVC(properties.getProperty("testCVC"));
            configs.setSecondaryTestCreditCard(properties.getProperty("secondaryTestCreditCard"));
            configs.setSecondaryTestExpDate(properties.getProperty("secondaryTestExpDate"));
            configs.setSecondaryTestCVC(properties.getProperty("secondaryTestCVC"));
            configs.setWebRunMode(properties.getProperty("webRunMode"));
            configs.setMobileRunMode(properties.getProperty("mobileRunMode"));
            configs.setWebBuildEnabled(Boolean.valueOf(properties.getProperty("webBuildEnabled")));
            configs.setMobileBuildEnabled(Boolean.valueOf(properties.getProperty("mobileBuildEnabled")));
            configs.setAndroidBuildEnabled(Boolean.valueOf(properties.getProperty("androidBuildEnabled")));
            configs.setiOSBuildEnabled(Boolean.valueOf(properties.getProperty("iosBuildEnabled")));
            configs.setAndroidDeviceName(properties.getProperty("androidDeviceName"));
            configs.setiOSDeviceName(properties.getProperty("iosDeviceName"));
            fileReader.close();

        } catch (Exception e){
            logger.error("Reading main config file failed. Killing the test...", e);
        }

        //Read Web Configs
        if (configs.getWebBuildEnabled()) {
            try {
                FileReader fileReader = new FileReader(webConfigs);
                Properties properties = new Properties();
                properties.load(fileReader);
                configs.setChromeDriverPath(properties.getProperty("chromedriver"));
                configs.setBrowser(properties.getProperty("browser"));

                fileReader.close();
            } catch (Exception e) {
                logger.error("Reading web config file failed. Killing the test...", e);
            }
        }

        //Read android Configs
        if (configs.getAndroidBuildEnabled()) {
            File androidDeviceConfigs = new File("resources/environments/"
                    + configs.getAndroidDeviceName() + ".properties");
            try {
                FileReader fileReader = new FileReader(androidDeviceConfigs);
                Properties properties = new Properties();
                properties.load(fileReader);
                configs.setAndroidPlatformName(properties.getProperty("platformName"));
                configs.setAndroidPlatformVersion(properties.getProperty("appium_platformVersion"));
                configs.setAndroidADBName(properties.getProperty("appium_deviceName"));
                configs.setAndroidAutomationName(properties.getProperty("appium_automationName"));

                fileReader.close();
            } catch (Exception e) {
                logger.error("Reading android device config file failed. Killing the test...", e);
            }
        }

        //Read iOS configs
        if (configs.getiOSBuildEnabled()) {
            File iosDeviceConfigs = new File("resources/environments/"
                    + configs.getiOSDeviceName().replace(" ", "_") + ".properties");
            try {
                FileReader fileReader = new FileReader(iosDeviceConfigs);
                Properties properties = new Properties();
                properties.load(fileReader);
                configs.setIosPlatformName(properties.getProperty("platformName"));
                configs.setIosPlatformVersion(properties.getProperty("appium_platformVersion"));
                configs.setIosUDID(properties.getProperty("appium_deviceUDID"));
                configs.setIosAutomationName(properties.getProperty("appium_automationName"));

                fileReader.close();
            } catch (Exception e) {
                logger.error("Reading iOS device config file failed. Killing the test...", e);
            }
        }

        return configs;
    }
}
