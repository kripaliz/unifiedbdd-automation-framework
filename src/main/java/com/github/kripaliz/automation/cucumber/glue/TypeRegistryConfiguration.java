package com.github.kripaliz.automation.cucumber.glue;

import java.util.Locale;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

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
