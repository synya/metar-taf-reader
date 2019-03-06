package com.mycompany.metartafreader.model.tokens;

import com.mycompany.metartafreader.utils.StringUtil;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class RunwayStateToken extends AbstractToken {

    private static final Pattern RUNWAY_STATE = Pattern.compile("^(?<runway>R[0-9]{2}[LCR]?)/" +
            "(?<operational>((?<deposition>[0-9])(?<contamination>[0-9])(?<depositionDepth>[0-9]{2})(?<frictionFactor>[0-9]{2})|" +
            "(?<special>(CLRD//)|(//99//)|(//////))))|" +
            "(?<closed>R/SNOCLO)$");

    private static final Map<String, String> DEPOSITION_DICTIONARY = new HashMap<>();
    private static final Map<String, String> CONTAMINATION_DICTIONARY = new HashMap<>();
    private static final Map<String, String> DEPOSITION_DEPTH_DICTIONARY = new HashMap<>();
    private static final Map<String, String> FRICTION_FACTOR_DICTIONARY = new HashMap<>();
    private static final Map<String, String> SPECIAL_DICTIONARY = new HashMap<>();

    static {
        DEPOSITION_DICTIONARY.put("0", "clear and dry");
        DEPOSITION_DICTIONARY.put("1", "damp");
        DEPOSITION_DICTIONARY.put("2", "wet or water patches");
        DEPOSITION_DICTIONARY.put("3", "rime of frost covered");
        DEPOSITION_DICTIONARY.put("4", "dry snow");
        DEPOSITION_DICTIONARY.put("5", "wet snow");
        DEPOSITION_DICTIONARY.put("6", "slush");
        DEPOSITION_DICTIONARY.put("7", "ice");
        DEPOSITION_DICTIONARY.put("8", "compacted or rolled snow");
        DEPOSITION_DICTIONARY.put("9", "frozen ruts of ridges");

        CONTAMINATION_DICTIONARY.put("0", "10% or less covered");
        CONTAMINATION_DICTIONARY.put("1", "10% or less covered");
        CONTAMINATION_DICTIONARY.put("2", "11% to 25% covered");
        CONTAMINATION_DICTIONARY.put("5", "26% to 50% covered");
        CONTAMINATION_DICTIONARY.put("9", "51% to 100% covered");
        CONTAMINATION_DICTIONARY.put("CLRD//", "runway(s) is(are) cleared");
        CONTAMINATION_DICTIONARY.put("//99//", "runway(s) is(are) non-operational due clearing");
        CONTAMINATION_DICTIONARY.put("//////", "no information about runway state");

        DEPOSITION_DEPTH_DICTIONARY.put("92", "10 cm");
        DEPOSITION_DEPTH_DICTIONARY.put("93", "15 cm");
        DEPOSITION_DEPTH_DICTIONARY.put("94", "20 cm");
        DEPOSITION_DEPTH_DICTIONARY.put("95", "25 cm");
        DEPOSITION_DEPTH_DICTIONARY.put("96", "30 cm");
        DEPOSITION_DEPTH_DICTIONARY.put("97", "35 cm");
        DEPOSITION_DEPTH_DICTIONARY.put("98", "40 cm");

        FRICTION_FACTOR_DICTIONARY.put("91", "breaking action is poor");
        FRICTION_FACTOR_DICTIONARY.put("92", "breaking action is medium/poor");
        FRICTION_FACTOR_DICTIONARY.put("93", "breaking action is medium");
        FRICTION_FACTOR_DICTIONARY.put("94", "breaking action is medium/good");
        FRICTION_FACTOR_DICTIONARY.put("95", "breaking action is good");

        SPECIAL_DICTIONARY.put("R/SNOCLO", "runway(s) is(are) closed due to snow");
        SPECIAL_DICTIONARY.put("R88", "all runways");
    }

    private final List<RunwayState> runwayStates = new ArrayList<>();

    public final List<RunwayState> getRunwayStates() {
        return runwayStates;
    }

    @Override
    public final String toString() {
        return "RunwayStateToken{" +
                "runwayStates=" + runwayStates +
                '}';
    }

    @Override
    protected final boolean mustBeJoinedBeforeDecode() {
        return false;
    }

    @Override
    protected final Matcher getMatcher(String input) {
        return RUNWAY_STATE.matcher(input);
    }

    @Override
    protected final void runDecode(String token) {
        Matcher matcher = RUNWAY_STATE.matcher(token);
        if (matcher.find()) {
            if (matcher.group("closed") != null) {
                runwayStates.add(new RunwayState(matcher.group("closed"), null, null,
                        null, null));
            } else if (matcher.group("special") != null) {
                runwayStates.add(new RunwayState(matcher.group("runway"), null, null,
                        matcher.group("special"), null));
            } else {
                runwayStates.add(new RunwayState(matcher.group("runway"), matcher.group("deposition"), matcher.group("depositionDepth"),
                        matcher.group("contamination"), matcher.group("frictionFactor")));
            }
        }
    }

    public static class RunwayState {
        private final String runway;
        private final String deposition;
        private final String depositionDepth;
        private final String contamination;
        private final String frictionFactor;

        public RunwayState(String runway, String deposition, String depositionDepth, String contamination, String frictionFactor) {
            this.runway = StringUtil.nullableConverter(runway, r -> (SPECIAL_DICTIONARY.getOrDefault(r, r.substring(1))), () -> "");
            this.deposition = StringUtil.nullableConverter(deposition, DEPOSITION_DICTIONARY::get, () -> "");
            this.depositionDepth = StringUtil.nullableConverter(depositionDepth, dd -> (DEPOSITION_DEPTH_DICTIONARY.getOrDefault(dd, dd + " mm")), () -> "");
            this.contamination = StringUtil.nullableConverter(contamination, CONTAMINATION_DICTIONARY::get, () -> "");
            this.frictionFactor = StringUtil.nullableConverter(frictionFactor, ff -> (FRICTION_FACTOR_DICTIONARY.getOrDefault(ff, "friction coefficient 0." + ff)), () -> "");
        }

        public final String getRunway() {
            return runway;
        }

        public final String getDeposition() {
            return deposition;
        }

        public final String getDepositionDepth() {
            return depositionDepth;
        }

        public final String getContamination() {
            return contamination;
        }

        public final String getFrictionFactor() {
            return frictionFactor;
        }

        @Override
        public final String toString() {
            return "RunwayState{" +
                    "runway='" + runway + '\'' +
                    ", deposition='" + deposition + '\'' +
                    ", depositionDepth='" + depositionDepth + '\'' +
                    ", contamination='" + contamination + '\'' +
                    ", frictionFactor='" + frictionFactor + '\'' +
                    '}';
        }
    }
}
