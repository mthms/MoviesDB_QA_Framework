package modals;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;

import java.time.Duration;

public class BaseAndroidScreen {
    public AndroidDriver androidDriver;
    public Wait<AndroidDriver> wait;

    private final static int ANDROID_WAIT_DURATION = 30;

    public BaseAndroidScreen(AndroidDriver androidDriver) {
        this.androidDriver = androidDriver;
        this.wait = new FluentWait<>(androidDriver).withTimeout(Duration.ofSeconds(ANDROID_WAIT_DURATION))
                .pollingEvery(Duration.ofSeconds(1)).ignoring(NoSuchElementException.class);
    }

    public boolean isElementDisplayed(WebElement webElement){
        try {
            wait.until(ExpectedConditions.visibilityOf(webElement));
            return true;
        } catch (Exception e){
            return false;
        }
    }
}
