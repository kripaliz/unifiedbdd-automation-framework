package com.github.kripaliz.automation;

import org.testng.annotations.DataProvider;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

/**
 * An abstraction for a TestNg runner that kicks off cucumber. <br>
 * <br>
 *
 * Extend this in your test suite and configure your glue package like:
 *
 * <pre>
 * <code>
&#64;CucumberOptions(glue = { "com.company.testing.step" })
public class AutomationTests extends AbstractAutomationTests {

}
 * </code>
 * </pre>
 *
 * @author kkurian
 *
 */
@CucumberOptions(features = "classpath:features", tags = { "not @wip" }, plugin = { "pretty",
		"com.github.kripaliz.automation.cucumber.plugin.TestReportListener",
		"io.qameta.allure.cucumber4jvm.AllureCucumber4Jvm",
		"rerun:target/rerun.txt" }, glue = { "com.github.kripaliz.automation.cucumber.glue" })
public abstract class AbstractAutomationTests extends AbstractTestNGCucumberTests {

	@Override
	@DataProvider(parallel = true)
	public Object[][] scenarios() {
		return super.scenarios();
	}

}
