package com.devskiller.jfairy.junit;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Month;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(FairyExtension.class)
class EnumParameterTests {

    @Test
    void enumParameter(@Faked Month month) {
        assertThat(month).isNotNull();
    }
}
