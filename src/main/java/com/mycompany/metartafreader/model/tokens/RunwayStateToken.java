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

    private static final Map<String, String> DEPOSITION = new HashMap<>();
    private static final Map<String, String> CONTAMINATION = new HashMap<>();
    private static final Map<String, String> DEPOSITION_DEPTH = new HashMap<>();
    private static final Map<String, String> FRICTION_FACTOR = new HashMap<>();
    private static final Map<String, String> SPECIAL = new HashMap<>();

    static {
        DEPOSITION.put("0", "clear and dry");
        DEPOSITION.put("1", "damp");
        DEPOSITION.put("2", "wet or water patches");
        DEPOSITION.put("3", "rime of frost covered");
        DEPOSITION.put("4", "dry snow");
        DEPOSITION.put("5", "wet snow");
        DEPOSITION.put("6", "slush");
        DEPOSITION.put("7", "ice");
        DEPOSITION.put("8", "compacted or rolled snow");
        DEPOSITION.put("9", "frozen ruts of ridges");

        CONTAMINATION.put("0", "10% or less covered");
        CONTAMINATION.put("1", "10% or less covered");
        CONTAMINATION.put("2", "11% to 25% covered");
        CONTAMINATION.put("5", "26% to 50% covered");
        CONTAMINATION.put("9", "51% to 100% covered");
        CONTAMINATION.put("CLRD//", "runway(s) is(are) cleared");
        CONTAMINATION.put("//99//", "runway(s) is(are) non-operational due clearing");
        CONTAMINATION.put("//////", "no information about runway state");

        DEPOSITION_DEPTH.put("92", "10 cm");
        DEPOSITION_DEPTH.put("93", "15 cm");
        DEPOSITION_DEPTH.put("94", "20 cm");
        DEPOSITION_DEPTH.put("95", "25 cm");
        DEPOSITION_DEPTH.put("96", "30 cm");
        DEPOSITION_DEPTH.put("97", "35 cm");
        DEPOSITION_DEPTH.put("98", "40 cm");

        FRICTION_FACTOR.put("91", "poor");
        FRICTION_FACTOR.put("92", "medium/poor");
        FRICTION_FACTOR.put("93", "medium");
        FRICTION_FACTOR.put("94", "medium/good");
        FRICTION_FACTOR.put("95", "good");

        SPECIAL.put("R/SNOCLO", "runway(s) is(are) closed due to snow");
        SPECIAL.put("R88", "all runways");
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
            this.runway = StringUtil.nullableConverter(runway, r -> (SPECIAL.getOrDefault(r, r)));
            this.deposition = StringUtil.nullableConverter(deposition, DEPOSITION::get);
            this.depositionDepth = StringUtil.nullableConverter(depositionDepth, dd -> (DEPOSITION_DEPTH.getOrDefault(dd, dd + " mm")));
            this.contamination = StringUtil.nullableConverter(contamination, CONTAMINATION::get);
            this.frictionFactor = StringUtil.nullableConverter(frictionFactor, ff -> (FRICTION_FACTOR.getOrDefault(ff, "friction coefficient 0." + ff)));
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
