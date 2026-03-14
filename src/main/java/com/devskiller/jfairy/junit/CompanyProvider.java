package com.devskiller.jfairy.junit;

import com.devskiller.jfairy.Fairy;
import com.devskiller.jfairy.producer.company.Company;
import java.lang.reflect.AnnotatedElement;

class CompanyProvider extends ObjectProvider {

    @Override
    boolean supports(Class<?> targetType) {
        return isAssignableFrom(targetType, Company.class);
    }

    @Override
    Object createFor(AnnotatedElement annotatedElement, Class<?> targetType, Fairy fairy) {
        return fairy.company();
    }

}
