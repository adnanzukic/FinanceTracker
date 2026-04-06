package financeapp;

import org.bson.Document;
import org.bson.types.ObjectId;

public class User {
    private ObjectId id;
    private String username;
    private String password;
    private String theme;

    public User(String username, String password, String theme) {
        this.username = username;
        this.password = password;
        this.theme = theme;
    }

    public User(ObjectId id, String username, String password, String theme) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.theme = theme;
    }

    public Document toDocument() {
        return new Document("username", username)
                .append("password", password)
                .append("theme", theme);
    }

    public ObjectId getId() { return id; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getTheme() { return theme; }
}