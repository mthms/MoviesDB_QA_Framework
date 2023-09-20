package redirections;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test
public class RedirectionTests extends BaseTest {
    @Test(alwaysRun = true)
    public void validateAppRedirection(){
        //Validate that app's movies screen is displayed correctly
        androidHomeScreen.get().waitUntilMoviesListIsDisplayed();
        Assert.assertTrue(androidHomeScreen.get().isMoviesPostersDisplayed(),
                "Movies posters are not displayed correctly");

        //Press on the first element
        androidHomeScreen.get().pressMovieCard(1);

        Assert.assertTrue(androidMovieDetailsScreen.get().isPageDisplayed(),
                "Movie Details page isn't displayed");

        androidMovieDetailsScreen.get().pressBackBtn();

        Assert.assertTrue(androidHomeScreen.get().isMoviesPostersDisplayed(),
                "App didn't go back to home screen as movies posters are not displayed correctly");
    }
}
