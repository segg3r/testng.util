package by.segg3r.testng.util.spring;

import by.segg3r.testng.util.spring.contextconfigurationprocessor.*;
import by.segg3r.testng.util.spring.exception.SpringContextListenerException;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.testng.ITestNGListener;
import org.testng.annotations.Listeners;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static by.segg3r.testng.util.spring.ContextConfigurationProcessingOption.WITH_CLASS_HIERARCHY;
import static by.segg3r.testng.util.spring.ContextConfigurationProcessingOption.WITH_INTERFACES;
import static java.util.Collections.singleton;

public class SuiteApplicationContextBuilder {

	private final static List<ContextConfigurationProcessor> CONTEXT_CONFIGURATION_PROCESSORS = Lists
			.newArrayList(
					new JavaConfigClassesContextConfigurationProcessor(),
					new RealObjectsContextConfigurationProcessor(),
					new SpiesContextConfigurationProcessor(),
					new MocksContextConfigurationProcessor(),
					new MockingAutowiringPostProcessorContextConfigurationProcessor());

	private Collection<Object> suites;
	private AnnotationConfigApplicationContext applicationContext;

	public SuiteApplicationContextBuilder(Object suite) {
		this(singleton(suite));
	}

	public SuiteApplicationContextBuilder(Collection<Object> suites) {
		this.suites = suites;
		this.applicationContext = new AnnotationConfigApplicationContext();
	}

	public ApplicationContextInitializationResult build()
			throws Exception {
		Optional<ContextConfigurationProcessingResult> resultingConfiguration = suites.stream()
				.map(suite -> {
					ContextConfigurationProcessingResult suiteClassHierarchyConfiguration
							= getClassContextConfigurationWithOptions(suite, suite.getClass(),
							WITH_CLASS_HIERARCHY, WITH_INTERFACES);
					ContextConfigurationProcessingResult listenersConfiguration
							= getListenersConfiguration(suite);
					return ContextConfigurationProcessingResult
							.merge(suiteClassHierarchyConfiguration, listenersConfiguration);
				})
				.reduce(ContextConfigurationProcessingResult::merge);

		if (!resultingConfiguration.isPresent()) {
			throw new SpringContextListenerException("Could not reduce to resulting configuration.");
		}

		applicationContext.refresh();
		return new ApplicationContextInitializationResult(
				applicationContext,
				resultingConfiguration.get().getAutowiringCandidates());
	}

	private ContextConfigurationProcessingResult getListenersConfiguration(Object suite) {
		List<ContextConfigurationProcessingResult> results = Lists.newArrayList();

		Listeners listenersAnnotation = suite.getClass().getAnnotation(Listeners.class);
		if (listenersAnnotation != null) {
			Class<?>[] listenerClasses = listenersAnnotation.value();
			for (Class<?> listenerClass : listenerClasses) {
				if (!listenerClass.equals(SpringContextListener.class)) {
					results.add(getClassContextConfigurationWithOptions(suite, listenerClass, WITH_INTERFACES));
				}
			}
		}

		return ContextConfigurationProcessingResult.merge(results);
	}

	private ContextConfigurationProcessingResult getClassContextConfigurationWithOptions(
			Object suite, Class<?> clazz, ContextConfigurationProcessingOption... optionsArray) {
		List<ContextConfigurationProcessingOption> options = Lists.newArrayList(optionsArray);
		List<ContextConfigurationProcessingResult> contextConfigurationProcessingResults = Lists.newArrayList();

		Class<?> classHierarchyElement = clazz;
		while (classHierarchyElement != null) {
			contextConfigurationProcessingResults.add(
					getClassContextConfiguration(suite, classHierarchyElement));

			if (options.contains(ContextConfigurationProcessingOption.WITH_INTERFACES)) {
				for (Class<?> hierarchyInterfaceElement : classHierarchyElement.getInterfaces()) {
					if (!hierarchyInterfaceElement.isAssignableFrom(ITestNGListener.class)) {
						contextConfigurationProcessingResults.add(getClassContextConfigurationWithOptions(
								suite, hierarchyInterfaceElement, ContextConfigurationProcessingOption.WITH_INTERFACES));
					}
				}
			}

			if (!options.contains(ContextConfigurationProcessingOption.WITH_CLASS_HIERARCHY)) {
				break;
			}

			classHierarchyElement = classHierarchyElement.getSuperclass();
		}

		return ContextConfigurationProcessingResult.merge(contextConfigurationProcessingResults);
	}

	private ContextConfigurationProcessingResult getClassContextConfiguration(
			Object suite, Class<?> clazz) {
		List<Object> autowiringCandidates = Lists.newArrayList();
		ContextConfiguration contextConfiguration = clazz
				.getAnnotation(ContextConfiguration.class);
		for (ContextConfigurationProcessor processor : CONTEXT_CONFIGURATION_PROCESSORS) {
			ContextConfigurationProcessingResult processorResult = processor
					.process(applicationContext, Optional.ofNullable(contextConfiguration), suite);
			autowiringCandidates.addAll(processorResult.getAutowiringCandidates());
		}

		return ContextConfigurationProcessingResult.withAutowiringCandidates(autowiringCandidates);
	}

}
