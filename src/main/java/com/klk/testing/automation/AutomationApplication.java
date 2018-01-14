package com.klk.testing.automation;

import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.github.bonigarcia.wdm.ChromeDriverManager;
import io.github.bonigarcia.wdm.FirefoxDriverManager;
import io.github.bonigarcia.wdm.InternetExplorerDriverManager;
import io.github.bonigarcia.wdm.OperaDriverManager;

/**
 * The Spring Boot application configuration that starts wiring up the
 * application to be executed.
 * 
 * @author kkurian
 *
 */
@SpringBootApplication
public class AutomationApplication {

	@Autowired
	private WebDriverConfig webDriverConfig;

	@Bean(destroyMethod = "quit")
	@Scope("cucumber-glue")
	public WebDriver webDriver() throws MalformedURLException {
		switch (webDriverConfig.getType().toLowerCase()) {
		case "chrome":
			ChromeDriverManager.getInstance().setup();
			return new ChromeDriver();
		case "firefox":
			FirefoxDriverManager.getInstance().setup();
			return new FirefoxDriver();
		case "ie":
			InternetExplorerDriverManager.getInstance().setup();
			return new InternetExplorerDriver();
		case "safari":
			return new SafariDriver();
		case "opera":
			OperaDriverManager.getInstance().setup();
			return new OperaDriver();
		case "remote":
			return new RemoteWebDriver(new URL(webDriverConfig.getUrl()), getCapabilities());
		case "android":
			DesiredCapabilities capabilities = getCapabilities();
			capabilities.setCapability("unicodeKeyboard", "true");
			capabilities.setCapability("resetKeyboard", "true");
			return new AndroidDriver<>(new URL(webDriverConfig.getUrl()), capabilities);
		case "ios":
			return new IOSDriver<>(new URL(webDriverConfig.getUrl()), getCapabilities());
		default:
			throw new IllegalArgumentException(
					String.format("Web driver %s not supported.", webDriverConfig.getType()));
		}
	}

	protected DesiredCapabilities getCapabilities() {
		return new DesiredCapabilities(webDriverConfig.getDesiredCapabilities());
	}

	public static void main(final String[] args) {
		SpringApplication.run(AutomationApplication.class, args);
	}
}
