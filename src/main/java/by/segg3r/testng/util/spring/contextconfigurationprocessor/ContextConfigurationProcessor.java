package by.segg3r.testng.util.spring.contextconfigurationprocessor;

import java.util.Optional;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import by.segg3r.testng.util.spring.ContextConfiguration;

public interface ContextConfigurationProcessor {

	ContextConfigurationProcessingResult process(AnnotationConfigApplicationContext applicationContext,
			Optional<ContextConfiguration> contextConfiguration, Object suite);

}
