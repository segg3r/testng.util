package by.segg3r.testng.util.mockito;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import com.google.common.collect.Lists;

public class MockitoUtils {

	public static void prepareSuite(Object suite) {
		MockitoAnnotations.initMocks(suite);
		injectFromTest(suite, suite);
	}

	public static void resetSuite(Object suite) {
		List<Object> resetableObjects = getMockedObjects(suite);
		Object[] resetableObjectsArray = resetableObjects
				.toArray(new Object[resetableObjects.size()]);
		Mockito.reset(resetableObjectsArray);
	}

	public static List<Object> getMockedObjects(Object test) {
		try {
			return getObjectsFromFields(test, field -> {
				return isMock(field) || isSpy(field);
			});
		} catch (Exception e) {
			throw new RuntimeException("Could not get mocked fields from "
					+ test, e);
		}
	}

	public static void inject(Object target, Object... dependencies) {
		try {
			injectDependenciesIntoFields(target, dependencies);
			processInjectMocksAnnotations(target, dependencies);
		} catch (Exception e) {
			throw new RuntimeException(
					"Could not inject dependencies under target object "
							+ target, e);
		}
	}

	private static void injectDependenciesIntoFields(Object target,
			Object... dependencies) throws Exception {
		List<Field> fields = getObjectFields(target);

		for (Object dependency : dependencies) {
			Field field = getMatchingField(fields, dependency);

			if (field != null) {
				setFieldValue(field, target, dependency);
				if (isSpy(field) || !isMock(field)) {
					inject(dependency, dependencies);
				}
			}
		}
	}

	private static void processInjectMocksAnnotations(Object target,
			Object[] dependencies) throws Exception {
		List<Field> fields = getObjectFields(target);

		for (Field field : fields) {
			if (isInjectMocks(field)) {
				Object fieldValue = getFieldValue(field, target);
				inject(fieldValue, dependencies);
			}
		}
	}

	public static void injectFromTest(Object target, Object test) {
		List<Object> mocks = getMockedObjects(test);
		Object[] mocksArray = mocks.toArray(new Object[mocks.size()]);
		inject(target, mocksArray);
	}

	private static Field getMatchingField(List<Field> fields, Object dependency) {
		Class<?> dependencyClass = dependency.getClass();

		for (Field field : fields) {
			Class<?> fieldClass = field.getType();
			if (fieldClass.isAssignableFrom(dependencyClass)) {
				return field;
			}
		}

		return null;
	}

	private static List<Object> getObjectsFromFields(Object object,
			Predicate<Field> fieldFilter) throws Exception {
		List<Object> objects = Lists.newArrayList();

		for (Field field : getObjectFields(object)) {
			if (fieldFilter.test(field)) {
				objects.add(getFieldValue(field, object));
			}
		}

		return objects;
	}

	private static List<Field> getObjectFields(Object object) {
		Class<?> clazz = object.getClass();

		List<Field> fields = Lists.newArrayList();
		while (clazz != Object.class) {
			fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
			clazz = clazz.getSuperclass();
		}

		return fields;
	}

	private static Object getFieldValue(Field field, Object object)
			throws Exception {
		boolean accessible = field.isAccessible();
		field.setAccessible(true);
		Object result = field.get(object);
		field.setAccessible(accessible);

		return result;
	}

	private static void setFieldValue(Field field, Object target,
			Object dependency) throws Exception {
		boolean accessible = field.isAccessible();
		field.setAccessible(true);
		field.set(target, dependency);
		field.setAccessible(accessible);
	}

	private static boolean isMock(Field field) {
		return field.getAnnotation(Mock.class) != null;
	}

	private static boolean isSpy(Field field) {
		return field.getAnnotation(Spy.class) != null;
	}

	private static boolean isInjectMocks(Field field) {
		return field.getAnnotation(InjectMocks.class) != null;
	}

}