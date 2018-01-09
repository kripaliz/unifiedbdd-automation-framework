/**
 *
 */
package com.klk.testing.automation.pageobject;

import javax.annotation.PostConstruct;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.springframework.beans.factory.annotation.Autowired;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;

/**
 * @author kkurian
 *
 */
public abstract class AbstractAndroidScreen {

	@Autowired
	public AppiumDriver<WebElement> driver;

	@PostConstruct
	public void init() {
		PageFactory.initElements(new AppiumFieldDecorator(driver), this);
	}

}
