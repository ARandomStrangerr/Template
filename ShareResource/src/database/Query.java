package database;

public class Query {
    private String select,
            tables;

    public Query() {
    }

    public Query select(String... attributes) {
        StringBuilder sb = new StringBuilder();
        for (String attribute : attributes) {
            if (sb.length() != 0) sb.append(",");
            sb.append(attribute);
        }
        select = sb.toString();
        return this;
    }

    public Query from(String... tables) {
        StringBuilder sb = new StringBuilder();
        for (String table : tables) {
//            if (sb.length())
        }
        return this;
    }

    public boolean isValid() {
        return false;
    }

    public String toString() {
        return "";
    }
}
