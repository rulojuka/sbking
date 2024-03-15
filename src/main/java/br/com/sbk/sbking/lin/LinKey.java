package br.com.sbk.sbking.lin;

import java.util.HashMap;
import java.util.Map;

public enum LinKey {

    AN("an", "annotation - bid explanation"), MB("mb", "make bid"), MC("mc", "make claim"), MD("md", "make deal? - cards in each players hand"),
    NT("nt", "note - commentary from kibitzers"), PC("pc", "play card"), PG("pg", "?? - separator node, used at the end of tricks and annotations"),
    PN("pn", "players names"), QX("qx", "?? - board info :o/c for open/closed. It also initiates the deal"),
    RS("rs", "results - results for previous sessions"), ST("st", "???"), SV("sv", "?? - vulnerability : o/n/e/b for None-NS-EW-Both"),
    VG("vg", "vugraph : Event information");

    private final String symbol;
    private final String description;
    private static Map<String, LinKey> symbolToKeyMap = new HashMap<>();

    static {
        for (LinKey current : LinKey.values()) {
            symbolToKeyMap.put(current.symbol.toLowerCase(), current);
        }
    }

    LinKey(String symbol, String description) {
        this.symbol = symbol;
        this.description = description;
    }

    public static LinKey get(String symbol) {
        LinKey linKey = symbolToKeyMap.get(symbol.toLowerCase());
        if (linKey == null) {
            throw new IllegalArgumentException("There is no Lin Key with this symbol: " + symbol);
        } else {
            return linKey;
        }
    }

}
