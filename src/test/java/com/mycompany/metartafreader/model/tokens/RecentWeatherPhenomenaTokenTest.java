package com.mycompany.metartafreader.model.tokens;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.assertj.core.api.Assertions.assertThat;

@ContextConfiguration("classpath:spring/spring-app.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class RecentWeatherPhenomenaTokenTest extends AbstractTokenTest {

    @Autowired
    private RecentWeatherPhenomenaToken recentWeatherPhenomenaToken;

    @Override
    protected Token getToken() {
        return recentWeatherPhenomenaToken;
    }

    @Test
    public void decodeRecentWeatherPhenomenaTest() {
        assertThat(recentWeatherPhenomenaToken.recognizedAndFetched("RE+TSRA")).isEqualTo(true);
        recentWeatherPhenomenaToken.decode();
        assertThat(recentWeatherPhenomenaToken.getPhenomena())
                .hasSize(3)
                .containsSequence("heavy", "thunderstorm", "rain");
    }
}