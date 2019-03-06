package com.mycompany.metartafreader.model.tokens;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD;

@ContextConfiguration("classpath:spring/spring-app.xml")
@RunWith(SpringJUnit4ClassRunner.class)
@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
public class TemperatureTokenTest extends AbstractTokenTest {
    @Autowired
    private TemperatureToken temperatureToken;

    @Override
    protected Token getToken() {
        return temperatureToken;
    }

    @Test
    public void decodeTemperatureTest() {
        assertThat(temperatureToken.recognizedAndFetched("02/M01")).isEqualTo(true);
        temperatureToken.decode();
        assertThat(temperatureToken.getTemperatures())
                .hasSize(1)
                .extracting("temperature", "dewPoint")
                .contains(tuple("02", "-01"));
    }
}