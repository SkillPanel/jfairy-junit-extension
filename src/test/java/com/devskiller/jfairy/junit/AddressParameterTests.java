package com.devskiller.jfairy.junit;

import static org.assertj.core.api.Assertions.assertThat;

import com.devskiller.jfairy.producer.person.Address;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(FairyExtension.class)
class AddressParameterTests {

    @Test
    void address(@Faked Address address) {
        assertThat(address).isNotNull();
        assertThat(address.getCity()).isNotEmpty();
    }

    @Test
    void addressWithLocale(@Faked(locale = "pl") Address address) {
        assertThat(address.getCity()).isNotEmpty();
    }
}
