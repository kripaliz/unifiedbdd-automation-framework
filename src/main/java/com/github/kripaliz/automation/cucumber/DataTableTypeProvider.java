/**
 *
 */
package com.github.kripaliz.automation.cucumber;

import java.util.ServiceLoader;

import io.cucumber.core.api.TypeRegistry;
import io.cucumber.datatable.DataTableType;

/**
 * Implement this interface to add custom {@link DataTableType}s.
 * Implementations are added to {@link TypeRegistry} using {@link ServiceLoader}
 *
 * @author kkurian
 */
public interface DataTableTypeProvider {

	/**
	 * Create a {@link DataTableType} to be added to cucumber4 {@link TypeRegistry}
	 *
	 * @return {@link DataTableType}
	 */
	DataTableType create();
}
