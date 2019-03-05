package com.mycompany.metartafreader.utils;

import java.util.Optional;
import java.util.function.Function;

public final class StringUtil {
    private StringUtil() {
    }

    public static String nullableConverter(String s, Function<String, String> mapper) {
        return Optional.ofNullable(s)
                .map(mapper)
                .orElse("");
    }
}
