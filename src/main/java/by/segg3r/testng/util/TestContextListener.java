package by.segg3r.testng.util;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public interface TestContextListener extends ITestListener {

	default void onBeforeTest(ITestResult testResult) {
		return;
	}
	
	default void onAfterTest(ITestResult testResult) {
		return;
	}
	
	@Override
	default void onFinish(ITestContext testContext) {
		return;
	}

	@Override
	default void onStart(ITestContext testContext) {
		return;
	}
	
	@Override
	default void onTestStart(ITestResult testResult) {
		onBeforeTest(testResult);
	}

	@Override
	default void onTestFailedButWithinSuccessPercentage(ITestResult testResult) {
		onAfterTest(testResult);
	}

	@Override
	default void onTestFailure(ITestResult testResult) {
		onAfterTest(testResult);
	}

	@Override
	default void onTestSkipped(ITestResult testResult) {
		onAfterTest(testResult);
	}

	@Override
	default void onTestSuccess(ITestResult testResult) {
		onAfterTest(testResult);
	}
	
}
