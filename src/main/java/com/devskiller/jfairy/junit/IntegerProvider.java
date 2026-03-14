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
        IntegerWith config = findAnnotation(annotatedElement, IntegerWith.class).orElse(null);
        int min = config != null ? config.min() : 1;
        int max = config != null ? config.max() : Integer.MAX_VALUE;
        return fairy.baseProducer().randomBetween(min, max);
    }

}
