package com.devskiller.jfairy.junit;

import com.devskiller.jfairy.Fairy;
import java.lang.reflect.AnnotatedElement;

class BooleanProvider extends ObjectProvider {

    @Override
    boolean supports(Class<?> targetType) {
        return isAssignableFrom(targetType, Boolean.class, Boolean.TYPE);
    }

    @Override
    Object createFor(AnnotatedElement annotatedElement, Class<?> targetType, Fairy fairy) {
        return fairy.baseProducer().trueOrFalse();
    }

}
