package ec.edu.espe.crealtivastudio.controller;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import java.util.Collection;
import org.bson.Document;

/**
 *
 * @author Mateo Cevallos
 */
public class CrudOperations {
    
    public static void insert(String collection, Document data){
        MongoDatabase dataBase = MongoDBConnection.getConnection();
        MongoCollection<Document> insertCollection = dataBase.getCollection(collection);
        insertCollection.insertOne(data);
    }
    
    public static void upddate(String collection, Document filter, Document update){
        MongoDatabase dataBase = MongoDBConnection.getConnection();
        MongoCollection<Document> updateCollection = dataBase.getCollection(collection);
        updateCollection.updateOne(filter, new Document("$set", update));
    }
    
    public static void delete(String collection, Document filter){
        MongoDatabase dataBase = MongoDBConnection.getConnection();
        MongoCollection<Document> deleteCollection = dataBase.getCollection(collection);
        deleteCollection.deleteOne(filter);
    }
    
    public static Document searchOne(String collection, Document filter){
        MongoDatabase dataBase = MongoDBConnection.getConnection();
        MongoCollection<Document> findCollection = dataBase.getCollection(collection);
        return findCollection.find(filter).first();
    }
}
