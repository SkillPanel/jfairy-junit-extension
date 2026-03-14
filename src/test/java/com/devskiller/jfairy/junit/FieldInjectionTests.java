package com.devskiller.jfairy.junit;

import static org.assertj.core.api.Assertions.assertThat;

import com.devskiller.jfairy.Fairy;
import com.devskiller.jfairy.producer.company.Company;
import com.devskiller.jfairy.producer.person.Address;
import com.devskiller.jfairy.producer.person.Person;
import java.time.Month;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(FairyExtension.class)
class FieldInjectionTests {

    @Nested
    class BooleanTests {
        @Faked
        private Boolean b;

        @Test
        void booleanField() {
            assertThat(b).isNotNull();
        }
    }

    @Nested
    class IntegerTests {
        @Faked
        private Integer i;

        @Test
        void integerField() {
            assertThat(i).isNotNull();
        }
    }

    @Nested
    class StringTests {
        @Faked
        private String s;

        @Test
        void stringField() {
            assertThat(s).isNotNull();
        }
    }

    @Nested
    class EnumTests {
        @Faked
        private Month month;

        @Test
        void enumField() {
            assertThat(month).isNotNull();
        }
    }

    @Nested
    class PersonTests {
        @Faked
        private Person person;

        @Test
        void personField() {
            assertThat(person).isNotNull();
        }
    }

    @Nested
    class CompanyTests {
        @Faked
        private Company company;

        @Test
        void companyField() {
            assertThat(company).isNotNull();
        }
    }

    @Nested
    class AddressTests {
        @Faked
        private Address address;

        @Test
        void addressField() {
            assertThat(address).isNotNull();
        }
    }

    @Nested
    class FairyTests {
        @Faked
        private Fairy fairy;

        @Test
        void fairyField() {
            assertThat(fairy).isNotNull();
        }
    }
}
