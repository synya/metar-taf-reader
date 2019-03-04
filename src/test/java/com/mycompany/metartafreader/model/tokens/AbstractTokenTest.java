package com.mycompany.metartafreader.model.tokens;

import org.junit.Test;

import static org.junit.Assert.assertFalse;

public abstract class AbstractTokenTest {

    protected abstract Token getToken();

    @Test
    public void decodeEmptyTest() {
        assertFalse(getToken().canBeDecoded(""));
    }

    @Test(expected = IllegalArgumentException.class)
    public void decodeNullTest() {
        assertFalse(getToken().canBeDecoded(null));
    }
}
