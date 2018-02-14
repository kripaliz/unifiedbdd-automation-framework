package com.klk.testing.automation;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Properties;

import javax.annotation.PostConstruct;

import org.apache.commons.collections.MapUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

import com.klk.testing.automation.allure.listener.ScreenshotListener;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.github.bonigarcia.wdm.WebDriverManager;
import io.qameta.allure.util.PropertiesUtils;

/**
 * The Spring Boot application configuration that starts wiring up the
 * application to be executed.
 *
 * @author kkurian
 *
 */
@SpringBootApplication
public class AutomationApplication {

	private static final Logger LOG = LoggerFactory.getLogger(AutomationApplication.class);

	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	private WebDriverConfig webDriverConfig;

	@Bean(destroyMethod = "quit")
	@Scope("cucumber-glue")
	public WebDriver webDriver() throws MalformedURLException {
		WebDriver webDriver = getWebDriver();
		ScreenshotListener.WEB_DRIVER.set(webDriver);
		return webDriver;
	}

	private WebDriver getWebDriver() throws MalformedURLException {
		switch (webDriverConfig.getType().toLowerCase()) {
		case "chrome":
			WebDriverManager.chromedriver().setup();
			return new ChromeDriver();
		case "firefox":
			WebDriverManager.firefoxdriver().setup();
			return new FirefoxDriver();
		case "ie":
			WebDriverManager.iedriver().setup();
			return new InternetExplorerDriver();
		case "edge":
			WebDriverManager.edgedriver().setup();
			return new EdgeDriver();
		case "safari":
			return new SafariDriver();
		case "opera":
			WebDriverManager.operadriver().setup();
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
		if (MapUtils.isNotEmpty(webDriverConfig.getDesiredCapabilities())) {
			webDriverConfig.getDesiredCapabilities().put("name", ScreenshotListener.TEST_NAME.get());
		}
		return new DesiredCapabilities(webDriverConfig.getDesiredCapabilities());
	}

	@PostConstruct
	public void writeAllureEnvironment() {
		String path = getAllureResultsPath();
		new File(path).mkdirs();
		File environmentFile = new File(path, "environment.properties");
		try (OutputStream outputStream = new FileOutputStream(environmentFile)) {
			Properties properties = new Properties();
			for (String key : Arrays.asList("spring.profiles.active")) {
				properties.put(key, applicationContext.getEnvironment().getProperty(key));
			}
			System.getProperties().entrySet().parallelStream()
					.filter(property -> Arrays.asList("threadCount", "cucumber.tags").contains(property.getKey()))
					.forEach(property -> properties.put(property.getKey(), property.getValue()));
			properties.store(outputStream, null);
		} catch (IOException e) {
			LOG.warn("error saving environment.properties", e);
		}
	}

	private String getAllureResultsPath() {
		final Properties properties = PropertiesUtils.loadAllureProperties();
		return properties.getProperty("allure.results.directory", "allure-results");
	}

	public static void main(final String[] args) {
		SpringApplication.run(AutomationApplication.class, args);
	}
}
