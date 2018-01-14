package com.klk.testing.automation;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

/**
 * An abstraction for a JUnit test runner that kicks off cucumber.
 * 
 * @author kkurian
 *
 */
@CucumberOptions(features = "classpath:features", tags = { "~@wip" })
@RunWith(Cucumber.class)
public abstract class AbstractAutomationTests {

}
