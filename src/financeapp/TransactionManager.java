package financeapp;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.types.ObjectId;
import java.util.ArrayList;

public class TransactionManager {
    private final MongoCollection<Document> collection;

    public TransactionManager() {
        MongoDatabase db = MongoDBConnection.getDatabase();
        collection = db.getCollection("transactions");
    }

    public void addTransaction(Transaction t) {
        collection.insertOne(t.toDocument());
    }

    public void updateTransaction(ObjectId id, String type, double amount, String description, String category) {
        collection.updateOne(
                Filters.eq("_id", id),
                Updates.combine(
                        Updates.set("type", type),
                        Updates.set("amount", amount),
                        Updates.set("description", description),
                        Updates.set("category", category)
                )
        );
    }

    public void deleteTransaction(ObjectId id) {
        collection.deleteOne(Filters.eq("_id", id));
    }

    public ArrayList<Transaction> getAllTransactions() {
        ArrayList<Transaction> list = new ArrayList<>();
        MongoCursor<Document> cursor = collection.find().iterator();
        while (cursor.hasNext()) {
            Document d = cursor.next();
            list.add(new Transaction(
                    d.getObjectId("_id"),
                    d.getString("type"),
                    d.getDouble("amount"),
                    d.getString("description"),
                    d.getString("category") != null ? d.getString("category") : "Ostalo"
            ));
        }
        return list;
    }

    public double getTotalIncome() {
        double total = 0;
        for (Transaction t : getAllTransactions()) {
            if (t.getType().equals("Prihod")) {
                total += t.getAmount();
            }
        }
        return total;
    }

    public double getTotalExpense() {
        double total = 0;
        for (Transaction t : getAllTransactions()) {
            if (t.getType().equals("Rashod")) {
                total += t.getAmount();
            }
        }
        return total;
    }

    public double getExpenseByCategory(String category) {
        double total = 0;
        for (Transaction t : getAllTransactions()) {
            if (t.getType().equals("Rashod") && t.getCategory().equals(category)) {
                total += t.getAmount();
            }
        }
        return total;
    }
}