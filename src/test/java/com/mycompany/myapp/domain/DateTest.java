package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DateTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Date.class);
        Date date1 = new Date();
        date1.setId(1L);
        Date date2 = new Date();
        date2.setId(date1.getId());
        assertThat(date1).isEqualTo(date2);
        date2.setId(2L);
        assertThat(date1).isNotEqualTo(date2);
        date1.setId(null);
        assertThat(date1).isNotEqualTo(date2);
    }
}
