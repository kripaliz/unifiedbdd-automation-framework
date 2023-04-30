# UnifiedBDD Automation Framework

## BDD automation testing made simple
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.kripaliz/unifiedbdd-automation-framework/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.kripaliz/unifiedbdd-automation-framework)

Automation framework / solution implemented in Java to support web browser as well as mobile browser / App automation. This includes

* abstraction for PageObject
* ContextLoader for Spring context initialisation
* Junit5 test runner that kicks off cucumber
* Spring Boot application configuration
* Webdriver properties, test data properties

## Built With

* [Cucumber](https://cucumber.io/docs/cucumber/) – BDD tests
* [Allure Reports](https://docs.qameta.io/allure/) - test report
* [Selenium](https://www.seleniumhq.org/) – web automation
* [Appium](http://appium.io/) – mobile automation
* [Page Object Model](https://www.selenium.dev/documentation/test_practices/encouraged/page_object_models/) – design pattern to abstract page behaviour
* [Spring Boot](http://spring.io/projects/spring-boot) – cleaner code
* [Webdriver Manager](https://github.com/bonigarcia/webdrivermanager) – manage webdriver executables automatically

## Prerequisites

* [JDK 11](https://openjdk.org/projects/jdk/11)
* [Maven](https://maven.apache.org/download.cgi)

## Usage

To use this automation framework in your test suite:

1. Inherit from [unifiedbdd-automation-parent](https://github.com/kripaliz/unifiedbdd-automation-parent)

	```xml
	<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
		<modelVersion>4.0.0</modelVersion>
		<parent>
			<groupId>com.github.kripaliz</groupId>
			<artifactId>unifiedbdd-automation-parent</artifactId>
			<version>0.1.6</version>
		</parent>
		<groupId>com.company.testing</groupId>
		<artifactId>uiautomation-suite</artifactId>
		<version>0.0.1-SNAPSHOT</version>

		<properties>
			<cucumber.execution.parallel.enabled>true</cucumber.execution.parallel.enabled>
			<cucumber.glue>com.company.testing.step</cucumber.glue>
			<cucumber.plugin>json:target/cucumber-reports/result.json,junit:target/cucumber-reports/result.xml</cucumber.plugin>
		</properties>
	</project>
	```

2. Create an application.yml file for spring boot

	```yml
	spring.main.sources: com.company.testing

	spring.profiles.active: chrome

	---
	spring.config.activate.on-profile: chrome
	webdriver:
	  type: chrome

	---
	spring.config.activate.on-profile: saucelabs
	webdriver:
	  type: remote
	  url: http://user:password@ondemand.saucelabs.com:80/wd/hub
	  desiredCapabilities:
	    browserName: chrome
	    browserVersion: latest
	    platformName: macOS 10.13
	```

3. Create [logback](https://logback.qos.ch/manual/configuration.html) xml

	```xml
	<?xml version="1.0" encoding="UTF-8"?>
	<configuration>

		<appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
			<!-- encoders are assigned the type ch.qos.logback.classic.encoder.PatternLayoutEncoder
				by default -->
			<encoder>
				<pattern>%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n</pattern>
			</encoder>
		</appender>

		<logger name="org.apache.http" level="INFO" />
		<logger name="org.springframework" level="INFO" />

		<root level="debug">
			<appender-ref ref="STDOUT" />
		</root>
	</configuration>
	```
4. Create a Tests class

	```java
	@Cucumber
	public class AutomationTests {

	}
	```

	`com.company.testing.step` is the package under which stepDef files can be created

5. Create PageObject classes that extend `com.github.kripaliz.automation.pageobject.AbstractPage` and use the marker interface `com.github.kripaliz.automation.pageobject.PageObject`.

	```java
	@PageObject
	public class AnukoHomePage extends AbstractPage {

		@FindBy(css = AnukoHomePageConstants.LOGIN_LINK_CSS)
		private WebElement loginLink;

		public void visitUrl() {
			webDriver.get("https://timetracker.anuko.com/");
		}
	}
	```
6. Create [StepDef](https://cucumber.io/docs/cucumber/step-definitions/) classes that use spring dependency injection to get pageObjects.

	```java
	public class SignInSteps {

		@Autowired
		private AnukoHomePage anukoHomePage;

		@Given("^I visit Anuko Home Page$")
		public void i_visit_Anuko_Home_Page() throws Exception {
		    anukoHomePage.visitUrl();
		}
	}
	```
7. Create [gherkin](https://cucumber.io/docs/gherkin/) feature files in src/test/resources/features

8. To run the suite

	```sh
	mvn clean test \
	-Dcucumber.execution.parallel.config.fixed.parallelism=10 \
	-Dgroups="!wip" \
	-Dspring.profiles.active=chrome \
	-Dsurefire.rerunFailingTestsCount=0
	```

	options:
	 - `cucumber.execution.parallel.config.fixed.parallelism`: specify the number of concurrent scenarios to execute
	 - `groups`: [junit tag expressions](https://github.com/cucumber/cucumber-jvm/tree/main/cucumber-junit-platform-engine#tags) to limit scope of test execution
	 - `spring.profiles.active`: switch between [spring profiles](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.profiles) created in application.yml
	 - `surefire.rerunFailingTestsCount`: reruns for any failed tests

9. To view the [allure report](https://github.com/allure-framework/allure-maven)

	```sh
	mvn allure:serve
	```

10. To re-run failed tests

	```sh
	mvn test -Dcucumber.options=@target/rerun.txt -Dspring.profiles.active=chrome -DthreadCount=4
	```

## Configuring the TypeRegistry

Cucumber 3+ provides `TypeRegistryConfigurer` to configure custom parameter types and data table types. The framework already defines one to setup a default DataTable Transformer using Jackson library.
To extend it, please use ServiceLoader mechanism:

* Create an implementation of `DataTableTypeProvider` or `ParameterTypeProvider`
* In your test suite, add file(s) with the implementations referenced:
    * `META-INF/services/com.github.kripaliz.automation.cucumber.DataTableTypeProvider`
    * `META-INF/services/com.github.kripaliz.automation.cucumber.ParameterTypeProvider`

Here's some more info on ServiceLoader; https://www.baeldung.com/java-spi

## Setting up eclipse for developing

* Download [eclipse](https://www.eclipse.org/downloads/)
* Follow instructions [here](https://projectlombok.org/setup/eclipse) for lombok setup
* Install eclipse plugins from the [eclipse marketplace](https://marketplace.eclipse.org/content/welcome-eclipse-marketplace) or using their [update site](https://help.eclipse.org/kepler/index.jsp?topic=%2Forg.eclipse.platform.doc.user%2Ftasks%2Ftasks-127.htm) - [YEdit](http://dadacoalition.org/yedit/), [Cucumber](https://cucumber.github.io/cucumber-eclipse-update-site-snapshot)
* Follow instructions in the answer [here](https://stackoverflow.com/questions/1886185/eclipse-and-windows-newlines) to use Unix style line endings for new files
