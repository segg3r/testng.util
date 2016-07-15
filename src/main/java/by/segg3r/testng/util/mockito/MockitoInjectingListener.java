package by.segg3r.testng.util.mockito;

import org.testng.IMethodInstance;
import org.testng.ITestClass;
import org.testng.ITestResult;

import by.segg3r.testng.util.TestClassListener;

/**
 * 
 * Listener, which does basic mockito annotation processing for test suite, which it was specified for.
 * It initializes mocks in fields annotated with {@link org.mockito.Mock @Mock}, {@link org.mockito.Spy @Spy} before all tests, and recursively injects them
 * in each real object, which was instantiated under {@link org.mockito.InjectMocks @InjectMocks} field. Also resets @Mock and @Spy objects
 * after each test. 
 * 
 * @author segg3r
 *
 */
public class MockitoInjectingListener implements TestClassListener {

	@Override
	public void onBeforeClass(ITestClass testClass, IMethodInstance method) {
		Object[] suites = testClass.getInstances(true);
		for (Object suite : suites) {
			MockitoUtils.prepareSuite(suite);
		}
	}

	@Override
	public void onAfterTest(ITestResult testResult) {
		Object suite = testResult.getInstance();
		MockitoUtils.resetSuite(suite);
	}

}
