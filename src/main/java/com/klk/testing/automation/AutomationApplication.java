package com.klk.testing.automation;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.safari.SafariDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Scope;

import cucumber.runtime.java.spring.GlueConfig;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;

/**
 * @author kkurian
 *
 */
@SpringBootApplication
@Import(GlueConfig.class)
public class AutomationApplication {

	@Bean(destroyMethod = "quit")
	@Scope("cucumber-glue")
	public WebDriver webDriver(@Value("${webDriver.browser}") final String webDriver) {

		if ("htmlunit".equals(webDriver)) {
			return new HtmlUnitDriver();
		}

		if ("chrome".equals(webDriver)) {
			return new ChromeDriver();
		}

		if ("firefox".equals(webDriver)) {
			return new FirefoxDriver();
		}

		if ("ie".equals(webDriver)) {
			return new InternetExplorerDriver();
		}

		if ("safari".equals(webDriver)) {
			return new SafariDriver();
		}

		if ("opera".equals(webDriver)) {
			return new OperaDriver();
		}

		throw new IllegalArgumentException(String.format("Web driver %s not supported.", webDriver));
	}

	@Bean
	@ConfigurationProperties("appium.desiredCapabilities")
	public Map<String, String> rawMap() {
		return new HashMap<String, String>();
	}

	@Bean
	public DesiredCapabilities desiredCapabilities() {
		return new DesiredCapabilities(rawMap());
	}

	@Bean(destroyMethod = "quit")
	@Scope("cucumber-glue")
	public AppiumDriver<WebElement> appiumDriver(@Value("${appium.url}") final String url)
			throws MalformedURLException {
		return new AndroidDriver<>(new URL(url), desiredCapabilities());
	}

	public static void main(final String[] args) {
		SpringApplication.run(AutomationApplication.class, args);
	}
}
