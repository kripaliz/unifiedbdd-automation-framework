package com.klk.testing.automation;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@ConfigurationProperties("webdriver")
@Data
public class WebDriverConfig {

	private String type;

	private String url;

	private Map<String, String> desiredCapabilities;
}
