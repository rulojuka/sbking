package br.com.sbk.sbking.lin;

import java.util.LinkedList;
import java.util.List;

import br.com.sbk.sbking.core.BiddingBox;
import br.com.sbk.sbking.core.Call;

/**
 * LIN files are text files and usually smaller than 1MB.
 * Because of this, the parser will receive String
 * and the file reading should be dealt with elsewhere.
 */

public class LinParser {

    /**
     * LIN files apparently (there is no official documentation) are formed by
     * an ordered list of key-value pairs, in the following format:
     * key|value|
     *
     * Where:
     *  key is a 2-letter string, enumerated at LinKey
     *  value is any string (including an empty string)
     *  and | is the "|" character
     *
     *  \n characters seem to only be used for readability and will be ignored by this parser
     */

    public static ParsedLin fromString(String wholeLinFile) {
        int limitForSplitNotToRemoveTrailingInformation = -1;
        String[] linTokens = wholeLinFile.replace("\n", "").trim().split("\\|", limitForSplitNotToRemoveTrailingInformation);
        List<LinKeyValuePair> list = new LinkedList<>();
        for (int i = 1; i < linTokens.length; i += 2) {
            list.add(new LinKeyValuePair(linTokens[i - 1], linTokens[i]));
        }
        return new ParsedLin(list);
    }

    @SuppressWarnings("checkstyle:NoWhitespaceAfter")
    public static Call parseFromLinCall(String linCall) {
        String pass = "P";
        String[] ddouble = { "D", "DBL" };
        String[] redouble = { "DD", "REDBL", "R" };

        String linCallWithoutAlert = linCall.replace("!", "").toUpperCase();

        if (pass.equals(linCallWithoutAlert)) {
            return BiddingBox.PASS;
        }
        for (String doublePossibleString : ddouble) {
            if (doublePossibleString.equals(linCallWithoutAlert)) {
                return BiddingBox.DOUBLE;
            }
        }
        for (String redoublePossibleString : redouble) {
            if (redoublePossibleString.equals(linCallWithoutAlert)) {
                return BiddingBox.REDOUBLE;
            }
        }
        String label = linCallWithoutAlert.substring(0, 2);
        return BiddingBox.get(label);
    }
}
