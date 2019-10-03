package com.github.kripaliz.automation;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.kripaliz.automation.cucumber.plugin.TestReportListener;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.github.bonigarcia.wdm.WebDriverManager;
import io.qameta.allure.util.PropertiesUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * The Spring Boot application configuration that starts wiring up the
 * application to be executed.
 *
 * @author kkurian
 *
 */
@Slf4j
@SpringBootApplication
public class AutomationApplication {

	private static ThreadLocal<WebDriver> WEB_DRIVER = new ThreadLocal<>();

	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	private WebDriverConfig webDriverConfig;

	public static WebDriver getWebDriver() {
		return WEB_DRIVER.get();
	}

	@Bean
	public ObjectMapper objectMapper() {
		return new ObjectMapper();
	}

	@Bean(destroyMethod = "quit")
	@Scope("cucumber-glue")
	public WebDriver webDriver() throws MalformedURLException {
		final WebDriver webDriver = createWebDriver();
		WEB_DRIVER.set(webDriver);
		return webDriver;
	}

	private WebDriver createWebDriver() throws MalformedURLException {
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
			final DesiredCapabilities capabilities = getCapabilities();
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
			webDriverConfig.getDesiredCapabilities().put("name", TestReportListener.getTestName());
		}
		return new DesiredCapabilities(webDriverConfig.getDesiredCapabilities());
	}

	@PostConstruct
	public void writeAllureEnvironment() {
		final String path = getAllureResultsPath();
		new File(path).mkdirs();
		final File environmentFile = new File(path, "environment.properties");
		try (OutputStream outputStream = new FileOutputStream(environmentFile)) {
			final Properties properties = new Properties();
			for (final String key : Arrays.asList("spring.profiles.active")) {
				properties.put(key, applicationContext.getEnvironment().getProperty(key));
			}
			System.getProperties().entrySet().parallelStream()
					.filter(property -> Arrays.asList("threadCount", "cucumber.tags").contains(property.getKey()))
					.forEach(property -> properties.put(property.getKey(), property.getValue()));
			properties.store(outputStream, null);
		} catch (final IOException e) {
			log.warn("error saving environment.properties", e);
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
