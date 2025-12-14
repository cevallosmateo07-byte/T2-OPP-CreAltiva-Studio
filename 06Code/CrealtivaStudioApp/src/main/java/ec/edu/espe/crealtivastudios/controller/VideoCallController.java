package ec.edu.espe.crealtivastudios.controller;

import utils.CrudOperations;
import org.bson.Document;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

public class VideoCallController {

    public static String scheduleVideoCall(
            int customerId,
            String date,
            String hour,
            String medium
    ) {

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
            callDate = LocalDate.parse(date);
        } catch (DateTimeParseException e) {
            return "Formato de fecha invalido";
        }

        if (callDate.isBefore(LocalDate.now())) {
            return "No se puede programar una fecha anterior a hoy";
        }

        // Validar que el cliente no tenga ya una videollamada en MongoDB
        List<Document> existingCalls = CrudOperations.findByField("videocalls", "customerId", customerId);
        if (!existingCalls.isEmpty()) {
            return "El cliente ya tiene una videollamada programada";
        }

        // Crear documento para MongoDB
        Document doc = new Document("customerId", customerId)
                .append("datetime", date + " " + hour)
                .append("medium", medium);

        try {
            CrudOperations.insert("videocalls", doc);
        } catch (Exception e) {
            e.printStackTrace();
            return "No se pudo registrar la videollamada";
        }

        return "OK";
    }
}
