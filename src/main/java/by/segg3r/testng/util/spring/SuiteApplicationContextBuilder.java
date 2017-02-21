package by.segg3r.testng.util.spring;

import by.segg3r.testng.util.spring.contextconfigurationprocessor.*;
import com.google.common.collect.Lists;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.testng.ITestNGListener;
import org.testng.annotations.Listeners;

import java.util.List;
import java.util.Optional;

import static by.segg3r.testng.util.spring.ContextConfigurationProcessingOption.WITH_CLASS_HIERARCHY;
import static by.segg3r.testng.util.spring.ContextConfigurationProcessingOption.WITH_INTERFACES;

public class SuiteApplicationContextBuilder {

	private final static List<ContextConfigurationProcessor> CONTEXT_CONFIGURATION_PROCESSORS = Lists
			.newArrayList(
					new JavaConfigClassesContextConfigurationProcessor(),
					new RealObjectsContextConfigurationProcessor(),
					new SpiesContextConfigurationProcessor(),
					new MocksContextConfigurationProcessor(),
					new MockingAutowiringPostProcessorContextConfigurationProcessor());

	private Object suite;
	private AnnotationConfigApplicationContext applicationContext;

	public SuiteApplicationContextBuilder(Object suite) {
		this.suite = suite;
		this.applicationContext = new AnnotationConfigApplicationContext();
	}

	public ApplicationContextInitializationResult build()
			throws Exception {
		ContextConfigurationProcessingResult suiteClassHierarchyConfiguration
				= getClassContextConfigurationWithOptions(suite.getClass(), WITH_CLASS_HIERARCHY, WITH_INTERFACES);
		ContextConfigurationProcessingResult listenersConfiguration
				= getListenersConfiguration();

		ContextConfigurationProcessingResult resultingConfiguration = ContextConfigurationProcessingResult
				.merge(suiteClassHierarchyConfiguration, listenersConfiguration);

		applicationContext.refresh();

		return new ApplicationContextInitializationResult(
				applicationContext,
				resultingConfiguration.getAutowiringCandidates());
	}

	private ContextConfigurationProcessingResult getListenersConfiguration() {
		List<ContextConfigurationProcessingResult> results = Lists.newArrayList();

		Listeners listenersAnnotation = suite.getClass().getAnnotation(Listeners.class);
		if (listenersAnnotation != null) {
			Class<?>[] listenerClasses = listenersAnnotation.value();
			for (Class<?> listenerClass : listenerClasses) {
				if (!listenerClass.equals(SpringContextListener.class)) {
					results.add(getClassContextConfigurationWithOptions(listenerClass, WITH_INTERFACES));
				}
			}
		}

		return ContextConfigurationProcessingResult.merge(results);
	}

	private ContextConfigurationProcessingResult getClassContextConfigurationWithOptions(
			Class<?> clazz, ContextConfigurationProcessingOption... optionsArray) {
		List<ContextConfigurationProcessingOption> options = Lists.newArrayList(optionsArray);
		List<ContextConfigurationProcessingResult> contextConfigurationProcessingResults = Lists.newArrayList();

		Class<?> classHierarchyElement = clazz;
		while (classHierarchyElement != null) {
			contextConfigurationProcessingResults.add(
					getClassContextConfiguration(classHierarchyElement));

			if (options.contains(ContextConfigurationProcessingOption.WITH_INTERFACES)) {
				for (Class<?> hierarchyInterfaceElement : classHierarchyElement.getInterfaces()) {
					if (!hierarchyInterfaceElement.isAssignableFrom(ITestNGListener.class)) {
						contextConfigurationProcessingResults.add(getClassContextConfigurationWithOptions(
								hierarchyInterfaceElement, ContextConfigurationProcessingOption.WITH_INTERFACES));
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
			Class<?> clazz) {
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
