package com.devskiller.jfairy.junit;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(FairyExtension.class)
class StringParameterTests {

    @Test
    void stringParameter(@Faked String value) {
        assertThat(value).isNotEmpty();
    }

    @Test
    void stringWithMaxLength(@Faked @StringWith(maxLength = 20) String value) {
        assertThat(value).hasSizeLessThanOrEqualTo(20);
    }

    @Test
    void stringWord(@Faked @StringWith(type = StringWith.StringType.WORD) String value) {
        assertThat(value).isNotEmpty();
    }
}
