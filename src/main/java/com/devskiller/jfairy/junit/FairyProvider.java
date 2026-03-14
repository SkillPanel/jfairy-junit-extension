package com.devskiller.jfairy.junit;

import com.devskiller.jfairy.Fairy;
import java.lang.reflect.AnnotatedElement;

class FairyProvider extends ObjectProvider {

    @Override
    boolean supports(Class<?> targetType) {
        return isAssignableFrom(targetType, Fairy.class);
    }

    @Override
    Object createFor(AnnotatedElement annotatedElement, Class<?> targetType, Fairy fairy) {
        return fairy;
    }

}
