package com.github.kripaliz.automation.cucumber.glue;

import java.util.Locale;
import java.util.Map;
import java.util.ServiceLoader;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.kripaliz.automation.cucumber.DataTableTypeProvider;
import com.github.kripaliz.automation.cucumber.ParameterTypeProvider;

import io.cucumber.core.api.TypeRegistry;
import io.cucumber.core.api.TypeRegistryConfigurer;
import io.cucumber.datatable.TableCellByTypeTransformer;
import io.cucumber.datatable.TableEntryByTypeTransformer;

/**
 * Default converter that will handle all types for which no converter has been
 * defined
 *
 * @author kkurian
 * @see https://cucumber.io/blog/announcing-cucumber-jvm-4-0-0/
 */
public class TypeRegistryConfiguration implements TypeRegistryConfigurer {

	@Override
	public Locale locale() {
		return Locale.ENGLISH;
	}

	@Override
	public void configureTypeRegistry(final TypeRegistry typeRegistry) {
		final JacksonTableTransformer jacksonTableTransformer = new JacksonTableTransformer();
		typeRegistry.setDefaultDataTableEntryTransformer(jacksonTableTransformer);

		final ServiceLoader<DataTableTypeProvider> dataTableTypeloader = ServiceLoader
				.load(DataTableTypeProvider.class);
		dataTableTypeloader.forEach(dataTableTypeProvider -> {
			typeRegistry.defineDataTableType(dataTableTypeProvider.create());
		});
		@SuppressWarnings("rawtypes")
		final ServiceLoader<ParameterTypeProvider> parameterTypeLoader = ServiceLoader
				.load(ParameterTypeProvider.class);
		parameterTypeLoader.forEach(parameterTypeProvider -> {
			typeRegistry.defineParameterType(parameterTypeProvider.create());
		});

	}

	private static final class JacksonTableTransformer
			implements TableEntryByTypeTransformer, TableCellByTypeTransformer {

		private final ObjectMapper objectMapper = new ObjectMapper();

		@Override
		public <T> T transform(final Map<String, String> entry, final Class<T> type,
				final TableCellByTypeTransformer cellTransformer) {
			return objectMapper.convertValue(entry, type);
		}

		@Override
		public <T> T transform(final String value, final Class<T> cellType) {
			return objectMapper.convertValue(value, cellType);
		}
	}
}
