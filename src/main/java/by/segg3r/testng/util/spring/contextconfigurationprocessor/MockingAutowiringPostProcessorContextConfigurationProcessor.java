package by.segg3r.testng.util.spring.contextconfigurationprocessor;

import java.util.Optional;

import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import by.segg3r.testng.util.spring.ContextConfiguration;
import by.segg3r.testng.util.spring.MockAutowiredAnnotationBeanPostProcessor;

public class MockingAutowiringPostProcessorContextConfigurationProcessor
		implements ContextConfigurationProcessor {

	@Override
	public ContextConfigurationProcessingResult process(
			AnnotationConfigApplicationContext applicationContext,
			Optional<ContextConfiguration> contextConfiguration, Object suite) {
		AutowiredAnnotationBeanPostProcessor autowiringPostProcessor = new MockAutowiredAnnotationBeanPostProcessor(applicationContext);
		ConfigurableListableBeanFactory beanFactory = applicationContext
				.getBeanFactory();
		autowiringPostProcessor.setBeanFactory(beanFactory);
		beanFactory
				.registerSingleton(
						"org.springframework.context.annotation.internalAutowiredAnnotationProcessor",
						autowiringPostProcessor);
		
		return ContextConfigurationProcessingResult.empty();
	}

}
