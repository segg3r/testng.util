package by.segg3r.testng.util.spring;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * Provides data for Spring context initialization, if this test class is also
 * annotated with {@link by.segg3r.testng.util.spring.SpringListener @SpringListener}.
 * <br>
 * <br>
 * All classes specified under <b>configClasses</b> field should be annotated with
 * {@link org.springframework.context.annotation.Configuration @Configuration} and will
 * be considered java configuration classes for Spring context. 
 * <br>
 * <br>
 * All classes specified under <b>realObjects</b> field will be considered singleton beans.
 * <br>
 * <b>Important: </b> all fields under all singletons, which were not provided in this
 * configuration will be populated with <i>Mockito.mock()</i> instances.
 * <br>
 * <br>
 * All classes specified under <b>spies</b> field will be considered singleton beans and
 * instantiated as Mockito spies. Specifying class here is the same as calling
 * <i>Mockito.spy(clazz)</i>. 
 * 
 * @author segg3r
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ContextConfiguration {

	public Class<?>[] configClasses() default {};

	public Class<?>[] realObjects() default {};

	public Class<?>[] spies() default {};

}
