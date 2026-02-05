package utils;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import java.util.ArrayList;
import java.util.List;

public class CrudOperations {
    private static MongoDatabase getDb() { return MongoConnection.getConnection(); }

    public static boolean insert(String collection, Document data) {
        try {
            getDb().getCollection(collection).insertOne(data);
            return true;
        } catch (Exception e) { return false; }
    }

    public static boolean update(String collection, String fieldName, Object value, Document updateData) {
        try {
          
            UpdateResult result = getDb().getCollection(collection)
                    .updateOne(new Document(fieldName, value), new Document("$set", updateData));
            return result.getMatchedCount() > 0;
        } catch (Exception e) { return false; }
    }

    public static boolean delete(String collection, String fieldName, Object value) {
        try {
            return getDb().getCollection(collection).deleteOne(new Document(fieldName, value)).getDeletedCount() > 0;
        } catch (Exception e) { return false; }
    }

    public static List<Document> findAll(String collection) {
        List<Document> list = new ArrayList<>();
        try {
            for (Document doc : getDb().getCollection(collection).find()) { list.add(doc); }
        } catch (Exception e) { }
        return list;
    }

    public static Document searchOne(String collection, Document filter) {
        try {
            return getDb().getCollection(collection).find(filter).first();
        } catch (Exception e) { return null; }
    }
}