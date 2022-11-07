package server;

public class DBRecord {
    private String key;
    private String value;
    @Override
    public String toString() {
        return "{" + "Key: " +
                key + ", Value: " +
                value + "}";
    }
}
