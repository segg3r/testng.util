package by.segg3r.testng.util.spring.extension;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ParentConfiguration {

	public static class ParentService {
	}

	public static class ParentSpiedService {
	}

	public static class ParentMockedService {
	}

	@Bean
	public ParentService parentService() {
		return new ParentService();
	}

}
