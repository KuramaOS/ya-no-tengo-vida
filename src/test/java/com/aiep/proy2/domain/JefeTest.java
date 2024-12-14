package com.aiep.proy2.domain;

import static com.aiep.proy2.domain.JefeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.aiep.proy2.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class JefeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Jefe.class);
        Jefe jefe1 = getJefeSample1();
        Jefe jefe2 = new Jefe();
        assertThat(jefe1).isNotEqualTo(jefe2);

        jefe2.setId(jefe1.getId());
        assertThat(jefe1).isEqualTo(jefe2);

        jefe2 = getJefeSample2();
        assertThat(jefe1).isNotEqualTo(jefe2);
    }
}
