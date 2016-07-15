package by.segg3r.testng.util.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestApplicationContextConfiguration {

	@Bean
	public Service service() {
		return new Service();
	}
	
	@Bean Dao dao() {
		return new Dao();
	}
	
	public static final class Service {
	
		@Autowired
		protected Dao dao;
		@Autowired
		protected MockedService mockedService;
		@Autowired
		protected RealService realService;
		@Autowired
		protected SpiedService spiedService;
		
	}
	
	public static final class Dao {
	}
	
	public static class MockedService {
		
		public Object get() {
			return new Object();
		}
		
	}
	
	public static class RealService {
	}

	public static class SpiedService {
		
		public Object get() {
			return new Object();
		}
		
	}
	
}
