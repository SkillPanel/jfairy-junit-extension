package com.devskiller.jfairy.junit;

import static org.assertj.core.api.Assertions.assertThat;

import com.devskiller.jfairy.Fairy;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(FairyExtension.class)
class FairyParameterTests {

    @Test
    void fairyInstance(@Faked Fairy fairy) {
        assertThat(fairy).isNotNull();
        assertThat(fairy.person()).isNotNull();
    }

    @Test
    void fairyWithLocale(@Faked(locale = "pl") Fairy fairy) {
        assertThat(fairy.person().getFirstName()).isNotEmpty();
    }
}
