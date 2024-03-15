package br.com.sbk.sbking.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public final class FileUtils {

    public static String readFromFilename(String filename, boolean removeEndOfLine) throws IOException {
        InputStream inputStream = FileUtils.class.getResourceAsStream(filename);
        StringBuilder resultStringBuilder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (removeEndOfLine) {
                    resultStringBuilder.append(line.trim());
                } else {
                    resultStringBuilder.append(line).append("\n");
                }
            }
        }
        return resultStringBuilder.toString();
    }

}
