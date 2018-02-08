/**
 * 
 */
package com.klk.testing.automation.allure.listener;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import io.qameta.allure.Allure;
import io.qameta.allure.listener.TestLifecycleListener;
import io.qameta.allure.model.Status;
import io.qameta.allure.model.TestResult;

/**
 * @author kkurian
 *
 */
public class ScreenshotListener implements TestLifecycleListener {

	public static ThreadLocal<String> TEST_NAME = new ThreadLocal<>();

	public static ThreadLocal<WebDriver> WEB_DRIVER = new ThreadLocal<>();

	@Override
	public void beforeTestSchedule(TestResult result) {
		TEST_NAME.set(result.getName());
	}

	@Override
	public void afterTestUpdate(TestResult result) {
		WebDriver webDriver = WEB_DRIVER.get();
		if (webDriver != null && Status.FAILED.equals(result.getStatus())) {
			Allure.addByteAttachmentAsync("Screenshot", "image/png", ".png",
					() -> ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.BYTES));
		}
	}

}
