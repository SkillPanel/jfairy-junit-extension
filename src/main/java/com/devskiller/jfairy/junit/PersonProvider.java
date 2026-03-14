package com.devskiller.jfairy.junit;

import static org.junit.platform.commons.support.AnnotationSupport.findAnnotation;

import com.devskiller.jfairy.Fairy;
import com.devskiller.jfairy.producer.person.Person;
import com.devskiller.jfairy.producer.person.Person.Sex;
import com.devskiller.jfairy.producer.person.PersonProperties;
import com.devskiller.jfairy.producer.person.PersonProperties.PersonProperty;
import java.lang.reflect.AnnotatedElement;
import java.util.ArrayList;
import java.util.List;

class PersonProvider extends ObjectProvider {

    private static final PersonProperty[] EMPTY_PROPERTIES = new PersonProperty[0];

    @Override
    boolean supports(Class<?> targetType) {
        return isAssignableFrom(targetType, Person.class);
    }

    @Override
    Object createFor(AnnotatedElement annotatedElement, Class<?> targetType, Fairy fairy) {
        PersonProperty[] properties = createProperties(annotatedElement);
        return fairy.person(properties);
    }

    private PersonProperty[] createProperties(AnnotatedElement annotatedElement) {
        return findAnnotation(annotatedElement, PersonWith.class)
                .map(config -> {
                    List<PersonProperty> properties = new ArrayList<>();

                    Sex[] sex = config.sex();
                    if (sex.length == 1) {
                        switch (sex[0]) {
                            case MALE -> properties.add(PersonProperties.male());
                            case FEMALE -> properties.add(PersonProperties.female());
                        }
                    }

                    properties.add(PersonProperties.ageBetween(config.minAge(), config.maxAge()));

                    return properties.toArray(EMPTY_PROPERTIES);
                })
                .orElse(EMPTY_PROPERTIES);
    }

}
