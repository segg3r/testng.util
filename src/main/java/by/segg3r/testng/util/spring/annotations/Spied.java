package by.segg3r.testng.util.spring.annotations;

import java.lang.annotation.ElementType;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.beans.factory.annotation.Autowired;
/**
 * 
 * If suite field is annotated with this annotation and suite class is annotated with {@link by.segg3r.testng.util.spring.SpringContextListener @SpringListener},
 * this field will be injected with spied instance of field declared class. Created bean will also be part of ApplicationContext.
 *
 * Supports Spring {@link org.springframework.beans.factory.annotation.Qualifier @Qualifier} annotation.
 *
 * @author segg3r
 *
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Autowired
public @interface Spied {

}
