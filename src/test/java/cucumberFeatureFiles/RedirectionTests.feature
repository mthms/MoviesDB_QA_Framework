Feature: Test the redirections between homePage and movie details screen in the application
  Scenario: Verify app redirection to movie details and back to home screen
    Given the app's movies screen is displayed correctly and movies are rendered
    When I press on the first movie card from the movies list displayed
    Then the Movie Details page is displayed
    And I press the back button in the movie details screen
    Then the app should go back to the home screen with movies posters displayed correctly