/**
 *
 */
package com.klk.testing.automation.pageobject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.Point;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
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
	protected void waitForElement(final WebElement webElement, final long timeOut) {
		final WebDriverWait wait = new WebDriverWait(webDriver, timeOut);
		wait.until(ExpectedConditions.elementToBeClickable(webElement));
	}

	/**
	 * Waits for given <code>webElement</code> to have an <code>attribute</code> of
	 * <code>value</code> upto <code>timeOut</code> seconds
	 *
	 * @param webElement
	 * @param attribute
	 * @param value
	 * @param timeOut
	 */
	protected void waitForElementAttribute(final WebElement webElement, final String attribute, final String value,
			final long timeOut) {
		final WebDriverWait wait = new WebDriverWait(webDriver, timeOut);
		wait.until(ExpectedConditions.attributeContains(webElement, attribute, value));
	}

	/**
	 * Sends the return/enter key
	 *
	 * @param webElement
	 */
	@SuppressWarnings("rawtypes")
	protected void sendEnterKey(final WebElement webElement) {
		if (webDriver instanceof AndroidDriver) {
			((AndroidDriver) webDriver).pressKeyCode(AndroidKeyCode.ENTER);
		} else {
			webElement.sendKeys(Keys.RETURN);
		}
	}

	/**
	 * Sends the search key on Android
	 *
	 * @param webElement
	 * @throws IOException
	 */
	@SuppressWarnings("rawtypes")
	protected void sendSearchKey(final WebElement webElement) throws IOException {
		if (webDriver instanceof AndroidDriver) {
			((AndroidDriver) webDriver).pressKeyCode(AndroidKeyCode.KEYCODE_SEARCH);
			final String command = "adb shell input keyevent KEYCODE_ENTER";
			Runtime.getRuntime().exec(command);
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
	 * Check whether current execution is on Firefox
	 *
	 * @return
	 */
	protected boolean isFirefox() {
		return webDriver instanceof FirefoxDriver || MapUtils.isNotEmpty(webDriverConfig.getDesiredCapabilities())
				&& "Firefox".equalsIgnoreCase(webDriverConfig.getDesiredCapabilities().get("browserName"));
	}

	/**
	 * Check whether current execution is on InternetExplorer
	 *
	 * @return
	 */
	protected boolean isInternetExplorer() {
		return webDriver instanceof InternetExplorerDriver
				|| MapUtils.isNotEmpty(webDriverConfig.getDesiredCapabilities()) && "Internet Explorer"
						.equalsIgnoreCase(webDriverConfig.getDesiredCapabilities().get("browserName"));
	}

	/**
	 * Check whether current execution is on Safari
	 *
	 * @return
	 */
	protected boolean isSafari() {
		return webDriver instanceof SafariDriver || MapUtils.isNotEmpty(webDriverConfig.getDesiredCapabilities())
				&& "Safari".equalsIgnoreCase(webDriverConfig.getDesiredCapabilities().get("browserName"));
	}

	/**
	 * Check whether current execution is on a Safari browser on mobile device
	 *
	 * @return
	 */
	protected boolean isDeviceSafari() {
		return webDriver instanceof AppiumDriver && MapUtils.isNotEmpty(webDriverConfig.getDesiredCapabilities())
				&& "Safari".equalsIgnoreCase(webDriverConfig.getDesiredCapabilities().get("browserName"));
	}

	/**
	 * Switches current webDriver context to the webView
	 *
	 * @param requiredPageTitle
	 */
	protected void switchToWebView(final String requiredPageTitle) {
		if (isApp()) {
			String webViewContext = "";
			// Switch to first web view context
			for (final String contextHandles : getAppiumDriver().getContextHandles()) {
				if (contextHandles.contains("WEBVIEW")) {
					webViewContext = contextHandles;
					break;
				}
			}
			getAppiumDriver().context(webViewContext);
			// Switch to window handle of the required page
			String pageTitle = "";
			for (final String windowHandle : getAppiumDriver().getWindowHandles()) {
				webViewContext = windowHandle;
				getAppiumDriver().switchTo().window(webViewContext);
				pageTitle = webDriver.getTitle();
				if (pageTitle.contains(requiredPageTitle)) {
					break;
				}
			}
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

	/**
	 * Select a webElement based on content text from a list
	 *
	 * @param webElementList
	 * @param requiredText
	 * @return
	 */
	protected WebElement selectWebElementWithTextFromList(final List<WebElement> webElementList,
			final String requiredText) {
		for (final WebElement webElement : webElementList) {
			if (webElement.getText().toLowerCase().replaceAll("\\s+", " ")
					.contains(requiredText.toLowerCase().replaceAll("\\s+", " "))) {
				return webElement;
			}
		}
		return null;
	}

	/**
	 * Select an option from a dropDown element
	 *
	 * @param dropdownWebElement
	 * @param requiredText
	 */
	protected void selectValueFromDropdown(final WebElement dropdownWebElement, final String requiredText) {
		if (isSafari()) {
			final JavascriptExecutor jse = (JavascriptExecutor) webDriver;
			jse.executeScript(
					"var select = arguments[0]; for(var i = 0; i < select.options.length; i++){ if(select.options[i].text == arguments[1]){ select.options[i].selected = true; } }",
					dropdownWebElement, requiredText);
		} else {
			final Select dropdown = new Select(dropdownWebElement);
			dropdown.selectByVisibleText(requiredText);
		}
	}

	/**
	 * get the selected option from a dropDown element
	 *
	 * @param dropdownWebElement
	 */
	protected String getSelectedOptionFromDropdown(final WebElement dropdownWebElement) {
		final Select dropdown = new Select(dropdownWebElement);
		final WebElement option = dropdown.getFirstSelectedOption();
		final String selectedOption = option.getText();
		return selectedOption;
	}

	/**
	 * Scroll to an element on the page
	 *
	 * @param webElement
	 */
	protected void scrollIntoView(final WebElement webElement) {
		final JavascriptExecutor jse = (JavascriptExecutor) webDriver;
		jse.executeScript("arguments[0].scrollIntoView();", webElement);
	}

	/**
	 * Force click on an element on the page
	 *
	 * @param webElement
	 */
	protected void forceClick(final WebElement webElement) {
		final JavascriptExecutor jse = (JavascriptExecutor) webDriver;
		jse.executeScript("arguments[0].click();", webElement);
	}

	/**
	 * Disable an element on the page
	 *
	 * @param webElement
	 */
	protected void disable(final WebElement webElement) {
		final JavascriptExecutor jse = (JavascriptExecutor) webDriver;
		jse.executeScript("arguments[0].setAttribute('disabled', '');", webElement);
	}

	/**
	 * Hide an element on the page
	 *
	 * @param webElement
	 */
	protected void hide(final WebElement webElement) {
		final JavascriptExecutor jse = (JavascriptExecutor) webDriver;
		jse.executeScript("arguments[0].setAttribute('style', 'display: none');", webElement);
	}

	/**
	 * Check whether element is present
	 *
	 * @param webElement
	 */
	protected boolean isElementPresent(final WebElement webElement) {
		boolean present = false;
		try {
			if (webElement.isDisplayed() && webElement.isEnabled()) {
				final Point point = webElement.getLocation();
				if (point.x > 0 && point.y > 0) {
					present = true;
				}
			}
		} catch (final NoSuchElementException | StaleElementReferenceException | ElementNotVisibleException e) {
			present = false;
		}
		return present;
	}

	/**
	 * Get textList from all webElementList
	 *
	 * @param webElementList
	 * @return
	 */
	public List<String> getTextListFromWebElementList(final List<WebElement> webElementList) {
		final List<String> textList = new ArrayList<>();
		if (webElementList != null && !webElementList.isEmpty()) {
			for (final WebElement webElement : webElementList) {
				textList.add(webElement.getText());
			}
		}
		return textList;
	}

}
