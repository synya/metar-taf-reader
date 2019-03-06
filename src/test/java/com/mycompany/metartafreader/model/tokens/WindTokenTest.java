package com.mycompany.metartafreader.model.tokens;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.assertj.core.api.Assertions.tuple;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD;

@ContextConfiguration("classpath:spring/spring-app.xml")
@RunWith(SpringJUnit4ClassRunner.class)
@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
public class WindTokenTest extends AbstractTokenTest {

    @Autowired
    private WindToken windToken;

    @Override
    protected Token getToken() {
        return windToken;
    }

    @Test
    public void decodeVariableWindTest() {
        assertThat(windToken.recognizedAndFetched("VRB02KMH")).isEqualTo(true);
        windToken.decode();
        assertThat(windToken.getWinds())
                .hasSize(1)
                .extracting("directions", "speed", "gusts", "unit")
                .contains(tuple(new String[]{"variable"}, "02", "", "kilometers per hour"));
    }

    @Test
    public void decodeWindTest() {
        assertThat(windToken.recognizedAndFetched("08002MPS")).isEqualTo(true);
        assertThat(windToken.recognizedAndFetched("040V120")).isEqualTo(true);
        windToken.decode();
        assertThat(windToken.getWinds())
                .hasSize(2)
                .extracting("directions", "speed", "gusts", "unit")
                .contains(tuple(new String[]{"080"}, "02", "", "meters per second"),
                        tuple(new String[]{"040", "120"}, "", "", ""));
    }

    @Test
    public void decodeWindGustsTest() {
        assertThat(windToken.recognizedAndFetched("33013G19KT")).isEqualTo(true);
        windToken.decode();
        assertThat(windToken.getWinds())
                .hasSize(1)
                .extracting("directions", "speed", "gusts", "unit")
                .contains(tuple(new String[]{"330"}, "13", "19", "knots"));
    }
}