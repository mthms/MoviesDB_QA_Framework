package base;

import helpers.*;
import helpers.factories.MobileDriversFactory;
import helpers.factories.WebDriversFactory;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import modals.androidMoviesDbApp.AndroidHomeScreen;
import modals.androidMoviesDbApp.AndroidMovieDetailsScreen;
import models.*;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public class BaseTest {
    private static final ThreadLocal<Logger> logger = new ThreadLocal<>();
    protected static ThreadLocal<DataHelper> dataHelper = new ThreadLocal<>();
    protected static ThreadLocal<Configs> configs = new ThreadLocal<>();
    protected static ThreadLocal<MobileDriversFactory> mobileDriversFactory = new ThreadLocal<>();
    protected static ThreadLocal<AppiumServer> androidAppiumServerController = new ThreadLocal<>();
    protected static ThreadLocal<AppiumServer> iosAppiumServerController = new ThreadLocal<>();
    protected static ThreadLocal<SetUpHelper> setUpHelper = new ThreadLocal<>();
    protected static ThreadLocal<WebDriver> webDriver = new ThreadLocal<>();
    protected static ThreadLocal<AndroidDriver> androidDriver = new ThreadLocal<>();
    protected static ThreadLocal<AndroidDevice> androidDevice = new ThreadLocal<>();
    protected static ThreadLocal<IOSDriver> iosDriver = new ThreadLocal<>();
    protected static ThreadLocal<IosDevice> iosDevice = new ThreadLocal<>();
    protected static ThreadLocal<TestExecutionHelper> testExecutionHelper = new ThreadLocal<>();
    protected static ThreadLocal<TestData> defaultTestData = new ThreadLocal<>();
    protected static ThreadLocal<WebDriversFactory> webDriversFactory = new ThreadLocal<>();
    protected static ThreadLocal<AndroidHomeScreen> androidHomeScreen = new ThreadLocal<>();
    protected static ThreadLocal<AndroidMovieDetailsScreen> androidMovieDetailsScreen = new ThreadLocal<>();

    @BeforeMethod
    public void setup(){
        logger.set(LoggerFactory.getLogger("BaseTest.class"));

        //Reading configs
        dataHelper.set(new DataHelper());
        configs.set(new Configs());
        configs.set(dataHelper.get().readConfigs(configs.get()));

        testExecutionHelper.set(new TestExecutionHelper(configs.get()));

        logger.get().info("Creating test data...");
        defaultTestData.set(new TestData());
        defaultTestData.set(testExecutionHelper.get().buildTestData(defaultTestData.get()));

        logger.get().info("Initiating test setup...");
        //Starting web driver
        if (configs.get().getWebBuildEnabled()){
            logger.get().info("Initiating Web driver setup...");
            webDriversFactory.set(new WebDriversFactory(configs.get()));
            webDriver.set(webDriversFactory.get().build());
            setUpHelper.set(new SetUpHelper(webDriver.get(), configs.get()));
            setUpHelper.get().openBaseURL();
        }

        //Appium Server Setup
        if (configs.get().getMobileBuildEnabled()) {
            mobileDriversFactory.set(new MobileDriversFactory(configs.get()));
            androidAppiumServerController.set(new AppiumServer());
            iosAppiumServerController.set(new AppiumServer());

            // Build Appium Server reserved for android device/emulator
            androidAppiumServerController.set(mobileDriversFactory
                    .get().buildLocalAppiumServer(androidAppiumServerController.get()));
            // Start Appium Server
            androidAppiumServerController.set(mobileDriversFactory
                    .get().startAppiumServer(androidAppiumServerController.get()));

            // Build Appium Server reserved for iOS device/simulator
            iosAppiumServerController.set(mobileDriversFactory
                    .get().buildLocalAppiumServer(iosAppiumServerController.get()));
            //Start Appium Server
            iosAppiumServerController.set(mobileDriversFactory
                    .get().startAppiumServer(iosAppiumServerController.get()));
        }

        //Android Driver Setup
        if (configs.get().getAndroidBuildEnabled()) {
            androidDevice.set(new AndroidDevice());
            // Generate a random port number for the automation driver to rely on
            androidAppiumServerController.set(mobileDriversFactory.get()
                    .generateRandomDriverPortNumber(androidAppiumServerController.get()));
            androidDevice.set(testExecutionHelper.get().buildAndroidDeviceData(androidDevice.get()));
            androidDriver.set(mobileDriversFactory.get().buildAndroidDriver(androidAppiumServerController.get()
                    , androidDevice.get()));

            //Initiating UI POMs
            androidHomeScreen.set(new AndroidHomeScreen(androidDriver.get()));
            androidMovieDetailsScreen.set(new AndroidMovieDetailsScreen(androidDriver.get()));
        }

        //iOS Driver Setup
        if (configs.get().getiOSBuildEnabled()) {
            iosDevice.set(new IosDevice());
            iosDevice.set(testExecutionHelper.get().buildIosDeviceData(iosDevice.get()));
            //Create an iOS simulator and assign for the test session using the generated email identifier
            iosDevice.set(mobileDriversFactory.get()
                    .createStandAloneSimulatorImage(iosDevice.get(), defaultTestData.get()));
            //Generate a random port for the automation driver to rely on
            iosAppiumServerController.set(mobileDriversFactory.get()
                    .generateRandomDriverPortNumber(iosAppiumServerController.get()));
            iosDriver.set(mobileDriversFactory.get().buildIosDriver(iosAppiumServerController.get(), iosDevice.get()));
        }
    }

    @AfterMethod
    public void tearDown(){
        logger.get().info("Initiating TearDown process");
        try {
            //Close the browser
            if (configs.get().getWebBuildEnabled()) {
                webDriver.get().quit();
            }

            //Terminate the android emulator
            if (configs.get().getAndroidBuildEnabled()){
                androidAppiumServerController.get().getAppiumLocalService().stop();
                if (!androidAppiumServerController.get().getAppiumLocalService().isRunning()) {
                    logger.get().info("Appium server on URL has been stopped: \n"
                            + androidAppiumServerController.get().getAppiumLocalService().getUrl());
                }
                mobileDriversFactory.get().killAndroidEmulator(androidDevice.get());
            }

            //Terminate iOS simulator
            if (configs.get().getiOSBuildEnabled()){
                iosAppiumServerController.get().getAppiumLocalService().stop();
                if (!iosAppiumServerController.get().getAppiumLocalService().isRunning()){
                    logger.get().info("Appium server on the below URL has been stopped: \n"
                            + iosAppiumServerController.get().getAppiumLocalService().getUrl());
                }
                mobileDriversFactory.get().killIosSimulator(iosDevice.get());

                //Clean up the created iOS simulator if any was created for the test
                mobileDriversFactory.get().deleteIosSimulator(iosDevice.get());
            }

        } catch (Exception e){
            logger.get().warn("Tear down operation failed.", e);
        }
    }
}
