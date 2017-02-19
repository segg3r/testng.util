package by.segg3r.testng.util.spring.annotations;

import by.segg3r.testng.util.spring.SpringContextContextListener;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * If suite field is annotated with this annotation and suite class is annotated with {@link SpringContextContextListener @SpringListener},
 * this field will be injected with mocked instance of field declared class. Created bean will also be part of ApplicationContext.
 *
 * Supports Spring {@link org.springframework.beans.factory.annotation.Qualifier @Qualifier} annotation.
 *
 * @author segg3r
 *
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Mocked {

}
