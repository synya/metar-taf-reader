package com.mycompany.metartafreader.model.tokens;

import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

public abstract class AbstractToken implements Token {

    private List<String> tokenParts = new ArrayList<>();

    protected abstract Matcher getMatcher(String input);

    protected abstract boolean mustBeJoinedBeforeDecode();

    protected abstract void runDecode(String token);

    public boolean recognizedAndFetched(String token) {
        Assert.notNull(token, "token string must not be null");
        Matcher matcher = getMatcher(token);
        if (matcher.find()) {
            tokenParts.add(matcher.group().trim());
            return true;
        }
        return false;
    }

    public void decode() {
        if (mustBeJoinedBeforeDecode()) {
            runDecode(String.join(" ", tokenParts));
        } else {
            tokenParts.forEach(this::runDecode);
        }
    }

}
