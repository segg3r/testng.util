package by.segg3r.testng.util.spring.extension;

import by.segg3r.testng.util.spring.annotations.Real;
import by.segg3r.testng.util.spring.annotations.Spied;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

public class SpringContextListenerInjectionParent {

	@Real
	protected ParentConfiguration.ParentService parentService;
	@Spied
	protected ParentConfiguration.ParentSpiedService parentSpiedService;
	@Mock
	protected ParentConfiguration.ParentMockedService parentMockedService;

	@Autowired
	protected ApplicationContext parentApplicationContext;

}
