package by.segg3r.testng.util;

import com.google.common.collect.Lists;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestNGListener;
import org.testng.ITestResult;
import org.testng.annotations.Listeners;

import java.util.Optional;

public interface SuiteTestListener extends TestContextListener {

	default void onBeforeTest(ITestResult testResult) {
		getSuite(this, testResult)
				.ifPresent(suite -> onBeforeSuiteTest(suite, testResult));
	}
	
	default void onAfterTest(ITestResult testResult) {
		getSuite(this, testResult)
				.ifPresent(suite -> onAfterSuiteTest(suite, testResult));
	}

	default void onBeforeSuiteTest(Object suite, ITestResult testResult) { return; }

	default void onAfterSuiteTest(Object suite, ITestResult testResult) { return; }

	static Optional<Object> getSuite(SuiteTestListener _this, ITestResult testResult) {
		Object suite = testResult.getInstance();

		Listeners listeners = suite.getClass().getAnnotation(Listeners.class);
		return listeners != null && Lists.newArrayList(listeners.value()).contains(_this.getClass())
				? Optional.of(suite)
				: Optional.empty();
	}

}
