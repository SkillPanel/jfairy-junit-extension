package com.devskiller.jfairy.junit;

import static org.junit.platform.commons.support.AnnotationSupport.findAnnotation;

import com.devskiller.jfairy.Fairy;
import java.lang.reflect.AnnotatedElement;

class IntegerProvider extends ObjectProvider {

    @Override
    boolean supports(Class<?> targetType) {
        return isAssignableFrom(targetType, Integer.class, Integer.TYPE);
    }

    @Override
    Object createFor(AnnotatedElement annotatedElement, Class<?> targetType, Fairy fairy) {
        return findAnnotation(annotatedElement, IntegerWith.class)
                .map(config -> fairy.baseProducer().randomBetween(config.min(), config.max()))
                .orElseGet(() -> fairy.baseProducer().randomBetween(1, Integer.MAX_VALUE));
    }

}
