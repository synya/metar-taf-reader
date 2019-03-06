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
public class RunwayVisibilityTokenTest extends AbstractTokenTest {
    @Autowired
    private RunwayVisibilityToken runwayVisibilityToken;

    @Override
    protected Token getToken() {
        return runwayVisibilityToken;
    }

    @Test
    public void decodeRunwayVisibilityTestMeters() {
        assertThat(runwayVisibilityToken.recognizedAndFetched("R28L/0250D")).isEqualTo(true);
        assertThat(runwayVisibilityToken.recognizedAndFetched("R28R/P2000N")).isEqualTo(true);
        runwayVisibilityToken.decode();
        assertThat(runwayVisibilityToken.getRunwayVisibilities())
                .hasSize(2)
                .extracting("runway", "bound", "distance", "unit", "tendency")
                .contains(tuple("28L", "", "0250", "meters", "tendency to downwards"),
                        tuple("28R", "prevailing", "2000", "meters", "without tendency to change"));
    }


    @Test
    public void decodeRunwayVisibilityTestFeet() {
        assertThat(runwayVisibilityToken.recognizedAndFetched("R05/P6000FT/D")).isEqualTo(true);
        assertThat(runwayVisibilityToken.recognizedAndFetched("R05R/M0100FT")).isEqualTo(true);
        runwayVisibilityToken.decode();
        assertThat(runwayVisibilityToken.getRunwayVisibilities())
                .hasSize(2)
                .extracting("runway", "bound", "distance", "unit", "tendency")
                .contains(tuple("05", "prevailing", "6000", "feet", "tendency to downwards"),
                        tuple("05R", "less than", "0100", "feet", ""));
    }
}