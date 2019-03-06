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
public class PressureTokenTest extends AbstractTokenTest {
    @Autowired
    private PressureToken pressureToken;

    @Override
    protected Token getToken() {
        return pressureToken;
    }

    @Test
    public void decodePressureTest() {
        assertThat(pressureToken.recognizedAndFetched("Q1031")).isEqualTo(true);
        assertThat(pressureToken.recognizedAndFetched("A1024")).isEqualTo(true);
        pressureToken.decode();
        assertThat(pressureToken.getPressures())
                .hasSize(2)
                .extracting("pressure", "units", "remark")
                .contains(tuple("1031", "hPa", "QNH"),
                        tuple("1024", "inches of mercury", "QNH"));
    }

    @Test
    public void decodeRemarkQFEPressureTest() {
        assertThat(pressureToken.recognizedAndFetched("QFE767/1024")).isEqualTo(true);
        pressureToken.decode();
        assertThat(pressureToken.getPressures())
                .hasSize(2)
                .extracting("pressure", "units", "remark")
                .contains(tuple("767", "mm of mercury", "QFE"),
                        tuple("1024", "hPa", "QFE"));
    }


    @Test
    public void decodeRemarkSLPPressureTest() {
        assertThat(pressureToken.recognizedAndFetched("SLP103")).isEqualTo(true);
        pressureToken.decode();
        assertThat(pressureToken.getPressures())
                .hasSize(1)
                .extracting("pressure", "units", "remark")
                .contains(tuple("1010.3", "mBar", "sea level pressure"));
    }
}