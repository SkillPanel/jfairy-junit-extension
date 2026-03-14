package com.devskiller.jfairy.junit;

import com.devskiller.jfairy.Fairy;
import java.lang.reflect.AnnotatedElement;

class EnumProvider extends ObjectProvider {

    @Override
    boolean supports(Class<?> targetType) {
        return targetType.isEnum();
    }

    @Override
    @SuppressWarnings("unchecked")
    Object createFor(AnnotatedElement annotatedElement, Class<?> targetType, Fairy fairy) {
        return fairy.baseProducer().randomElement((Class<Enum<?>>) targetType);
    }

}
