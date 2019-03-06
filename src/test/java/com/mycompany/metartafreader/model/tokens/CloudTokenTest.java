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
public class CloudTokenTest extends AbstractTokenTest {
    @Autowired
    private CloudToken cloudToken;

    @Override
    protected Token getToken() {
        return cloudToken;
    }

    @Test
    public void decodeCloudsTest() {
        assertThat(cloudToken.recognizedAndFetched("FEW100")).isEqualTo(true);
        assertThat(cloudToken.recognizedAndFetched("SCT200CB")).isEqualTo(true);
        cloudToken.decode();
        assertThat(cloudToken.getClouds())
                .hasSize(2)
                .extracting("type", "flightLevel", "remark")
                .contains(tuple("few (1-2 octant)", "100", ""),
                        tuple("scattered (3-4 octant)", "200", "cumulonimbus"));
    }

    @Test
    public void decodeNoCloudsTest() {
        assertThat(cloudToken.recognizedAndFetched("NSC")).isEqualTo(true);
        cloudToken.decode();
        assertThat(cloudToken.getClouds())
                .hasSize(1)
                .extracting("type", "flightLevel", "remark")
                .contains(tuple("no significant clouds", "", ""));
    }

    @Test
    public void decodeVerticalVisibilityTest() {
        assertThat(cloudToken.recognizedAndFetched("VV050")).isEqualTo(true);
        cloudToken.decode();
        assertThat(cloudToken.getClouds())
                .hasSize(1)
                .extracting("type", "flightLevel", "remark")
                .contains(tuple("vertical visibility", "050", ""));
    }
}