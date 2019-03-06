package com.mycompany.metartafreader.model.tokens;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD;

@ContextConfiguration("classpath:spring/spring-app.xml")
@RunWith(SpringJUnit4ClassRunner.class)
@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
public class WindShearTokenTest extends AbstractTokenTest {

    @Autowired
    private WindShearToken windShearToken;

    @Override
    protected Token getToken() {
        return windShearToken;
    }

    @Test
    public void decodeWindShearTest() {
        assertThat(windShearToken.recognizedAndFetched("WS")).isEqualTo(true);
        assertThat(windShearToken.recognizedAndFetched("R28L")).isEqualTo(true);
        windShearToken.decode();
        assertThat(windShearToken.getRunway()).isEqualTo("28L");
    }


    @Test
    public void decodeWindShearAllRunwaysTest() {
        assertThat(windShearToken.recognizedAndFetched("WS")).isEqualTo(true);
        assertThat(windShearToken.recognizedAndFetched("ALL")).isEqualTo(true);
        assertThat(windShearToken.recognizedAndFetched("RWY")).isEqualTo(true);
        windShearToken.decode();
        assertThat(windShearToken.getRunway()).isEqualTo("all runways");
    }
}