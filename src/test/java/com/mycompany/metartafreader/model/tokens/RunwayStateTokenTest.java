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
public class RunwayStateTokenTest extends AbstractTokenTest {
    @Autowired
    private RunwayStateToken runwayStateToken;

    @Override
    protected Token getToken() {
        return runwayStateToken;
    }

    @Test
    public void decodeRunwayStateTest() {
        assertThat(runwayStateToken.recognizedAndFetched("R88/090060")).isEqualTo(true);
        runwayStateToken.decode();
        assertThat(runwayStateToken.getRunwayStates())
                .hasSize(1)
                .extracting("runway", "deposition", "contamination", "depositionDepth", "frictionFactor")
                .contains(tuple("all runways", "clear and dry", "51% to 100% covered", "00 mm", "friction coefficient 0.60"));
    }

    @Test
    public void decodeRunwayStateClosedStateTest() {
        assertThat(runwayStateToken.recognizedAndFetched("R/SNOCLO")).isEqualTo(true);
        runwayStateToken.decode();
        assertThat(runwayStateToken.getRunwayStates())
                .hasSize(1)
                .extracting("runway", "deposition", "contamination", "depositionDepth", "frictionFactor")
                .contains(tuple("runway(s) is(are) closed due to snow", "", "", "", ""));
    }

    @Test
    public void decodeRunwayStateClearingTest() {
        assertThat(runwayStateToken.recognizedAndFetched("R10L///99//")).isEqualTo(true);
        runwayStateToken.decode();
        assertThat(runwayStateToken.getRunwayStates())
                .hasSize(1)
                .extracting("runway", "deposition", "contamination", "depositionDepth", "frictionFactor")
                .contains(tuple("R10L", "", "runway(s) is(are) non-operational due clearing", "", ""));
    }

    @Test
    public void decodeRunwayStateClearedTest() {
        assertThat(runwayStateToken.recognizedAndFetched("R10L/CLRD//")).isEqualTo(true);
        runwayStateToken.decode();
        assertThat(runwayStateToken.getRunwayStates())
                .hasSize(1)
                .extracting("runway", "deposition", "contamination", "depositionDepth", "frictionFactor")
                .contains(tuple("R10L", "", "runway(s) is(are) cleared", "", ""));
    }

    @Test
    public void decodeRunwayStateNoInformationTest() {
        assertThat(runwayStateToken.recognizedAndFetched("R26C///////")).isEqualTo(true);
        runwayStateToken.decode();
        assertThat(runwayStateToken.getRunwayStates())
                .hasSize(1)
                .extracting("runway", "deposition", "contamination", "depositionDepth", "frictionFactor")
                .contains(tuple("R26C", "", "no information about runway state", "", ""));
    }
}