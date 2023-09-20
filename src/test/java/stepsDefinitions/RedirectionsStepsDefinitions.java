package stepsDefinitions;

import base.BaseTest;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;

public class RedirectionsStepsDefinitions extends BaseTest {
    @Given("the app's movies screen is displayed correctly and movies are rendered")
    public void validateAppMoviesScreen() {
        androidHomeScreen.get().waitUntilMoviesListIsDisplayed();
        Assert.assertTrue(androidHomeScreen.get().isMoviesPostersDisplayed(),
                "Movies posters are not displayed correctly");
    }

    @When("I press on the first movie card from the movies list displayed")
    public void pressOnFirstMovieCard() {
        androidHomeScreen.get().pressMovieCard(1);
    }

    @Then("the Movie Details page is displayed")
    public void verifyMovieDetailsPage() {
        Assert.assertTrue(androidMovieDetailsScreen.get().isPageDisplayed(),
                "Movie Details page isn't displayed");
    }

    @Then("I press the back button in the movie details screen")
    public void pressBackButton() {
        androidMovieDetailsScreen.get().pressBackBtn();
    }

    @Then("the app should go back to the home screen with movies posters displayed correctly")
    public void verifyAppGoesBackToHomeScreen() {
        Assert.assertTrue(androidHomeScreen.get().isMoviesPostersDisplayed(),
                "App didn't go back to the home screen as movies posters are not displayed correctly");
    }
}
