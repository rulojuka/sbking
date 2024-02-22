package br.com.sbk.sbking.core;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

public final class ContractFactory {

    private static final Map<String, Strain> STRING_TO_STRAIN_MAP;

    static {
        Map<String, Strain> modifiableMap = new TreeMap<>();
        for (Strain strain : Strain.values()) {
            modifiableMap.put(strain.getSymbol(), strain);
        }
        STRING_TO_STRAIN_MAP = Collections.unmodifiableMap(modifiableMap);
    }

    /**
     * @param text text should be in the format LS[P] where L = level (int from 1 to 7) S = strain P = Penalty ( "X" for double and "XX" for redouble
     * )
     * @return
     */
    public static Contract fromText(String text, boolean vulnerable) {
        int level = Integer.parseInt(text.substring(0, 1));
        String strainText = text.substring(1, 2).toUpperCase();
        Strain strain = STRING_TO_STRAIN_MAP.get(strainText);
        String penalty = "";
        if (text.length() > 2) {
            penalty = text.substring(2, text.length()).toUpperCase();
        }
        boolean doubled = "X".equals(penalty);
        boolean redoubled = "XX".equals(penalty);
        return new Contract(level, strain, doubled, redoubled, vulnerable);
    }

}
