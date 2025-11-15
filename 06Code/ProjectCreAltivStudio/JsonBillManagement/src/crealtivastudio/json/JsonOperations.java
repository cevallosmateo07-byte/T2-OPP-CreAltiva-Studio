package crealtivastudio.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Librería avanzada para manejar archivos JSON usando Gson
 */
public class JsonOperations {
    private static final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    /**
     * Guarda una lista de objetos en un archivo JSON
     */
    public static <T> boolean saveListToFile(List<T> list, String filename) {
        try (Writer writer = new OutputStreamWriter(
                new FileOutputStream(filename + ".json"), StandardCharsets.UTF_8)) {
            gson.toJson(list, writer);
            return true;
        } catch (IOException e) {
            System.out.println("Error guardando archivo " + filename + ": " + e.getMessage());
            return false;
        }
    }

    /**
     * Carga una lista de objetos desde un archivo JSON
     */
    public static <T> List<T> loadListFromFile(String filename, Type type) {
        File file = new File(filename + ".json");
        if (!file.exists()) {
            return new ArrayList<>();
        }

        try (Reader reader = new InputStreamReader(
                new FileInputStream(file), StandardCharsets.UTF_8)) {
            List<T> list = gson.fromJson(reader, type);
            return list != null ? list : new ArrayList<>();
        } catch (IOException e) {
            System.out.println("Error cargando archivo " + filename + ": " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Guarda un solo objeto en un archivo JSON
     */
    public static <T> boolean saveObjectToFile(T object, String filename) {
        try (Writer writer = new OutputStreamWriter(
                new FileOutputStream(filename + ".json"), StandardCharsets.UTF_8)) {
            gson.toJson(object, writer);
            return true;
        } catch (IOException e) {
            System.out.println("Error guardando archivo " + filename + ": " + e.getMessage());
            return false;
        }
    }

    /**
     * Carga un solo objeto desde un archivo JSON
     */
    public static <T> T loadObjectFromFile(String filename, Class<T> clazz) {
        File file = new File(filename + ".json");
        if (!file.exists()) {
            return null;
        }

        try (Reader reader = new InputStreamReader(
                new FileInputStream(file), StandardCharsets.UTF_8)) {
            return gson.fromJson(reader, clazz);
        } catch (IOException e) {
            System.out.println("Error cargando archivo " + filename + ": " + e.getMessage());
            return null;
        }
    }

    /**
     * Verifica si un archivo JSON existe
     */
    public static boolean fileExists(String filename) {
        return new File(filename + ".json").exists();
    }

    /**
     * Elimina un archivo JSON
     */
    public static boolean deleteFile(String filename) {
        File file = new File(filename + ".json");
        return file.exists() && file.delete();
    }
    
}
