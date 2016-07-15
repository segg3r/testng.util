package by.segg3r.testng.util.spring.contextconfigurationprocessor;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import by.segg3r.testng.util.spring.ContextConfiguration;

public interface ContextConfigurationProcessor {

	void process(AnnotationConfigApplicationContext applicationContext,
			ContextConfiguration contextConfiguration);

}
