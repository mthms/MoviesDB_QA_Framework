package modals.androidMoviesDbApp;

import io.appium.java_client.android.AndroidDriver;
import modals.BaseAndroidScreen;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

public class AndroidHomeScreen extends BaseAndroidScreen {
    public AndroidHomeScreen(AndroidDriver androidDriver) {
        super(androidDriver);
        PageFactory.initElements(androidDriver, this);
    }

    @FindBy(id = "com.skydoves.themovies:id/item_poster_post")
    List<WebElement> movieCardPosters;

    @FindBy(id = "com.skydoves.themovies:id/item_poster_title")
    List<WebElement> movieCardNames;

    @FindBy(id = "com.skydoves.themovies:id/action_one")
    WebElement moviesTabBtn;

    @FindBy(id = "com.skydoves.themovies:id/action_two")
    WebElement tvTabBtn;

    @FindBy(id = "com.skydoves.themovies:id/action_three")
    WebElement starTabBtn;

    public void pressMovieCard(int targetMovieListNumber){
        if (isElementDisplayed(movieCardPosters.get(0)) && targetMovieListNumber <= movieCardPosters.size())
            wait.until(ExpectedConditions.visibilityOf(movieCardPosters.get(targetMovieListNumber-1))).click();
    }

    public void pressMovieName(int targetMovieListNumber){
        if (isElementDisplayed(movieCardNames.get(0)) && targetMovieListNumber <= movieCardNames.size())
            wait.until(ExpectedConditions.visibilityOf(movieCardNames.get(targetMovieListNumber-1))).click();
    }

    public void pressMoviesTabBtn(){
        wait.until(ExpectedConditions.visibilityOf(moviesTabBtn)).click();
    }

    public void pressTvTabBtn(){
        wait.until(ExpectedConditions.visibilityOf(tvTabBtn)).click();
    }

    public void pressStarTabBtn(){
        wait.until(ExpectedConditions.visibilityOf(starTabBtn)).click();
    }
}
