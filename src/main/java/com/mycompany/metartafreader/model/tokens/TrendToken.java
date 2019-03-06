package com.mycompany.metartafreader.model.tokens;

import com.mycompany.metartafreader.utils.TokenDateTimeUtil;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class TrendToken extends AbstractToken {

    private static final Pattern TREND = Pattern.compile("^(NOSIG|TEMPO|BECMG)$|" +
            "^([0-9]{4}/[0-9]{4})$|" +
            "^(FM[0-9]{6})$|" +
            "^(PROB[0-9]{2})$");

    private static final Pattern TREND_TOKEN = Pattern.compile("(?<trend>NOSIG|TEMPO|BECMG)");
    private static final Pattern PROBABILITY_TOKEN = Pattern.compile("PROB(?<probability>[0-9]{2})");
    private static final Pattern FROM_TOKEN = Pattern.compile("FM(?<fromDayHourMinute>[0-9]{6})");
    private static final Pattern FROM_TILL_TOKEN = Pattern.compile("(?<fromDayHour>[0-9]{4})/(?<toDayHour>[0-9]{4})");

    private static final Map<String, String> TREND_DICTIONARY = new HashMap<>();

    static {
        TREND_DICTIONARY.put("NOSIG", "no significant changes of weather");
        TREND_DICTIONARY.put("BECMG", "becoming");
        TREND_DICTIONARY.put("TEMPO", "temporary");
    }

    private String trend;
    private LocalDateTime fromDateTime;
    private LocalDateTime toDateTime;

    public final String getTrend() {
        return trend;
    }

    public final LocalDateTime getFromDateTime() {
        return fromDateTime;
    }

    public final LocalDateTime getToDateTime() {
        return toDateTime;
    }

    @Override
    public final String toString() {
        return "TrendToken{" +
                "trend='" + trend + '\'' +
                ", fromDateTime=" + fromDateTime +
                ", toDateTime=" + toDateTime +
                '}';
    }

    @Override
    protected final boolean mustBeJoinedBeforeDecode() {
        return true;
    }

    @Override
    protected final Matcher getMatcher(String input) {
        return TREND.matcher(input);
    }

    @Override
    protected final void runDecode(String token) {
        StringBuilder stringBuilder = new StringBuilder();
        Matcher matcher = PROBABILITY_TOKEN.matcher(token);
        if (matcher.find()) {
            stringBuilder.append("probability ")
                    .append(matcher.group("probability"))
                    .append("%");
        }
        matcher = TREND_TOKEN.matcher(token);
        if (matcher.find()) {
            stringBuilder = optionalAppendDelimitter(stringBuilder);
            stringBuilder.append(TREND_DICTIONARY.get(matcher.group("trend")));
        }
        matcher = FROM_TOKEN.matcher(token);
        if (matcher.find()) {
            stringBuilder = optionalAppendDelimitter(stringBuilder);
            fromDateTime = TokenDateTimeUtil.parseDayHourMinute(matcher.group("fromDayHourMinute"));
            stringBuilder.append("from ").append(TokenDateTimeUtil.format(fromDateTime));
        }
        matcher = FROM_TILL_TOKEN.matcher(token);
        if (matcher.find()) {
            stringBuilder = optionalAppendDelimitter(stringBuilder);
            fromDateTime = TokenDateTimeUtil.parseDayHour(matcher.group("fromDayHour"));
            toDateTime = TokenDateTimeUtil.parseDayHour(matcher.group("toDayHour"));
            stringBuilder.append("from ").append(TokenDateTimeUtil.format(fromDateTime));
            stringBuilder.append(" to ").append(TokenDateTimeUtil.format(toDateTime));
        }
        trend = stringBuilder.toString();
    }

    private StringBuilder optionalAppendDelimitter(StringBuilder stringBuilder) {
        return stringBuilder.length() > 0 ? stringBuilder.append(": ") : stringBuilder;
    }
}
