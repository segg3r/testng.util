package by.segg3r.testng.util.spring.contextconfigurationprocessor;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.ReflectionUtils.FieldCallback;
import org.springframework.util.ReflectionUtils.FieldFilter;

import com.beust.jcommander.internal.Lists;

import by.segg3r.testng.util.spring.ContextConfiguration;
import by.segg3r.testng.util.spring.exception.ContextConfigurationProcessorException;

public abstract class ApplicationContextBeansContextConfigurationProcessor
		implements ContextConfigurationProcessor {

	@Override
	public ContextConfigurationProcessingResult process(AnnotationConfigApplicationContext applicationContext,
			Optional<ContextConfiguration> contextConfiguration, Object suite) {
		try {
			if (contextConfiguration.isPresent()) {
				registerContextConfigurationSingletons(applicationContext,
						contextConfiguration.get());
			}
			List<Object> autowiringCandidates = registerAndInjectSuiteSingletons(applicationContext, suite);
			
			return new ContextConfigurationProcessingResult(autowiringCandidates);
		} catch (Exception e) {
			throw new ContextConfigurationProcessorException(
					"Error registering objects into ApplicationContext", e);
		}
	}

	private void registerContextConfigurationSingletons(
			AnnotationConfigApplicationContext applicationContext,
			ContextConfiguration contextConfiguration) throws Exception {
		Class<?>[] classes = getContextConfigurationClasses(contextConfiguration);
		for (Class<?> clazz : classes) {
			findOrRegisterSingleton(applicationContext, clazz,
					clazz.getCanonicalName());
		}
	}

	private List<Object> registerAndInjectSuiteSingletons(
			AnnotationConfigApplicationContext applicationContext, Object suite) {
		List<Object> autowiringCandidates = Lists.newArrayList();
		
		ReflectionUtils.doWithFields(suite.getClass(), new FieldCallback() {
			@Override
			public void doWith(Field field) throws IllegalArgumentException,
					IllegalAccessException {
				try {
					Class<?> realObjectClass = field.getType();
					String singletonName = realObjectClass.getCanonicalName()
							+ field.getName();
					Object singleton = findOrRegisterSingleton(applicationContext,
							realObjectClass, singletonName);

					field.setAccessible(true);
					field.set(suite, singleton);
					
					autowiringCandidates.add(singleton);
				} catch (Exception e) {
					throw new ContextConfigurationProcessorException("Could not inject field " + field.getName(), e);
				}
			}
		}, new FieldFilter() {
			@Override
			public boolean matches(Field field) {
				return isInjectedField(field);
			}
		});
		
		return autowiringCandidates;
	}

	private Object findOrRegisterSingleton(
			AnnotationConfigApplicationContext applicationContext,
			Class<?> clazz, String beanName) throws Exception {
		ConfigurableListableBeanFactory beanFactory = applicationContext
				.getBeanFactory();

		Object existingSingleton = beanFactory.getSingleton(beanName);
		if (existingSingleton != null) {
			return existingSingleton;
		}

		Object realObject = createSingleton(clazz);
		beanFactory.registerSingleton(beanName, realObject);
		return realObject;
	}

	protected abstract Class<?>[] getContextConfigurationClasses(
			ContextConfiguration contextConfiguration);

	protected abstract Object createSingleton(Class<?> clazz) throws Exception;

	protected abstract boolean isInjectedField(Field field);

}
