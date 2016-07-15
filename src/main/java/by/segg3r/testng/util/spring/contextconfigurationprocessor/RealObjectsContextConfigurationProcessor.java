package by.segg3r.testng.util.spring.contextconfigurationprocessor;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import by.segg3r.testng.util.spring.ContextConfiguration;
import by.segg3r.testng.util.spring.exception.ContextConfigurationProcessorException;

public class RealObjectsContextConfigurationProcessor implements
		ContextConfigurationProcessor {

	@Override
	public void process(AnnotationConfigApplicationContext applicationContext,
			ContextConfiguration contextConfiguration) {
		try {
			ConfigurableListableBeanFactory beanFactory = applicationContext.getBeanFactory();
			
			Class<?>[] realObjectClasses = contextConfiguration.realObjects();
			for (Class<?> realObjectClass : realObjectClasses) {
				Object realObject = realObjectClass.newInstance();
				beanFactory.registerSingleton(realObjectClass.getCanonicalName(), realObject);
			}
		} catch (InstantiationException | IllegalAccessException e) {
			throw new ContextConfigurationProcessorException(
					"Could not instantiate real object", e);
		}
	}
}
