package handlers;

public enum Method {
    GET("GET"),
    POST("POST"),
    DELETE("DELETE"),
    UNKNOWN("UNKNOWN");

    private final String value;

    Method(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }

    public static Method fromString(String value) {
        for (Method method : Method.values()) {
            if (method.getValue().equalsIgnoreCase(value)) {
                return method;
            }
        }
        return UNKNOWN;
    }
}