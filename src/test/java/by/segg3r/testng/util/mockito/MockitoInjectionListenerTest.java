package by.segg3r.testng.util.mockito;

import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

import org.mockito.Mock;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Listeners(MockitoInjectingContextListener.class)
public class MockitoInjectionListenerTest {

	@Mock
	private Object object1;
	@Mock
	private Object object2;
	@Mock
	private Service service;
	
	@Test(description = "mocked fields should be initialized")
	public void testInitialization() {
		assertNotNull(service);
	}
	
	@Test(description = "intermediate test to setup mock")
	public void intermediateMockSetup() {
		when(service.get()).thenReturn(object1);
	}
	
	@Test(description = "should not throw on mock instantiation, because was reseted")
	public void testResetSuite() {
		when(service.get()).thenReturn(object2);
		assertEquals(service.get(), object2);
	}
	
	private static interface Service {
		
		Object get();
		
	}
	
}
