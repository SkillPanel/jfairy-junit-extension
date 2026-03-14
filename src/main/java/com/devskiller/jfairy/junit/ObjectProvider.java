package com.devskiller.jfairy.junit;

import static org.junit.platform.commons.support.AnnotationSupport.findAnnotation;

import com.devskiller.jfairy.Bootstrap.Builder;
import com.devskiller.jfairy.Fairy;
import java.lang.reflect.AnnotatedElement;
import java.util.Locale;

abstract class ObjectProvider {

    abstract boolean supports(Class<?> targetType);

    final boolean isAssignableFrom(Class<?> targetType, Class<?>... supportedClasses) {
        for (Class<?> supportedClass : supportedClasses) {
            if (targetType.isAssignableFrom(supportedClass)) {
                return true;
            }
        }
        return false;
    }

    abstract Object createFor(AnnotatedElement annotatedElement, Class<?> targetType, Fairy fairy);

    final Object createFor(AnnotatedElement annotatedElement, Class<?> targetType) {
        Fairy fairy = buildFairy(annotatedElement);
        return createFor(annotatedElement, targetType, fairy);
    }

    private Fairy buildFairy(AnnotatedElement annotatedElement) {
        Faked faked = findAnnotation(annotatedElement, Faked.class).orElseThrow();
        Builder builder = Fairy.builder();

        String locale = faked.locale();
        if (!locale.isEmpty()) {
            builder.withLocale(Locale.forLanguageTag(locale));
        }

        int seed = faked.seed();
        if (seed != -1) {
            builder.withRandomSeed(seed);
        }

        return builder.build();
    }

}
