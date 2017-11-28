package by.segg3r.testng.util.spring;

import by.segg3r.testng.util.TestClassContextListener;
import by.segg3r.testng.util.spring.exception.SpringContextListenerException;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.testng.ITestClass;
import org.testng.ITestContext;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.annotations.Listeners;

import java.util.*;

import static java.util.stream.Collectors.toSet;
import static org.mockito.Mockito.reset;
import static org.mockito.internal.util.MockUtil.isMock;
import static org.mockito.internal.util.MockUtil.isSpy;

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

	private GenericApplicationContext applicationContext;

	public SpringContextListener() {
	}

	@Override
	public void onStart(ITestContext testContext) {
		try {
			Collection<Object> suites = Arrays.stream(testContext.getAllTestMethods())
					.map(ITestNGMethod::getTestClass)
					.distinct()
					.filter(this::containsListener)
					.flatMap(testClass -> Arrays.stream(testClass.getInstances(true)))
					.collect(toSet());

			ApplicationContextInitializationResult applicationContextInitializationResult
					= new SuiteApplicationContextBuilder(suites).build();
			applicationContext = applicationContextInitializationResult.getApplicationContext();
			Set<Object> autowiringCandidates = new HashSet<>(applicationContextInitializationResult
					.getAutowiringCandidates());

			suites.forEach(suite -> {
				processAutowiredAnnotations(suite, applicationContext);
				MockitoAnnotations.initMocks(suite);
			});
			for (Object autowiringCandidate : autowiringCandidates) {
				processAutowiredAnnotations(autowiringCandidate, applicationContext);
			}
		} catch (Exception e) {
			throw new SpringContextListenerException("Could not configure test suite", e);
		}
	}

	@Override
	public void onFinish(ITestContext testContext) {
		if (applicationContext != null) {
			applicationContext.close();
		}
	}

	private boolean containsListener(ITestClass testClass) {
		Class<?> realClass = testClass.getRealClass();
		Listeners listeners = realClass.getAnnotation(Listeners.class);
		return listeners != null
				&& Arrays.asList(listeners.value()).contains(SpringContextListener.class);

	}

	@Override
	public void onAfterTest(ITestResult testResult) {
		resetMockedBeans();
	}

	private void resetMockedBeans() {
		Map<String, Object> beans = applicationContext.getBeansOfType(Object.class);
		for (Object bean : beans.values()) {
			if (isMock(bean) || isSpy(bean)) {
				reset(bean);
			}
		}
	}

	private void processAutowiredAnnotations(Object bean, ApplicationContext applicationContext) {
		AutowireCapableBeanFactory autowiringBeanFactory = applicationContext
				.getAutowireCapableBeanFactory();
		autowiringBeanFactory.autowireBean(bean);
	}

}
