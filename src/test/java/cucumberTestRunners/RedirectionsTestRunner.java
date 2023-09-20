package cucumberTestRunners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
        features = "src/test/java/cucumberFeatureFiles/RedirectionTests.feature",
        glue = {"base", "stepsDefinitions"}
)
public class RedirectionsTestRunner extends AbstractTestNGCucumberTests {
}
