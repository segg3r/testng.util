package by.segg3r.testng.util.spring.contextconfigurationprocessor;

import java.util.Optional;

import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import by.segg3r.testng.util.spring.ContextConfiguration;
import by.segg3r.testng.util.spring.MockAutowiredAnnotationBeanPostProcessor;

public class MockingAutowiringPostProcessorContextConfigurationProcessor
		implements ContextConfigurationProcessor {

	private static final String INTERNAL_AUTOWIRED_ANNOTATION_PROCESSOR_BEAN_NAME
			= "org.springframework.context.annotation.internalAutowiredAnnotationProcessor";

	@Override
	public ContextConfigurationProcessingResult process(
			AnnotationConfigApplicationContext applicationContext,
			Optional<ContextConfiguration> contextConfiguration, Object suite) {
		ConfigurableListableBeanFactory beanFactory = applicationContext
				.getBeanFactory();

		if (beanFactory.getSingleton(INTERNAL_AUTOWIRED_ANNOTATION_PROCESSOR_BEAN_NAME) == null) {
			AutowiredAnnotationBeanPostProcessor autowiringPostProcessor = new MockAutowiredAnnotationBeanPostProcessor(applicationContext);
			autowiringPostProcessor.setBeanFactory(beanFactory);
			beanFactory
					.registerSingleton(
							INTERNAL_AUTOWIRED_ANNOTATION_PROCESSOR_BEAN_NAME,
							autowiringPostProcessor);
		}

		return ContextConfigurationProcessingResult.empty();
	}

}
