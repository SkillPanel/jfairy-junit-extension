package com.devskiller.jfairy.junit;

import static com.devskiller.jfairy.producer.person.Person.Sex.MALE;
import static org.assertj.core.api.Assertions.assertThat;

import com.devskiller.jfairy.producer.person.Person;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(FairyExtension.class)
class PersonParameterTests {

    @Test
    void person(@Faked Person person) {
        assertThat(person).isNotNull();
    }

    @Test
    void personWithLocaleAndSeed(@Faked(locale = "de", seed = 1337) Person person) {
        assertThat(person.getFirstName()).isNotEmpty();
    }

    @Test
    void adult(@Faked @PersonWith(minAge = 18) Person person) {
        assertThat(person.getAge()).isGreaterThanOrEqualTo(18);
    }

    @Test
    void teenager(@Faked @PersonWith(minAge = 13, maxAge = 19) Person person) {
        assertThat(person.getAge()).isBetween(13, 19);
    }

    @Test
    void male(@Faked @PersonWith(sex = MALE) Person person) {
        assertThat(person.getSex()).isEqualTo(MALE);
    }
}
