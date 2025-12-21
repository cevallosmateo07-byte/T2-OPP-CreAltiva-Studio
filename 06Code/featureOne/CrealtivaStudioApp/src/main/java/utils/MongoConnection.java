package utils;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

public class MongoConnection {
    private static MongoClient mongoClient;
    private static MongoDatabase database;

    public static void connect() {
        if (database != null) return; // ya conectado

        // --- URI actualizado ---
        String uri = "mongodb+srv://kevin:kevin2001@cluster0.oxinj5p.mongodb.net/CreativaStudioDB?retryWrites=true&w=majority";
        mongoClient = MongoClients.create(uri);
        database = mongoClient.getDatabase("CreativaStudioDB");
        System.out.println("Conectado a MongoDB Atlas en CreativaStudioDB");
    }

    public static MongoCollection<Document> getPhotographerCollection() {
        if (database == null)
            throw new IllegalStateException("Mongo no conectado");
        return database.getCollection("PhotographerDB"); // colección correcta
    }

    public static MongoCollection<Document> getEquipmentCollection() {
        if (database == null)
            throw new IllegalStateException("Mongo no conectado");
        return database.getCollection("EquipmentDB"); // colección correcta
    }

    public static MongoDatabase getDatabase() {
        if (database == null) {
            throw new IllegalStateException("Mongo no está conectado — llama a connect() primero");
        }
        return database;
    }

    public static void close() {
        if (mongoClient != null) {
            mongoClient.close();
            database = null;
            System.out.println("Conexión a MongoDB cerrada");
        }
    }
}
