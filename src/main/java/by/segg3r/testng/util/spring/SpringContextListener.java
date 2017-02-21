package by.segg3r.testng.util.spring;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.util.MockUtil;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.testng.IMethodInstance;
import org.testng.ITestClass;
import org.testng.ITestResult;

import by.segg3r.testng.util.TestClassContextListener;
import by.segg3r.testng.util.spring.exception.SpringContextListenerException;

/**
 * 
 * Listener, which instantiates Spring context. <br>
 * There are currently two ways of configure beans in this context: <br>
 * <br>
 * <b>1.</b> Test class can be annotated with
 * {@link by.segg3r.testng.util.spring.ContextConfiguration @ContextConfiguration}, which provides beans data for Spring context. <br>
 * <b>2.</b> Fields of test suite can be annotated with one of injection annotations. Check
 * {@link by.segg3r.testng.util.spring.annotations.Real @Real},
 * {@link by.segg3r.testng.util.spring.annotations.Spied @Spied} and
 * {@link by.segg3r.testng.util.spring.annotations.Mocked @Mocked} annotations.
 * 
 * @see by.segg3r.testng.util.spring.ContextConfiguration
 * @see by.segg3r.testng.util.spring.annotations.Real
 * @see by.segg3r.testng.util.spring.annotations.Spied
 * @see by.segg3r.testng.util.spring.annotations.Mocked
 * 
 * @author segg3r
 *
 */
public class SpringContextListener implements TestClassContextListener {

	private final Map<Object, GenericApplicationContext> applicationContexts;
	private final Map<Object, Integer> invokedMethods;
	private final MockUtil mockUtil;

	public SpringContextListener() {
		this.applicationContexts = new ConcurrentHashMap<>();
		this.mockUtil = new MockUtil();
		this.invokedMethods = new ConcurrentHashMap<>();
	}

	@Override
	public void onBeforeClass(ITestClass testClass, IMethodInstance method) {
		try {
			Object[] suites = testClass.getInstances(true);
			for (Object suite : suites) {
				if (applicationContexts.get(suite) != null) {
					continue;
				}
				
				ApplicationContextInitializationResult applicationContextInitializationResult
						= new SuiteApplicationContextBuilder(suite).build();
				GenericApplicationContext applicationContext = applicationContextInitializationResult.getApplicationContext();
				List<Object> autowiringCandidates = applicationContextInitializationResult.getAutowiringCandidates();
				
				processAutowiredAnnotations(suite, applicationContext);
				for (Object autowiringCandidate : autowiringCandidates) {
					processAutowiredAnnotations(autowiringCandidate, applicationContext);
				}
				applicationContexts.put(suite, applicationContext);
				invokedMethods.put(suite, testClass.getTestMethods().length);
				
				MockitoAnnotations.initMocks(suite);
			}
		} catch (Exception e) {
			throw new SpringContextListenerException("Could not configure test suite", e);
		}
	}

	@Override
	public void onAfterTest(ITestResult testResult) {
		Object suite = testResult.getInstance();
		GenericApplicationContext applicationContext = applicationContexts.get(suite);
		if (!applicationContext.isActive()) {
			return;
		}
		
		invokedMethods.put(suite, invokedMethods.get(suite) - 1);
		
		Map<String, Object> beans = applicationContext.getBeansOfType(Object.class);
		for (Object bean : beans.values()) {
			if (mockUtil.isMock(bean) || mockUtil.isSpy(bean)) {
				Mockito.reset(bean);
			}
		}
	}

	@Override
	public void onAfterClass(ITestClass testClass, IMethodInstance method) {
		for (Object suite : applicationContexts.keySet()) {
			if (invokedMethods.get(suite) == 0) {
				GenericApplicationContext applicationContext = applicationContexts.get(suite);
				if (applicationContext.isActive()) {
					applicationContext.close();
				}
			}
		}
	}

	private void processAutowiredAnnotations(Object suite,
			ApplicationContext applicationContext) {
		AutowireCapableBeanFactory autowiringBeanFactory = applicationContext
				.getAutowireCapableBeanFactory();
		autowiringBeanFactory.autowireBean(suite);
	}

}
