package com.devskiller.jfairy.junit;

import static com.devskiller.jfairy.producer.person.Person.Sex.MALE;
import static org.assertj.core.api.Assertions.assertThat;

import com.devskiller.jfairy.producer.company.Company;
import com.devskiller.jfairy.producer.person.Address;
import com.devskiller.jfairy.producer.person.Person;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(FairyExtension.class)
class ExampleTests {

    @Faked
    @IntegerWith(min = 50, max = 100)
    private int randomInt;

    @Faked
    @PersonWith(sex = MALE, minAge = 13, maxAge = 19)
    private Person maleTeenager;

    @Test
    void testRandomInteger() {
        assertThat(randomInt).isBetween(50, 100);
    }

    @Test
    void testMaleTeenager() {
        assertThat(maleTeenager.getAge()).isBetween(13, 19);
    }

    @Test
    void testAsParameter(@Faked int x) {
        assertThat(x).isGreaterThan(0);
    }

    @Test
    void testPerson(@Faked Person person) {
        assertThat(person.getFullName()).isNotEmpty();
    }

    @Test
    void testCompany(@Faked Company company) {
        assertThat(company.getName()).isNotEmpty();
    }

    @Test
    void testAddress(@Faked Address address) {
        assertThat(address.getCity()).isNotEmpty();
    }

    @Test
    void testWithLocale(@Faked(locale = "pl") Person person) {
        assertThat(person.getFirstName()).isNotEmpty();
    }
}
