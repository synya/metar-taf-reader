package com.mycompany.metartafreader.model.tokens;

import com.mycompany.metartafreader.utils.StringUtil;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class WindShearToken extends AbstractToken {

    private static final Pattern WIND_SHEAR = Pattern.compile("^(WS)$|^(R[0-9]{2}[LCR]?)$|^(ALL)$|^(RWY)$");
    private static final Pattern WIND_SHEAR_TOKEN = Pattern.compile("^(WS (?<runway>R[0-9]{2}[LCR]?))$|^(?<allRunways>WS ALL RWY)$");

    private String runway = "";

    public final String getRunway() {
        return runway;
    }

    @Override
    public final String toString() {
        return "WindShearToken{" +
                "runway='" + runway + '\'' +
                '}';
    }

    @Override
    protected final boolean mustBeJoinedBeforeDecode() {
        return true;
    }

    @Override
    protected final Matcher getMatcher(String input) {
        return WIND_SHEAR.matcher(input);
    }

    @Override
    protected final void runDecode(String token) {
        Matcher matcher = WIND_SHEAR_TOKEN.matcher(token);
        if (matcher.find()) {
            if (matcher.group("runway") != null) {
                runway = StringUtil.nullableConverter(matcher.group("runway"), r -> r.replace("R", ""));
            }
            if (matcher.group("allRunways") != null) {
                runway = "all runways";
            }
        }
    }
}
