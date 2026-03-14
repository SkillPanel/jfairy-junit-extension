package com.devskiller.jfairy.junit;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(FairyExtension.class)
class BooleanParameterTests {

    @Test
    void booleanParameter(@Faked Boolean value) {
        assertNotNull(value);
    }
}
