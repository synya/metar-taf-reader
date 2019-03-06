package com.mycompany.metartafreader.model.tokens;

import com.mycompany.metartafreader.utils.TokenDateTimeUtil;
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
public class ForecastTemperatureTokenTest extends AbstractTokenTest {
    @Autowired
    private ForecastTemperatureToken forecastTemperatureToken;

    @Override
    protected Token getToken() {
        return forecastTemperatureToken;
    }

    @Test
    public void decodeForecastTemperatureTest() {
        assertThat(forecastTemperatureToken.recognizedAndFetched("TX04/0215Z")).isEqualTo(true);
        assertThat(forecastTemperatureToken.recognizedAndFetched("TNM01/0312Z")).isEqualTo(true);
        forecastTemperatureToken.decode();
        assertThat(forecastTemperatureToken.getTemperatures())
                .hasSize(2)
                .extracting("temperature", "remark")
                .contains(tuple("04", "maximum at: 02 day of month, 15:00 UTC"),
                        tuple("-01", "minimum at: 03 day of month, 12:00 UTC"));
    }
}