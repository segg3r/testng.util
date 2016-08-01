package by.segg3r.testng.util.spring;

import static org.testng.Assert.*;
import org.mockito.internal.util.MockUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import by.segg3r.testng.util.spring.annotations.Mocked;
import by.segg3r.testng.util.spring.annotations.Real;
import by.segg3r.testng.util.spring.annotations.Spied;

@Listeners(SpringContextListener.class)
public class SpringContextListenerInjectionTest {

	@Real
	private Service service;
	@Spied
	private SpiedDao spiedDao;
	@Mocked
	private MockedDao mockedDao;
	
	@Test(description = "should correctly configure ApplicationContext from custom directives and inject fields")
	public void testApplicationContextConfigurationAndInjection() {
		MockUtil mockUtil = new MockUtil();
		
		assertTrue(service != null && !mockUtil.isMock(service) && !mockUtil.isSpy(service));
		assertTrue(mockUtil.isSpy(spiedDao));
		assertTrue(mockUtil.isMock(mockedDao));
		
		assertEquals(spiedDao, service.spiedDao);
		assertEquals(mockedDao, service.mockedDao);
	}
	
	public static class Service {
		@Autowired
		private SpiedDao spiedDao;
		@Autowired
		private MockedDao mockedDao;
	}
	
	public static class SpiedDao {
	}

	public static class MockedDao {
	}
	
}
