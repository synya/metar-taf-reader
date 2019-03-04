package com.mycompany.metartafreader.model.tokens;

public interface Token {

    boolean canBeDecoded(String token);

    void decode();

}
