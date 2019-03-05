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
public class WeatherPhenomenaTokenTest extends AbstractTokenTest{

    @Autowired
    private WeatherPhenomenaToken weatherPhenomenaToken;

    @Override
    protected Token getToken() {
        return weatherPhenomenaToken;
    }

    @Test
    public void decodeWeatherPhenomenaTest() {
        assertThat(weatherPhenomenaToken.recognizedAndFetched("+TSRA")).isEqualTo(true);
        assertThat(weatherPhenomenaToken.recognizedAndFetched("VCFG")).isEqualTo(true);
        weatherPhenomenaToken.decode();
        assertThat(weatherPhenomenaToken.getPhenomena())
                .hasSize(5)
                .containsSequence("heavy", "thunderstorm", "rain", "in the vicinity of airport", "fog");
    }

}