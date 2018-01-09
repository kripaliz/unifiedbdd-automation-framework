/**
 *
 */
package com.klk.testing.automation.pageobject;

import javax.annotation.PostConstruct;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author kkurian
 *
 */
public abstract class AbstractPage {

	@Autowired
	protected WebDriver webDriver;

	@PostConstruct
	public void init() {
		PageFactory.initElements(webDriver, this);
	}
}
