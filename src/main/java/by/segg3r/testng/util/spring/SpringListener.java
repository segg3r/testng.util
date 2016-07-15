package by.segg3r.testng.util.spring;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.mockito.Mockito;
import org.mockito.internal.util.MockUtil;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.testng.IMethodInstance;
import org.testng.ITestClass;
import org.testng.ITestResult;

import by.segg3r.testng.util.TestClassListener;
import by.segg3r.testng.util.exception.ListenerException;
import by.segg3r.testng.util.spring.contextconfigurationprocessor.ContextConfigurationProcessor;
import by.segg3r.testng.util.spring.contextconfigurationprocessor.JavaConfigClassesContextConfigurationProcessor;
import by.segg3r.testng.util.spring.contextconfigurationprocessor.MockingAutowiringPostProcessorContextConfigurationProcessor;
import by.segg3r.testng.util.spring.contextconfigurationprocessor.RealObjectsContextConfigurationProcessor;
import by.segg3r.testng.util.spring.contextconfigurationprocessor.SpiesContextConfigurationProcessor;

import com.google.common.collect.Lists;

/**
 * 
 * Listener, which instantiates Spring context. Test class should be also annotated with
 * {@link by.segg3r.testng.util.spring.ContextConfiguration @ContextConfiguration},
 * which provides beans data for Spring context.
 * 
 * @see by.segg3r.testng.util.spring.ContextConfiguration
 * 
 * @author segg3r
 *
 */
public class SpringListener implements TestClassListener {

	private final static List<ContextConfigurationProcessor> CONTEXT_CONFIGURATION_PROCESSORS = Lists
			.newArrayList(
					new JavaConfigClassesContextConfigurationProcessor(),
					new RealObjectsContextConfigurationProcessor(),
					new SpiesContextConfigurationProcessor(),
					new MockingAutowiringPostProcessorContextConfigurationProcessor());

	private final Map<Object, GenericApplicationContext> applicationContexts;
	private final MockUtil mockUtil;

	private boolean initialized;
	private int invokedMethodsCounter;

	public SpringListener() {
		this.applicationContexts = new ConcurrentHashMap<>();
		this.mockUtil = new MockUtil();
	}

	@Override
	public void onBeforeClass(ITestClass testClass, IMethodInstance method) {
		if (initialized) {
			return;
		}
			
		Object[] suites = testClass.getInstances(true);
		for (Object suite : suites) {
			GenericApplicationContext applicationContext = initializeApplicationContext(suite);
			processAutowiredAnnotations(suite, applicationContext);
			applicationContexts.put(suite, applicationContext);
		}
		
		invokedMethodsCounter = testClass.getTestMethods().length;
		initialized = true;
	}

	@Override
	public void onAfterTest(ITestResult testResult) {
		invokedMethodsCounter--;
		
		Object suite = testResult.getInstance();
		ApplicationContext applicationContext = applicationContexts.get(suite);

		String[] beanNames = applicationContext.getBeanDefinitionNames();
		for (String beanName : beanNames) {
			Object bean = applicationContext.getBean(beanName);
			if (mockUtil.isMock(bean) || mockUtil.isSpy(bean)) {
				Mockito.reset(bean);
			}
		}
	}

	@Override
	public void onAfterClass(ITestClass testClass, IMethodInstance method) {
		if (invokedMethodsCounter == 0) {
			for (GenericApplicationContext applicationContext : applicationContexts
					.values()) {
				applicationContext.close();
			}
		}
	}

	private GenericApplicationContext initializeApplicationContext(Object suite) {
		ContextConfiguration contextConfiguration = suite.getClass()
				.getAnnotation(ContextConfiguration.class);
		if (contextConfiguration == null) {
			throw new ListenerException(
					"Suite class should be annotated with @SpringJavaConfiguration");
		}

		AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
		for (ContextConfigurationProcessor processor : CONTEXT_CONFIGURATION_PROCESSORS) {
			processor.process(applicationContext, contextConfiguration);
		}
		applicationContext.refresh();

		return applicationContext;
	}

	private void processAutowiredAnnotations(Object suite,
			ApplicationContext applicationContext) {
		AutowireCapableBeanFactory autowiringBeanFactory = applicationContext
				.getAutowireCapableBeanFactory();
		autowiringBeanFactory.autowireBean(suite);
	}

}
