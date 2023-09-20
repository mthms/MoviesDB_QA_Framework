package base;

import io.cucumber.java.After;
import io.cucumber.java.Before;

public class CucumberHooks extends BaseTest{
    @Before
    public void beforeScenario() {
        setup();
    }

    @After
    public void afterScenario() {
        tearDown();
    }
}
