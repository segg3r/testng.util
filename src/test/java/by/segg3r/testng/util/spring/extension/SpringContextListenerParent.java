package by.segg3r.testng.util.spring.extension;

import by.segg3r.testng.util.spring.ContextConfiguration;

@ContextConfiguration(
		configClasses = ParentConfiguration.class,
		spies = ParentConfiguration.ParentSpiedService.class
)
public class SpringContextListenerParent implements SpringContextListenerInterface {
}
