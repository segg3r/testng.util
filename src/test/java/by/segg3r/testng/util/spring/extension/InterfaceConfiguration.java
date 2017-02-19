package by.segg3r.testng.util.spring.extension;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InterfaceConfiguration {

	public static class InterfaceService {
	}

	public static class InterfaceSpiedService {
	}

	public static class InterfaceMockedService {
	}

	@Bean
	public InterfaceService interfaceService() {
		return new InterfaceService();
	}

}
