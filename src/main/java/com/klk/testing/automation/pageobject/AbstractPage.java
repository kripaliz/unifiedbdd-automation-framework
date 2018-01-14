/**
 *
 */
package com.klk.testing.automation.pageobject;

import java.util.Set;

import javax.annotation.PostConstruct;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;

import com.klk.testing.automation.WebDriverConfig;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidKeyCode;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;

/**
 * The abstraction for a PageObject that can execute for browsers on desktops
 * and mobile devices as well as mobile apps.
 * 
 * @author kkurian
 *
 */
public abstract class AbstractPage {

	@Autowired
	protected WebDriverConfig webDriverConfig;

	@Autowired
	protected WebDriver webDriver;

	@PostConstruct
	public void init() {
		if (webDriver instanceof AppiumDriver) {
			PageFactory.initElements(new AppiumFieldDecorator(webDriver), this);
		} else {
			PageFactory.initElements(webDriver, this);
		}
	}

	/**
	 * Waits for given <code>webElement</code> upto <code>timeOut</code> seconds
	 * 
	 * @param webElement
	 * @param timeOut
	 */
	protected void waitForElement(WebElement webElement, long timeOut) {
		WebDriverWait wait = new WebDriverWait(webDriver, timeOut);
		wait.until(ExpectedConditions.elementToBeClickable(webElement));
	}

	/**
	 * Sends the return/enter key
	 * 
	 * @param webElement
	 */
	@SuppressWarnings("rawtypes")
	protected void sendEnterKey(WebElement webElement) {
		if (webDriver instanceof AndroidDriver) {
			((AndroidDriver) webDriver).pressKeyCode(AndroidKeyCode.ENTER);
		} else {
			webElement.sendKeys(Keys.RETURN);
		}
	}

	/**
	 * Check whether current execution is on a mobile device
	 * 
	 * @return
	 */
	protected boolean isDevice() {
		return webDriver instanceof AppiumDriver;
	}

	/**
	 * Check whether current execution is on a mobile app
	 * 
	 * @return
	 */
	protected boolean isApp() {
		return isDevice() && MapUtils.isNotEmpty(webDriverConfig.getDesiredCapabilities())
				&& (StringUtils.isNotBlank(webDriverConfig.getDesiredCapabilities().get("appPackage"))
						|| StringUtils.isNotBlank(webDriverConfig.getDesiredCapabilities().get("app")));
	}

	/**
	 * Switches current webDriver context to the webView
	 */
	protected void switchToWebView() {
		if (isApp()) {
			Set<String> contextHandles = getAppiumDriver().getContextHandles();
			getAppiumDriver().context(contextHandles.toArray(new String[] {})[1]);
		}
	}

	/**
	 * Switches current webDriver context to native app
	 */
	protected void switchToNativeView() {
		if (isApp()) {
			getAppiumDriver().context("NATIVE_APP");
		}
	}

	/**
	 * If current execution is using the AppiumDriver, it returns the AppiumDriver
	 * instance(type-cast)
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected AppiumDriver<WebElement> getAppiumDriver() {
		if (isDevice()) {
			return (AppiumDriver<WebElement>) webDriver;
		} else {
			throw new IllegalStateException(
					"webDriver is of type:" + webDriver.getClass().getName() + " expected AppiumDriver");
		}
	}
}
