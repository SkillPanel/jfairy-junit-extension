package com.devskiller.jfairy.junit;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(FairyExtension.class)
class IntegerParameterTests {

    @Test
    void integerParameter(@Faked int value) {
        assertThat(value).isGreaterThan(0);
    }

    @Test
    void integerWithRange(@Faked @IntegerWith(min = 10, max = 20) int value) {
        assertThat(value).isBetween(10, 20);
    }
}
