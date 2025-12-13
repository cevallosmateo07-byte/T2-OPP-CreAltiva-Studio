package utils;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

/**
 *
 * @author Mateo Cevallos
 */
public class MongoDBConnection {
   private static MongoDatabase dataBase;
   private static MongoClient client;
   
   public static MongoDatabase getConnection(){
       
            if (client == null) {
                String uri = "mongodb+srv://Mateo:Mateo2006@cluster0.2mp0ve2.mongodb.net/?appName=Cluster0";
                client = MongoClients.create(uri);
                dataBase = client.getDatabase("CreAltivaStudioDB");
            }
            
        return dataBase;
    }
   }
