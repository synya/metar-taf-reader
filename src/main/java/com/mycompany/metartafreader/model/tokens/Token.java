package com.mycompany.metartafreader.model.tokens;

public interface Token {

    boolean recognizedAndFetched(String token);

    void decode();

}
