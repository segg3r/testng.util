package by.segg3r.testng.util.spring.contextconfigurationprocessor;

import java.lang.reflect.Field;

import org.mockito.Mockito;

import by.segg3r.testng.util.spring.ContextConfiguration;
import by.segg3r.testng.util.spring.annotations.Mocked;

public class MocksContextConfigurationProcessor extends
		ApplicationContextBeansContextConfigurationProcessor {

	@Override
	protected Class<?>[] getContextConfigurationClasses(
			ContextConfiguration contextConfiguration) {
		return new Class<?>[] {};
	}

	@Override
	protected Object createSingleton(Class<?> clazz) throws Exception {
		return Mockito.mock(clazz);
	}

	@Override
	protected boolean isInjectedField(Field field) {
		return field.getAnnotation(Mocked.class) != null;
	}

}