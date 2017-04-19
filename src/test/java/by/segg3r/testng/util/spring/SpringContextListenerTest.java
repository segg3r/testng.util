package by.segg3r.testng.util.spring;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doReturn;
import static org.mockito.internal.util.MockUtil.isMock;
import static org.mockito.internal.util.MockUtil.isSpy;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.assertNotNull;

import by.segg3r.testng.util.spring.extension.*;
import org.mockito.Mock;
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
public class SpringContextListenerTest
		extends SpringContextListenerParent
		implements SpringContextListenerInterface {

	private Object object1 = new Object();
	private Object object2 = new Object();
	
	@Autowired
	private Service service;
	@Mock
	private Object mockedObject;

	@Autowired
	private ParentConfiguration.ParentService parentService;
	@Autowired
	private ParentConfiguration.ParentSpiedService parentSpiedService;

	@Autowired
	private InterfaceConfiguration.InterfaceService interfaceService;
	@Autowired
	private InterfaceConfiguration.InterfaceSpiedService interfaceSpiedService;

	@Autowired
	private InterfaceParentConfiguration.InterfaceParentService interfaceParentService;

	@Test(description = "should trigger initAnnotations")
	public void testMockitoAnnotations() {
		assertTrue(isMock(mockedObject));
	}
	
	@Test(description = "should inject values from application context")
	public void testInjection() {
		assertNotNull(service);
		
		assertNotNull(service.dao);
		assertFalse(isMock(service.dao));
		
		assertNotNull(service.mockedService);
		assertTrue(isMock(service.mockedService));
		
		assertNotNull(service.realService);
		assertFalse(isMock(service.realService));
		
		assertNotNull(service.spiedService);
		assertTrue(isSpy(service.spiedService));
	}

	@Test(description = "should inject values from parent ContextConfiguration")
	public void testParentConfigurationInjection() {
		assertNotNull(parentService);
		assertFalse(isSpy(parentService));
		assertFalse(isMock(parentService));

		assertTrue(isSpy(parentSpiedService));
	}

	@Test(description = "should inject values from implemented interfaces")
	public void testInterfaceConfigurationInjection() {
		assertNotNull(interfaceService);
		assertFalse(isSpy(interfaceService));
		assertFalse(isMock(interfaceService));

		assertTrue(isSpy(interfaceSpiedService));
	}

	@Test(description = "should inject values from implemented interface hierarchy")
	public void testInterfaceHierarchyConfiguraitonInjection() {
		assertNotNull(interfaceParentService);
		assertFalse(isSpy(interfaceParentService));
		assertFalse(isMock(interfaceParentService));
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
