package by.segg3r.testng.util.spring;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doReturn;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.assertNotNull;

import org.mockito.internal.util.MockUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import by.segg3r.testng.util.spring.TestApplicationContextConfiguration.RealService;
import by.segg3r.testng.util.spring.TestApplicationContextConfiguration.Service;
import by.segg3r.testng.util.spring.TestApplicationContextConfiguration.SpiedService;

@Listeners(SpringContextListener.class)
@ContextConfiguration(
		configClasses = TestApplicationContextConfiguration.class,
		realObjects = RealService.class,
		spies = SpiedService.class)
public class SpringContextListenerTest {

	private MockUtil mockUtil = new MockUtil();

	private Object object1 = new Object();
	private Object object2 = new Object();
	
	@Autowired
	private Service service;
	
	@Test(description = "should inject values from application context")
	public void testInjection() {
		assertNotNull(service);
		
		assertNotNull(service.dao);
		assertFalse(mockUtil.isMock(service.dao));
		
		assertNotNull(service.mockedService);
		assertTrue(mockUtil.isMock(service.mockedService));
		
		assertNotNull(service.realService);
		assertFalse(mockUtil.isMock(service.realService));
		
		assertNotNull(service.spiedService);
		assertTrue(mockUtil.isSpy(service.spiedService));
	}
	
	@Test(description = "intermediate test to setup mock")
	public void intermediateMockSetup() {
		when(service.mockedService.get()).thenReturn(object1);
		doReturn(object1).when(service.spiedService).get();
	}
	
	@Test(description = "should not throw on mock instantiation, because was reseted")
	public void testResetSuite() {
		when(service.mockedService.get()).thenReturn(object2);
		assertEquals(service.mockedService.get(), object2);
		
		doReturn(object2).when(service.spiedService).get();
		assertEquals(service.spiedService.get(), object2);
	}
	
}
