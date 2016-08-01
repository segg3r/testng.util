package by.segg3r.testng.util.spring.contextconfigurationprocessor;

import java.lang.reflect.Field;

import by.segg3r.testng.util.spring.ContextConfiguration;
import by.segg3r.testng.util.spring.annotations.Real;

public class RealObjectsContextConfigurationProcessor extends
		ApplicationContextBeansContextConfigurationProcessor {

	@Override
	protected Class<?>[] getContextConfigurationClasses(
			ContextConfiguration contextConfiguration) {
		return contextConfiguration.realObjects();
	}

	@Override
	protected Object createSingleton(Class<?> clazz) throws Exception {
		return clazz.newInstance();
	}

	@Override
	protected boolean isInjectedField(Field field) {
		return field.getAnnotation(Real.class) != null;
	}
	
}