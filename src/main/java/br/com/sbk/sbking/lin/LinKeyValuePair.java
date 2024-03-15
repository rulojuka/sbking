package br.com.sbk.sbking.lin;

public final class LinKeyValuePair {

    /**
     * Used for sanity check.
     */
    private static final int MAX_VALUE_SIZE = 1024;
    private static final String ERROR_MESSAGE = "Value length cannot be greater than " + MAX_VALUE_SIZE;

    private LinKey key;
    private String value;

    public LinKeyValuePair(String key, String value) {
        this.key = LinKey.get(key);
        if (value.length() > MAX_VALUE_SIZE) {
            throw new IllegalArgumentException(ERROR_MESSAGE);
        }
        this.value = value;
    }

    public LinKey getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

}
