package com.github.kripaliz.automation.cucumber.glue;

import org.springframework.boot.test.context.SpringBootTest;

import com.github.kripaliz.automation.AutomationApplication;

import io.cucumber.java.Before;
import io.cucumber.spring.CucumberContextConfiguration;

/**
 * This class loads the spring context for cucumber execution. This package is
 * included as glue in cucumber options.
 *
 * @author kkurian
 *
 */
@CucumberContextConfiguration
@SpringBootTest(classes = AutomationApplication.class)
public class ContextLoader {

	@Before
	public void setup() {
	}
}
