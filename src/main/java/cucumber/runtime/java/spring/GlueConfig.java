/**
 *
 */
package cucumber.runtime.java.spring;

import org.springframework.beans.factory.config.CustomScopeConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author kkurian
 *
 */
@Configuration
public class GlueConfig {

	@Bean
	public CustomScopeConfigurer glueScopeConfigurer() {
		final CustomScopeConfigurer toReturn = new CustomScopeConfigurer();
		toReturn.addScope("cucumber-glue", new GlueCodeScope());
		return toReturn;
	}
}
