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
public class VisibilityTokenTest extends AbstractTokenTest {

    @Autowired
    private VisibilityToken visibilityToken;

    @Override
    protected Token getToken() {
        return visibilityToken;
    }

    @Test
    public void decodeVisibilityTest() {
        assertThat(visibilityToken.recognizedAndFetched("P3000")).isEqualTo(true);
        visibilityToken.decode();
        assertThat(visibilityToken.getVisibilities())
                .hasSize(1)
                .extracting("distance", "direction", "unit", "remark")
                .contains(tuple("3000", "", "meters", "prevailing"));
    }

    @Test
    public void decodeDirectedVisibilityTest() {
        assertThat(visibilityToken.recognizedAndFetched("6000")).isEqualTo(true);
        assertThat(visibilityToken.recognizedAndFetched("4500NE")).isEqualTo(true);
        visibilityToken.decode();
        assertThat(visibilityToken.getVisibilities())
                .hasSize(2)
                .extracting("distance", "direction", "unit", "remark")
                .contains(tuple("6000", "", "meters", ""),
                        tuple("4500", "north-east", "meters", ""));
    }

    @Test
    public void decodeFractionalStateMilesVisibilityTest() {
        assertThat(visibilityToken.recognizedAndFetched("P1/4SM")).isEqualTo(true);
        visibilityToken.decode();
        assertThat(visibilityToken.getVisibilities())
                .hasSize(1)
                .extracting("distance", "direction", "unit", "remark")
                .contains(tuple("1/4", "", "state miles", "prevailing"));
    }

    @Test
    public void decodeStateMilesVisibilityTest() {
        assertThat(visibilityToken.recognizedAndFetched("1SM")).isEqualTo(true);
        visibilityToken.decode();
        assertThat(visibilityToken.getVisibilities())
                .hasSize(1)
                .extracting("distance", "direction", "unit", "remark")
                .contains(tuple("1", "", "state miles", ""));
    }

    @Test
    public void decodeCavokTest() {
        assertThat(visibilityToken.recognizedAndFetched("CAVOK")).isEqualTo(true);
        visibilityToken.decode();
        assertThat(visibilityToken.getVisibilities())
                .hasSize(1)
                .extracting("distance", "direction", "unit", "remark")
                .contains(tuple("", "", "", "ceiling and visibility OK"));
    }

    @Test
    public void decodeNotDeterminedTest() {
        assertThat(visibilityToken.recognizedAndFetched("////")).isEqualTo(true);
        visibilityToken.decode();
        assertThat(visibilityToken.getVisibilities())
                .hasSize(1)
                .extracting("distance", "direction", "unit", "remark")
                .contains(tuple("", "", "", "visibility is not determined"));
    }
}