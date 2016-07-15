package by.segg3r.testng.util;

import org.testng.IClassListener;
import org.testng.IMethodInstance;
import org.testng.ITestClass;

public interface TestClassListener extends IClassListener, TestListener {

	@Override
	default void onBeforeClass(ITestClass testClass, IMethodInstance method) {
		return;
	}

	@Override
	default void onAfterClass(ITestClass testClass, IMethodInstance method) {
		return;
	}
	
}
