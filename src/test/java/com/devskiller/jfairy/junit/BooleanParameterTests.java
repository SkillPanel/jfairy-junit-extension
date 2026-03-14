package com.devskiller.jfairy.junit;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(FairyExtension.class)
class BooleanParameterTests {

    @Test
    void booleanParameter(@Faked Boolean value) {
        assertThat(value).isNotNull();
    }
}
