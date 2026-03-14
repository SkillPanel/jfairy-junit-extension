package com.devskiller.jfairy.junit;

import static org.junit.jupiter.api.Assertions.assertNotNull;

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
            assertNotNull(b);
        }
    }

    @Nested
    class IntegerTests {
        @Faked
        private Integer i;

        @Test
        void integerField() {
            assertNotNull(i);
        }
    }

    @Nested
    class StringTests {
        @Faked
        private String s;

        @Test
        void stringField() {
            assertNotNull(s);
        }
    }

    @Nested
    class EnumTests {
        @Faked
        private Month month;

        @Test
        void enumField() {
            assertNotNull(month);
        }
    }

    @Nested
    class PersonTests {
        @Faked
        private Person person;

        @Test
        void personField() {
            assertNotNull(person);
        }
    }

    @Nested
    class CompanyTests {
        @Faked
        private Company company;

        @Test
        void companyField() {
            assertNotNull(company);
        }
    }

    @Nested
    class AddressTests {
        @Faked
        private Address address;

        @Test
        void addressField() {
            assertNotNull(address);
        }
    }

    @Nested
    class FairyTests {
        @Faked
        private Fairy fairy;

        @Test
        void fairyField() {
            assertNotNull(fairy);
        }
    }
}
