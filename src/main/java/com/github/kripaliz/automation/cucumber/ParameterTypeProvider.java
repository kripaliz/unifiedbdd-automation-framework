package com.github.kripaliz.automation.cucumber;

import java.util.ServiceLoader;

import io.cucumber.core.api.TypeRegistry;
import io.cucumber.cucumberexpressions.ParameterType;

/**
 * Implement this interface to add custom {@link ParameterType}s.
 * Implementations are added to {@link TypeRegistry} using {@link ServiceLoader}
 *
 * @author kkurian
 *
 */
public interface ParameterTypeProvider<T> {

	/**
	 * Create a {@link ParameterType} to be added to cucumber4 {@link TypeRegistry}
	 *
	 * @return {@link ParameterType}
	 */
	ParameterType<T> create();
}
