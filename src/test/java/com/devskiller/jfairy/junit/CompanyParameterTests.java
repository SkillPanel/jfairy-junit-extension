package com.devskiller.jfairy.junit;

import static org.assertj.core.api.Assertions.assertThat;

import com.devskiller.jfairy.producer.company.Company;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(FairyExtension.class)
class CompanyParameterTests {

    @Test
    void company(@Faked Company company) {
        assertThat(company).isNotNull();
        assertThat(company.getName()).isNotEmpty();
    }

    @Test
    void companyWithLocale(@Faked(locale = "pl") Company company) {
        assertThat(company.getName()).isNotEmpty();
    }
}
