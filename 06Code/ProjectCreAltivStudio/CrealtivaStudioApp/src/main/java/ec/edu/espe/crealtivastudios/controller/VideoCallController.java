package ec.edu.espe.crealtivastudios.controller;

import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import utils.CrudOperations;
import org.bson.Document;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import utils.JsonOperations;

public class VideoCallController {
    
    private static final String JSON_FILE = "videcalls";
    
    private static final Type VIDEOCALL_LIST_TYPE = new TypeToken<List<Document>>(){
    }
    .getType();

    public static String scheduleVideoCall(int customerId,String date,String hour,String medium) {

        // Validaciones básicas
        
        if (customerId <= 0) {
            return "Cliente invalido";
        }

        if (date == null || date.isEmpty()) {
            return "Fecha obligatoria";
        }

        if (!hour.matches("^([01]\\d|2[0-3]):[0-5]\\d$")) {
            return "Hora invalida (HH:mm)";
        }

        if (medium == null || medium.isEmpty()) {
            return "Medio obligatorio";
        }

        // Validar fecha no pasada
        LocalDate callDate;
        try {
            callDate = LocalDate.parse(date.trim());
        } catch (DateTimeParseException e) {
            return "Formato de fecha invalido";
        }

        if (callDate.isBefore(LocalDate.now())) {
            return "No se puede programar una fecha anterior a hoy";
        }

        // Validar que el cliente no tenga ya una videollamada 
        List<Document> jsonCalls =JsonOperations.loadListFromFile(JSON_FILE, VIDEOCALL_LIST_TYPE);
        
        if (jsonCalls==null){
            jsonCalls = new ArrayList<>();
        }
        
        List<Document> mongoCalls =CrudOperations.findByField("Videocalls", "customerId", customerId);

        if (!mongoCalls.isEmpty()) {
            return "El cliente ya tiene una videollamada programada";
        }


        // Crear documento para MongoDB
        Document videocall = new Document("customerId", customerId).append("datetime", date + " " + hour).append("medium", medium);

        jsonCalls.add(videocall);
        
        boolean savedJson = JsonOperations.saveListToFile(jsonCalls, JSON_FILE);
        
        if (!savedJson) {
            return "Error al guardar la videollamada en JSON";
        }
        
        try {
            CrudOperations.insert("Videocalls", videocall);
        } catch (Exception e) {
            return "Error al guardar la videollamada en MongoDB";
        }
        return "OK";
    }
}
