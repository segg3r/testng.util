package by.segg3r.testng.util.spring;

import java.beans.PropertyDescriptor;
import java.util.Optional;
import java.util.Set;

import org.mockito.Mockito;
import org.mockito.internal.util.MockUtil;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.TypeConverter;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.beans.factory.config.DependencyDescriptor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

public class MockAutowiredAnnotationBeanPostProcessor extends AutowiredAnnotationBeanPostProcessor {

	private static final MockUtil MOCK_UTIL = new MockUtil();
	
	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		BeanFactory mockProvidingFactory = new MockProvidingListableBeanFactory(beanFactory);
		super.setBeanFactory(mockProvidingFactory);
	}
	
	@Override
	public PropertyValues postProcessPropertyValues(PropertyValues pvs,
			PropertyDescriptor[] pds, Object bean, String beanName)
			throws BeansException {
		return isIgnoringAutowiring(bean)
				? pvs
				: super.postProcessPropertyValues(pvs, pds, bean, beanName);
	}

	private boolean isIgnoringAutowiring(Object bean) {
		return MOCK_UTIL.isMock(bean);
	}

	private static final class MockProvidingListableBeanFactory extends DefaultListableBeanFactory {
		
		public MockProvidingListableBeanFactory(BeanFactory delegate) {
			super(delegate);
		}

		@Override
		public Object resolveDependency(DependencyDescriptor descriptor,
				String beanName, Set<String> autowiredBeanNames,
				TypeConverter typeConverter) throws BeansException {
			Optional<Object> delegateDependency = resolveDelegatedDependency(descriptor, beanName, autowiredBeanNames, typeConverter);
			return delegateDependency.isPresent()
					? delegateDependency.get()
					: createMock(descriptor);
		}

		private Optional<Object> resolveDelegatedDependency(
				DependencyDescriptor descriptor, String beanName,
				Set<String> autowiredBeanNames, TypeConverter typeConverter) {
			try {
				return Optional.of(super.resolveDependency(descriptor, beanName, autowiredBeanNames,
					typeConverter));
			} catch (NoSuchBeanDefinitionException e) {
				return Optional.empty();
				
			}
		}

		private Object createMock(DependencyDescriptor descriptor) {
			return Mockito.mock(descriptor.getField().getType());
		}
		
	}
	
}
