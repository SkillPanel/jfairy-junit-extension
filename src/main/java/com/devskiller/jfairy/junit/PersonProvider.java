package com.devskiller.jfairy.junit;

import static com.devskiller.jfairy.producer.person.Person.Sex.FEMALE;
import static com.devskiller.jfairy.producer.person.Person.Sex.MALE;
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
                        if (sex[0] == MALE) {
                            properties.add(PersonProperties.male());
                        } else if (sex[0] == FEMALE) {
                            properties.add(PersonProperties.female());
                        }
                    }

                    properties.add(PersonProperties.ageBetween(config.minAge(), config.maxAge()));

                    return properties.toArray(new PersonProperty[0]);
                })
                .orElseGet(() -> new PersonProperty[0]);
    }

}
