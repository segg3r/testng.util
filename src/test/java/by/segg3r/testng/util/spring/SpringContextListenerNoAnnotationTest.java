package by.segg3r.testng.util.spring;

import static org.testng.Assert.*;

import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Listeners(SpringContextListener.class)
public class SpringContextListenerNoAnnotationTest {

	@Test(description = "should not fail if @ContextConfiguration annotation is not present")
	public void testSuccessWithoutAnnotation() {
		assertTrue(true);
	}
	
}
