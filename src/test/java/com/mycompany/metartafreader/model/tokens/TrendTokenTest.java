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
public class TrendTokenTest extends AbstractTokenTest {

    @Autowired
    TrendToken trendToken;

    @Override
    protected Token getToken() {
        return trendToken;
    }

    @Test
    public void decodeSimpleTrendTest() {
        assertThat(trendToken.recognizedAndFetched("NOSIG")).isEqualTo(true).isEqualTo(true);
        trendToken.decode();
        assertThat(trendToken.getTrend()).isEqualTo("no significant changes of weather");
    }

    @Test
    public void decodeProbabilityTrendTest() {
        assertThat(trendToken.recognizedAndFetched("PROB30")).isEqualTo(true);
        assertThat(trendToken.recognizedAndFetched("TEMPO")).isEqualTo(true);
        assertThat(trendToken.recognizedAndFetched("2512/2516")).isEqualTo(true);
        trendToken.decode();
        assertThat(trendToken.getTrend()).isEqualTo("probability 30%: temporary: from 25 day of month, 12:00 UTC to 25 day of month, 16:00 UTC");
    }

    @Test
    public void decodeBecomingTrendTest() {
        assertThat(trendToken.recognizedAndFetched("BECMG")).isEqualTo(true);
        assertThat(trendToken.recognizedAndFetched("FM181730")).isEqualTo(true);
        trendToken.decode();
        assertThat(trendToken.getTrend()).isEqualTo("becoming: from 18 day of month, 17:30 UTC");
    }

    @Test
    public void decodeFromTrendTest() {
        assertThat(trendToken.recognizedAndFetched("FM181730")).isEqualTo(true);
        trendToken.decode();
        assertThat(trendToken.getTrend()).isEqualTo("from 18 day of month, 17:30 UTC");
    }


    @Test
    public void decodePeriodTest() {
        assertThat(trendToken.recognizedAndFetched("2512/2516")).isEqualTo(true);
        trendToken.decode();
        assertThat(trendToken.getTrend()).isEqualTo("from 25 day of month, 12:00 UTC to 25 day of month, 16:00 UTC");
    }
}