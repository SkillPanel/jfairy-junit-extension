package com.devskiller.jfairy.junit;

import com.devskiller.jfairy.Fairy;
import com.devskiller.jfairy.producer.person.Address;
import java.lang.reflect.AnnotatedElement;

class AddressProvider extends ObjectProvider {

    @Override
    boolean supports(Class<?> targetType) {
        return isAssignableFrom(targetType, Address.class);
    }

    @Override
    Object createFor(AnnotatedElement annotatedElement, Class<?> targetType, Fairy fairy) {
        return fairy.person().getAddress();
    }

}
