package by.segg3r.testng.util.spring.contextconfigurationprocessor;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import by.segg3r.testng.util.spring.ContextConfiguration;

public class JavaConfigClassesContextConfigurationProcessor implements ContextConfigurationProcessor {

	@Override
	public void process(
			AnnotationConfigApplicationContext applicationContext,
			ContextConfiguration contextConfiguration) {
		Class<?>[] configClasses = contextConfiguration.configClasses();
		applicationContext.register(configClasses);
	}

}
