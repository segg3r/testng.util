package by.segg3r.testng.util.spring.contextconfigurationprocessor;

import java.util.Optional;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import by.segg3r.testng.util.spring.ContextConfiguration;

public class JavaConfigClassesContextConfigurationProcessor implements
		ContextConfigurationProcessor {

	@Override
	public ContextConfigurationProcessingResult process(AnnotationConfigApplicationContext applicationContext,
			Optional<ContextConfiguration> contextConfiguration, Object suite) {
		if (contextConfiguration.isPresent()) {
			Class<?>[] configClasses = contextConfiguration.get().configClasses();
			applicationContext.register(configClasses);
		}
		
		return ContextConfigurationProcessingResult.empty();
	}

}
