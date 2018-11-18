package com.github.kripaliz.automation;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

/**
 * An abstraction for a JUnit test runner that kicks off cucumber. <br>
 * <br>
 * This abstraction and its subclasses are not used when running using the
 * cucumber-jvm-parallel-plugin.
 *
 * @author kkurian
 *
 */
@CucumberOptions(features = "classpath:features", tags = { "~@wip" })
@RunWith(Cucumber.class)
public abstract class AbstractAutomationTests {

}
