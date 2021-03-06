package by.segg3r.testng.util.spring.contextconfigurationprocessor;

import java.lang.reflect.Field;

import org.mockito.Mockito;

import by.segg3r.testng.util.spring.ContextConfiguration;
import by.segg3r.testng.util.spring.annotations.Spied;

public class SpiesContextConfigurationProcessor extends
		ApplicationContextBeansContextConfigurationProcessor {

	@Override
	protected Class<?>[] getContextConfigurationClasses(
			ContextConfiguration contextConfiguration) {
		return contextConfiguration.spies();
	}

	@Override
	protected Object createSingleton(Class<?> clazz) throws Exception {
		Object object = clazz.newInstance();
		return Mockito.spy(object);
	}

	@Override
	protected boolean isInjectedField(Field field) {
		return field.getAnnotation(Spied.class) != null;
	}

}