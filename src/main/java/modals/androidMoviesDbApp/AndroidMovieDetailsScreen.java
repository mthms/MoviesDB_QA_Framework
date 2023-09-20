package modals.androidMoviesDbApp;

import io.appium.java_client.android.AndroidDriver;
import modals.BaseAndroidScreen;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class AndroidMovieDetailsScreen extends BaseAndroidScreen {
    public AndroidMovieDetailsScreen(AndroidDriver androidDriver) {
        super(androidDriver);
        PageFactory.initElements(androidDriver, this);
    }

    @FindBy(id = "com.skydoves.themovies:id/detail_header_title")
    WebElement movieTitle;

    @FindBy(id = "com.skydoves.themovies:id/detail_header_release")
    WebElement releaseDate;

    @FindBy(xpath = "//android.widget.ImageButton[@content-desc='Navigate up']")
    WebElement backBtn;

    public boolean isPageDisplayed(){
        return isElementDisplayed(movieTitle);
    }

    public String getMovieTitle(){
        return wait.until(ExpectedConditions.visibilityOf(movieTitle))
                .getText();
    }

    public String getReleaseDate(){
        return wait.until(ExpectedConditions.visibilityOf(releaseDate)).getText().split(": ")[1];
    }

    public void pressBackBtn(){
        wait.until(ExpectedConditions.visibilityOf(backBtn))
                .click();
    }
}
