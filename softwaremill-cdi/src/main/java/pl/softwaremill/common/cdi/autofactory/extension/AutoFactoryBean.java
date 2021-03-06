package pl.softwaremill.common.cdi.autofactory.extension;

import org.jboss.weld.literal.DefaultLiteral;

import javax.enterprise.context.Dependent;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.InjectionPoint;
import java.lang.annotation.Annotation;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Set;

/**
 * @author Adam Warski (adam at warski dot org)
 */
public class AutoFactoryBean<T> implements Bean<T> {
    private final BeanManager beanManager;
    private final Class<?> factoryClass;
    private final CreatedTypeData createdTypeData;

    private final Class<?>[] factoryClassInArray;

    public AutoFactoryBean(BeanManager beanManager, Class<?> factoryClass,
                           CreatedTypeData<T> createdTypeData) {
        this.beanManager = beanManager;
        this.factoryClass = factoryClass;
        this.createdTypeData = createdTypeData;

        this.factoryClassInArray = new Class<?>[] { this.factoryClass };
    }

    @Override
    public Set<Type> getTypes() {
        return Collections.<Type>singleton(factoryClass);
    }

    @SuppressWarnings({"unchecked"})
    @Override
    public T create(CreationalContext<T> creationalContext) {
        return (T) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                factoryClassInArray,
                new FactoryInvocationHandler(beanManager, this, createdTypeData, creationalContext));
    }

    @Override
    public void destroy(Object instance, CreationalContext creationalContext) { }

    @Override
    public Set<Annotation> getQualifiers() {
        return Collections.<Annotation>singleton(DefaultLiteral.INSTANCE);
    }

    @Override
    public Class<? extends Annotation> getScope() {
        return Dependent.class;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public Set<Class<? extends Annotation>> getStereotypes() {
        return Collections.emptySet();
    }

    @Override
    public Class<?> getBeanClass() {
        return factoryClass;
    }

    @Override
    public boolean isAlternative() {
        return false;
    }

    @Override
    public boolean isNullable() {
        return false;
    }

    @Override
    public Set<InjectionPoint> getInjectionPoints() {
        return Collections.emptySet();
    }
}
