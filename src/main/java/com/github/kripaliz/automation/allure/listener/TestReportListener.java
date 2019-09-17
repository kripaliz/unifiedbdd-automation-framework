/**
 *
 */
package com.github.kripaliz.automation.allure.listener;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import io.qameta.allure.Allure;
import io.qameta.allure.listener.StepLifecycleListener;
import io.qameta.allure.listener.TestLifecycleListener;
import io.qameta.allure.model.Status;
import io.qameta.allure.model.StepResult;
import io.qameta.allure.model.TestResult;

/**
 * An allure lifecycle listener used to subscribe to events.
 *
 * <ul>
 * <li>saves test name on scheduling a test</li>
 * <li>captures screenshots on test failure</li>
 * <li>captures screenshots for any test step name which contains 'print'</li>
 * </ul>
 *
 *
 * @author kkurian
 *
 */
public class TestReportListener implements TestLifecycleListener, StepLifecycleListener {

	public static ThreadLocal<String> TEST_NAME = new ThreadLocal<>();

	public static ThreadLocal<WebDriver> WEB_DRIVER = new ThreadLocal<>();

	@Override
	public void beforeTestSchedule(final TestResult result) {
		TEST_NAME.set(result.getName());
	}

	@Override
	public void afterTestUpdate(final TestResult result) {
		final WebDriver webDriver = WEB_DRIVER.get();
		if (webDriver != null && Status.BROKEN.equals(result.getStatus())) {
			Allure.addByteAttachmentAsync("Screenshot", "image/png", ".png",
					() -> ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.BYTES));
		}
	}

	@Override
	public void afterStepUpdate(final StepResult result) {
		final WebDriver webDriver = WEB_DRIVER.get();
		if (webDriver != null && result.getName().contains("print")) {
			Allure.addByteAttachmentAsync("Screenshot", "image/png", ".png",
					() -> ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.BYTES));
		}
	}

}
