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
        assertThat(cloudToken.getClouds()).hasSize(2);
        assertThat(cloudToken.getClouds().get(0).getType()).isEqualTo("few (1-2 octant)");
        assertThat(cloudToken.getClouds().get(0).getFlightLevel()).isEqualTo("100");
        assertThat(cloudToken.getClouds().get(0).getRemark()).isEqualTo("");
        assertThat(cloudToken.getClouds().get(1).getType()).isEqualTo("scattered (3-4 octant)");
        assertThat(cloudToken.getClouds().get(1).getFlightLevel()).isEqualTo("200");
        assertThat(cloudToken.getClouds().get(1).getRemark()).isEqualTo("cumulonimbus");
    }

    @Test
    public void decodeNoCloudsTest() {
        assertThat(cloudToken.recognizedAndFetched("NSC")).isEqualTo(true);
        cloudToken.decode();
        assertThat(cloudToken.getClouds()).hasSize(1);
        assertThat(cloudToken.getClouds().get(0).getType()).isEqualTo("no significant clouds");
        assertThat(cloudToken.getClouds().get(0).getFlightLevel()).isEqualTo("");
        assertThat(cloudToken.getClouds().get(0).getRemark()).isEqualTo("");
    }

    @Test
    public void decodeVerticalVisibilityTest() {
        assertThat(cloudToken.recognizedAndFetched("VV050")).isEqualTo(true);
        cloudToken.decode();
        assertThat(cloudToken.getClouds()).hasSize(1);
        assertThat(cloudToken.getClouds().get(0).getType()).isEqualTo("vertical visibility");
        assertThat(cloudToken.getClouds().get(0).getFlightLevel()).isEqualTo("050");
        assertThat(cloudToken.getClouds().get(0).getRemark()).isEqualTo("");
    }
}