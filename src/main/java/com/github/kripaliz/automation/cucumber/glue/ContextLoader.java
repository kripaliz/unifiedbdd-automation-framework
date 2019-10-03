package com.github.kripaliz.automation.cucumber.glue;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.github.kripaliz.automation.AutomationApplication;

import io.cucumber.java.Before;

/**
 * This class loads the spring context for cucumber execution. This package is
 * included as glue in cucumber options.
 *
 * @author kkurian
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AutomationApplication.class)
public class ContextLoader {

	@Before
	public void setup() {
	}
}
