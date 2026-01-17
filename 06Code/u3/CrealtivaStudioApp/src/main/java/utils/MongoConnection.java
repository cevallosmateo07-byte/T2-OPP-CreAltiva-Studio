package utils;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

public class MongoConnection {
    private static MongoClient mongoClient;
    private static MongoDatabase database;

    // Cambiamos a 'getConnection' para que coincida con lo que el Controlador espera
    public static MongoDatabase getConnection() {
        if (database == null) {
            try {
                String uri = "mongodb+srv://Mateo:Mateo2006@cluster0.2mp0ve2.mongodb.net/?appName=Cluster0";
                mongoClient = MongoClients.create(uri);
                database = mongoClient.getDatabase("CreAltivaStudioDB");
                System.out.println("✅ Conexión establecida con CreAltivaStudioDB");
            } catch (Exception e) {
                System.err.println("❌ Error al conectar: " + e.getMessage());
            }
        }
        return database;
    }

    public static MongoCollection<Document> getPhotographerCollection() {
        return getConnection().getCollection("PhotographerDB");
    }

    public static MongoCollection<Document> getEquipmentCollection() {
        return getConnection().getCollection("EquipmentDB");
    }
    
    // Agregamos este para los eventos que usamos en el FrmAssignEvent
    public static MongoCollection<Document> getEventsCollection() {
        return getConnection().getCollection("Events");
    }

    public static void close() {
        if (mongoClient != null) {
            mongoClient.close();
            database = null;
            System.out.println("Conexión a MongoDB cerrada");
        }
    }
}