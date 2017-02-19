package by.segg3r.testng.util.spring.extension;

import by.segg3r.testng.util.spring.ContextConfiguration;

@ContextConfiguration(
		configClasses = InterfaceConfiguration.class,
		spies = InterfaceConfiguration.InterfaceSpiedService.class
)
public interface SpringContextListenerInterface {
}
