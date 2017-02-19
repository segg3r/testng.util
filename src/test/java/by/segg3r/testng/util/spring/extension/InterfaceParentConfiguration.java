package by.segg3r.testng.util.spring.extension;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InterfaceParentConfiguration {

	public static class InterfaceParentService {
	}

	@Bean
	public InterfaceParentService interfaceParentService() {
		return new InterfaceParentService();
	}

}
