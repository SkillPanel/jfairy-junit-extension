package com.devskiller.jfairy.junit;

import static org.junit.platform.commons.support.AnnotationSupport.findAnnotation;

import com.devskiller.jfairy.Bootstrap.Builder;
import com.devskiller.jfairy.Fairy;
import java.lang.reflect.AnnotatedElement;
import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;

abstract class ObjectProvider {

    private static final ConcurrentHashMap<String, Fairy> fairyCache = new ConcurrentHashMap<>();

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
        String locale = faked.locale();
        int seed = faked.seed();
        String cacheKey = locale + ":" + seed;

        return fairyCache.computeIfAbsent(cacheKey, key -> {
            Builder builder = Fairy.builder();

            if (!locale.isEmpty()) {
                builder.withLocale(Locale.forLanguageTag(locale));
            }

            if (seed != Faked.NO_SEED) {
                builder.withRandomSeed(seed);
            }

            return builder.build();
        });
    }

}
