package com.aiep.proy2.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class DepartamentoJefeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static DepartamentoJefe getDepartamentoJefeSample1() {
        return new DepartamentoJefe().id(1L).idDepartamento("idDepartamento1").idJefe("idJefe1");
    }

    public static DepartamentoJefe getDepartamentoJefeSample2() {
        return new DepartamentoJefe().id(2L).idDepartamento("idDepartamento2").idJefe("idJefe2");
    }

    public static DepartamentoJefe getDepartamentoJefeRandomSampleGenerator() {
        return new DepartamentoJefe()
            .id(longCount.incrementAndGet())
            .idDepartamento(UUID.randomUUID().toString())
            .idJefe(UUID.randomUUID().toString());
    }
}
