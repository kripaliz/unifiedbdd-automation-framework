package com.klk.testing.automation;

import org.junit.BeforeClass;
import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import io.github.bonigarcia.wdm.ChromeDriverManager;
import io.github.bonigarcia.wdm.FirefoxDriverManager;
import io.github.bonigarcia.wdm.InternetExplorerDriverManager;

@CucumberOptions(features = "classpath:features", tags = { "~@wip" })
@RunWith(Cucumber.class)
public abstract class AbstractAutomationTests {

	@BeforeClass
	public static void setupClass() {
		ChromeDriverManager.getInstance().setup();
		InternetExplorerDriverManager.getInstance().setup();
		FirefoxDriverManager.getInstance().setup();
	}
}
