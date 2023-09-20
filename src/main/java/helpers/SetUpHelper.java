package helpers;

import models.Configs;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.ServerSocket;

public class SetUpHelper {
    public SetUpHelper (WebDriver webDriver, Configs configs){
        this.webDriver = webDriver;
        this.configs = configs;
    }
    private final WebDriver webDriver;
    private static final Logger logger = LoggerFactory.getLogger(SetUpHelper.class);
    private final Configs configs;

    //Return the local driver path based on the OS
    public String getDriverPath(){
        logger.info("Getting Driver Path");
        String driverPath;
        String osName = System.getProperty("os.name");
        if (osName.toLowerCase().contains("mac") || osName.toLowerCase().contains("linux")){
            driverPath = configs.getChromeDriverPath();
        } else {
            driverPath = System.getProperty("user.dir") + "/" + configs.getChromeDriverPath() + ".exe";
        }
        logger.info("DriverPath is: " + driverPath);
        return driverPath;
    }

    public void openBaseURL(){
        webDriver.get(configs.getBaseURL());
    }
}
