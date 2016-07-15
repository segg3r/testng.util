package by.segg3r.testng.util.mockito;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.List;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.testng.annotations.Test;

public class MockitoUtilsTest {

	@Test(description = "should correctly return mocked objects from class fields")
	public void testGetMockedObjects() {
		TestedClass test = new TestedClass();
		MockitoUtils.prepareSuite(test);

		List<Object> mocks = MockitoUtils.getMockedObjects(test);
		assertEquals(mocks.size(), 2);
		assertTrue(mocks.get(0) instanceof ServiceClass);
		assertTrue(mocks.get(1) instanceof DAOClass);
	}

	@Test(description = "should correctly inject objects under target object recursively")
	public void testInject() {
		ServiceClass service = new ServiceClass();
		DAOClass dao = new DAOClass();

		TestedClass test = new TestedClass();
		MockitoUtils.inject(test, service, dao);

		assertEquals(test.getService(), service);
		assertEquals(test.getDAO(), dao);
		assertEquals(service.getDAO(), dao);
	}

	@Test(description = "should correctly inject dependencies from mockito annotated fields")
	public void testInjectFromFields() {
		ServiceClass service = new ServiceClass();
		DAOClass dao = new DAOClass();

		TestedClass test = new TestedClass();
		MockitoUtils.inject(test, service, dao);

		TestedClass mockitoInjectionTarget = new TestedClass();
		MockitoUtils.injectFromTest(mockitoInjectionTarget, test);
		assertEquals(mockitoInjectionTarget.getService(), service);
		assertEquals(mockitoInjectionTarget.getDAO(), dao);
	}
	
	@Test(description = "should correcly initialize InjectionContext test")
	public void testPrepareTest() {
		TestedClassWithInjectMocks test = new TestedClassWithInjectMocks();
		MockitoUtils.prepareSuite(test);
	
		assertTrue(test.getDAO() instanceof DAOClass);
		
		assertTrue(test.getService() instanceof ServiceClass);
		assertTrue(test.getService().getDAO() instanceof DAOClass);
		
		assertTrue(test.getTest().getService() instanceof ServiceClass);
		assertTrue(test.getTest().getService().getDAO() instanceof DAOClass);
		assertTrue(test.getTest().getDAO() instanceof DAOClass);
	}

	private static class DAOClass {
	}

	private static class ServiceClass {

		private DAOClass dao;

		public DAOClass getDAO() {
			return this.dao;
		}

	}

	private static class TestedClass {

		@Spy
		private ServiceClass service;
		@Mock
		private DAOClass dao;

		public ServiceClass getService() {
			return this.service;
		}

		public DAOClass getDAO() {
			return this.dao;
		}

	}

	private static class TestedClassWithInjectMocks {

		@Spy
		private ServiceClass service;
		@Mock
		private DAOClass dao;
		@InjectMocks
		private TestedClass test;

		public ServiceClass getService() {
			return this.service;
		}

		public DAOClass getDAO() {
			return this.dao;
		}

		public TestedClass getTest() {
			return test;
		}

	}

}