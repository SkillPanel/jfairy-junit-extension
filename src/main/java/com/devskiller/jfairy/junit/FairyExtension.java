package com.devskiller.jfairy.junit;

import static org.junit.platform.commons.support.AnnotationSupport.isAnnotated;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Parameter;
import java.util.List;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.jupiter.api.extension.TestInstancePostProcessor;

public class FairyExtension implements ParameterResolver, TestInstancePostProcessor {

    private static final List<ObjectProvider> providers = List.of(new BooleanProvider(), new IntegerProvider(), new StringProvider());

    @Override
    public boolean supportsParameter(ParameterContext parameterContext,
            ExtensionContext extensionContext) {
        return isAnnotated(parameterContext.getParameter(), Faked.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext,
            ExtensionContext extensionContext) {
        Parameter parameter = parameterContext.getParameter();
        return resolve(parameter, parameter.getType());
    }

    @Override
    public void postProcessTestInstance(Object testInstance, ExtensionContext context)
            throws ReflectiveOperationException {
        for (Field field : testInstance.getClass().getDeclaredFields()) {
            if (isAnnotated(field, Faked.class)) {
                Object value = resolve(field, field.getType());
                field.setAccessible(true);
                field.set(testInstance, value);
            }
        }
    }

    private Object resolve(AnnotatedElement annotatedElement, Class<?> targetType) {
        for (ObjectProvider provider : providers) {
            if (provider.supports(targetType)) {
                return provider.createFor(annotatedElement, targetType);
            }
        }
        throw new IllegalArgumentException("No provider found for type " + targetType);
    }

}
