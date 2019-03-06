package com.mycompany.metartafreader.utils;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

public final class StringUtil {
    private StringUtil() {
    }

    public static String nullableConverter(String s, Function<String, String> converter, Supplier<String> insteadOfNull) {
        return Optional.ofNullable(s)
                .map(converter)
                .orElseGet(insteadOfNull);
    }
}
