package helpers.factories;

import helpers.DataHelper;
import helpers.SetUpHelper;
import models.Configs;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class WebDriversFactory {

    private static final Logger logger = LoggerFactory.getLogger(WebDriversFactory.class);
    private WebDriver webDriver;
    private final Configs configs;
    private final String runMode;
    private final String browserType;
    private final String gridURL;

    public WebDriversFactory(Configs configs) {
        this.configs = configs;
        runMode = configs.getWebRunMode();
        browserType = configs.getBrowser();
        gridURL = configs.getGridURL();
    }

    public WebDriver build(){
        switch (runMode){
            case "local": case "localHeadless":
                if (browserType.equals("chrome"))
                    webDriver = buildChromeDriver();
                break;
            case "remote":
                webDriver = buildRemoteWebDriver();
                break;
        }
        return webDriver;
    }

    public WebDriver buildRemoteWebDriver(){
        logger.info("Building the remote WebDriver...");
        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
        desiredCapabilities.setPlatform(Platform.LINUX);

        switch (browserType) {
            case "chrome" -> {
                logger.info("Building remote chrome driver...");
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.merge(desiredCapabilities)
                        .addArguments("headless", "window-size=1920,1080", "--disable-gpu", "proxy=null",
                                "--remote-allow-origins=*");
                try {
                    webDriver = new RemoteWebDriver(new URI(gridURL).toURL(), chromeOptions);
                } catch (MalformedURLException | URISyntaxException e) {
                    logger.error("Grid URL is invalid", e);
                }
                logger.info("Completed building remote chrome driver.");
            }
            case "firefox" -> logger.info("Building remote firefox driver...");
            case "opera" -> logger.info("Building remote opera driver...");
        }
        logger.info("Completed building remote chrome driver...");
        return webDriver;

    }

    public WebDriver buildChromeDriver(){
        logger.info("Building local chrome driver...");
        System.setProperty("webdriver.chrome.driver", new SetUpHelper(webDriver, configs).getDriverPath());
        if (runMode.equals("localHeadless"))
            webDriver = new ChromeDriver(
                    new ChromeOptions().addArguments("headless", "window-size=1920,1080", "--disable-gpu", "proxy=null",
                            "--remote-allow-origins=*"));
        else
            webDriver = new ChromeDriver(new ChromeOptions().addArguments("--remote-allow-origins=*"));
        logger.info("Completed building remote chrome driver...");
        return webDriver;
    }

}
