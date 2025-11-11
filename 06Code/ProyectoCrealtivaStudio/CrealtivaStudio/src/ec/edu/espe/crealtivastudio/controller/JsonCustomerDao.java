





/**
 *
 * @author Daniel
 */

package ec.edu.espe.crealtivastudio.controller;

import ec.edu.espe.crealtivastudio.model.Customer;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
/**
 *
 * @author Daniel
 */
public class JsonCustomerDao implements CustomerDao {
    
    // 1. CARPETA ESPECÍFICA: Define la ruta a la carpeta 'data/' (se crea si no existe)
    private static final String FOLDER_PATH = "data";
    private static final String FILE_PATH = FOLDER_PATH + File.separator + "customers.json";

    // --- Métodos de ayuda de Archivos ---

    private String readFileContent() {
        // Asegura que la carpeta 'data' exista antes de intentar leer
        new File(FOLDER_PATH).mkdirs(); 
        
        File file = new File(FILE_PATH);
        if (!file.exists() || file.length() == 0) {
            return "[]";
        }
        try (Scanner scanner = new Scanner(file)) {
            scanner.useDelimiter("\\Z");
            return scanner.next();
        } catch (IOException e) {
            System.err.println("ERROR DE LECTURA DE ARCHIVO: " + e.getMessage());
            return "[]";
        }
    }
    
    private void writeFileContent(String content) {
        // Asegura que la carpeta 'data' exista antes de intentar escribir
        new File(FOLDER_PATH).mkdirs(); 
        
        try (FileWriter writer = new FileWriter(FILE_PATH)) {
            writer.write(content);
        } catch (IOException e) {
            System.err.println("ERROR DE ESCRITURA JSON: " + e.getMessage());
        }
    }
    
    // --- Serializacion Manual (Con Formato Ordenado) ---

    /** Transforma un objeto Customer a una cadena JSON (un solo objeto) con formato. */
    private String customerToJson(Customer customer) {
        // Formato con saltos de línea y tabulaciones para hacer el objeto legible
        return String.format(
            "\t{\n\t\t\"id\": %d,\n\t\t\"name\": \"%s\",\n\t\t\"phone\": \"%s\",\n\t\t\"email\": \"%s\",\n\t\t\"address\": \"%s\"\n\t}",
            customer.getId(),
            customer.getName(),
            customer.getPhone(),
            customer.getEmail(),
            customer.getAddress()
        );
    }
    
    /** Combina la lista de clientes en un Array JSON con formato ordenado. */
    private String formatListToJsonArray(List<Customer> customers) {
        // Une los objetos JSON con una coma y un salto de línea
        String innerContent = customers.stream()
            .map(this::customerToJson)
            .collect(Collectors.joining(",\n"));
            
        // Encierra el contenido en corchetes y añade saltos de línea para el array
        return "[\n" + innerContent + "\n]";
    }

    // --- Implementacion de la Interfaz CustomerDao ---

    // El metodo findAll() se mantiene igual, solo usa readFileContent()

    @Override
    public List<Customer> findAll() {
        // [Implementacion anterior]
        String jsonArrayContent = readFileContent();
        List<Customer> customers = new ArrayList<>();
        
        if (jsonArrayContent.length() > 2) {
            // Se usa un patrón más robusto para encontrar objetos JSON, ignorando el formato
            Pattern customerPattern = Pattern.compile("\\{[^{}]+\\}");
            Matcher m = customerPattern.matcher(jsonArrayContent);
            
            while (m.find()) {
                customers.add(jsonToCustomer(m.group()));
            }
        }
        return customers;
    }

    // Los metodos save, update y delete usan el nuevo formatListToJsonArray()

    @Override
    public boolean save(Customer customer) {
        List<Customer> customers = findAll();
        
        boolean idExists = customers.stream().anyMatch(c -> c.getId() == customer.getId());
        if (idExists) {
            System.err.println("ERROR: No se pudo guardar. El ID " + customer.getId() + " ya existe.");
            return false;
        }

        customers.add(customer);
        
        String jsonArray = formatListToJsonArray(customers); // Uso del nuevo metodo
            
        writeFileContent(jsonArray);
        return true;
    }

    @Override
    public boolean update(int id, Customer updatedCustomer) {
        List<Customer> customers = findAll();
        boolean found = false;
        
        for (int i = 0; i < customers.size(); i++) {
            if (customers.get(i).getId() == id) {
                updatedCustomer.setId(id);
                customers.set(i, updatedCustomer);
                found = true;
                break;
            }
        }

        if (found) {
             String jsonArray = formatListToJsonArray(customers); // Uso del nuevo metodo
            writeFileContent(jsonArray);
            return true;
        }
        return false;
    }

    @Override
    public boolean delete(int id) {
        List<Customer> customers = findAll();
        int sizeBefore = customers.size();
        
        customers.removeIf(c -> c.getId() == id);
        
        if (customers.size() < sizeBefore) {
            String jsonArray = formatListToJsonArray(customers); // Uso del nuevo metodo
            writeFileContent(jsonArray);
            return true;
        }
        return false;
    }

    // El método jsonToCustomer se mantiene igual, pero lo he optimizado ligeramente
    private Customer jsonToCustomer(String jsonObject) {
        // [Implementacion anterior]
        Pattern idPattern = Pattern.compile("\"id\"\\s*:\\s*(\\d+)");
        Pattern namePattern = Pattern.compile("\"name\"\\s*:\\s*\"([^\"]*)\"");
        Pattern phonePattern = Pattern.compile("\"phone\"\\s*:\\s*\"([^\"]*)\"");
        Pattern emailPattern = Pattern.compile("\"email\"\\s*:\\s*\"([^\"]*)\"");
        Pattern addressPattern = Pattern.compile("\"address\"\\s*:\\s*\"([^\"]*)\"");

        Matcher m;
        int id = 0;
        String name = "", phone = "", email = "", address = "";

        m = idPattern.matcher(jsonObject);
        if (m.find()) id = Integer.parseInt(m.group(1));

        m = namePattern.matcher(jsonObject);
        if (m.find()) name = m.group(1);
        
        m = phonePattern.matcher(jsonObject);
        if (m.find()) phone = m.group(1);
        
        m = emailPattern.matcher(jsonObject);
        if (m.find()) email = m.group(1);
        
        m = addressPattern.matcher(jsonObject);
        if (m.find()) address = m.group(1);

        return new Customer(id, name, phone, email, address);
    }
}