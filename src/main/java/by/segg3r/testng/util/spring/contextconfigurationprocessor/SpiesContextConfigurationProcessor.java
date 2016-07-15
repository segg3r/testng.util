package by.segg3r.testng.util.spring.contextconfigurationprocessor;

import org.mockito.Mockito;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import by.segg3r.testng.util.spring.ContextConfiguration;
import by.segg3r.testng.util.spring.exception.ContextConfigurationProcessorException;

public class SpiesContextConfigurationProcessor implements
		ContextConfigurationProcessor {

	@Override
	public void process(AnnotationConfigApplicationContext applicationContext,
			ContextConfiguration contextConfiguration) {
		try {
			ConfigurableListableBeanFactory beanFactory = applicationContext.getBeanFactory();
			
			Class<?>[] spiesClasses = contextConfiguration.spies();
			for (Class<?> spyClass : spiesClasses) {
				Object realObject = spyClass.newInstance();
				Object spy = Mockito.spy(realObject);
				beanFactory.registerSingleton(spyClass.getCanonicalName(), spy);
			}
		} catch (InstantiationException | IllegalAccessException e) {
			throw new ContextConfigurationProcessorException(
					"Could not instantiate real object", e);
		}
	}
}
