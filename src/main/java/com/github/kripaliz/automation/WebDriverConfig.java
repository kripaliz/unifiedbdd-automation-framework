package com.github.kripaliz.automation;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

/**
 * A configuration bean for webDriver related properties.
 *
 * @author kkurian
 *
 */
@Component
@ConfigurationProperties("webdriver")
@Data
public class WebDriverConfig {

	private String type;

	private String url;

	private Map<String, Object> desiredCapabilities;
}
