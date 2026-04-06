package financeapp;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class UserManager {
    private final MongoCollection<Document> collection;

    public UserManager() {
        MongoDatabase db = MongoDBConnection.getDatabase();
        collection = db.getCollection("users");
    }

    public boolean registerUser(String username, String password, String theme) {
        // Provjeri da li korisnik vec postoji
        Document existing = collection.find(new Document("username", username)).first();
        if (existing != null) {
            return false; // korisnik vec postoji
        }
        Document user = new Document("username", username)
                .append("password", password)
                .append("theme", theme);
        collection.insertOne(user);
        return true;
    }

    public User loginUser(String username, String password) {
        Document doc = collection.find(
                new Document("username", username).append("password", password)
        ).first();

        if (doc != null) {
            return new User(
                    doc.getObjectId("_id"),
                    doc.getString("username"),
                    doc.getString("password"),
                    doc.getString("theme")
            );
        }
        return null;
    }
}