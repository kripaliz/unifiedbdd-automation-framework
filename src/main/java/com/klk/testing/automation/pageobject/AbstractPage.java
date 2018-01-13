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

	protected void waitForElement(WebElement webElement, long timeOut) {
		WebDriverWait wait = new WebDriverWait(webDriver, timeOut);
		wait.until(ExpectedConditions.elementToBeClickable(webElement));
	}

	@SuppressWarnings("rawtypes")
	protected void sendEnterKey(WebElement webElement) {
		if (webDriver instanceof AndroidDriver) {
			((AndroidDriver) webDriver).pressKeyCode(AndroidKeyCode.ENTER);
		} else {
			webElement.sendKeys(Keys.RETURN);
		}
	}

	protected boolean isDevice() {
		return webDriver instanceof AppiumDriver;
	}

	protected boolean isNativeApp() {
		return isDevice() && MapUtils.isNotEmpty(webDriverConfig.getDesiredCapabilities())
				&& (StringUtils.isNotBlank(webDriverConfig.getDesiredCapabilities().get("appPackage"))
						|| StringUtils.isNotBlank(webDriverConfig.getDesiredCapabilities().get("app")));
	}

	protected void switchToWebView() {
		if (isNativeApp()) {
			Set<String> contextHandles = getAppiumDriver().getContextHandles();
			getAppiumDriver().context(contextHandles.toArray(new String[] {})[1]);
		}
	}

	protected void switchToNativeView() {
		if (isNativeApp()) {
			getAppiumDriver().context("NATIVE_APP");
		}
	}

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
