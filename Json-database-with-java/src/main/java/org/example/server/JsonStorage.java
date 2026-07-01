package org.example.server;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

public class JsonStorage {
<<<<<<< Updated upstream
    private static final Path FILE = Paths.get("data.json");
    private JsonArray data;

    public void load() throws IOException {
        String content = Files.readString(FILE);
        data = JsonParser.parseString(content).getAsJsonArray();
=======
    private static ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private static ReentrantReadWriteLock.ReadLock readLock = lock.readLock();
    private static ReentrantReadWriteLock.WriteLock writeLock = lock.writeLock();
    private static final Path FILE = Paths.get("./data.json");
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private JsonObject data;


    public void loadDatabase() throws IOException {
        System.out.println("loadDatabase() beginning");
        readLock.lock();
        try {
            try (FileReader fileReader = new FileReader(FILE.toFile())) {
                JsonElement element = JsonParser.parseReader(fileReader).getAsJsonObject();
                if (element.isJsonObject()) {
                    data = element.getAsJsonObject();
                    System.out.println(data.toString());
                } else {
                    data = new JsonObject();
                }
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } finally {
            readLock.unlock();
        }
>>>>>>> Stashed changes
    }

    public void save() throws IOException {
        Files.writeString(FILE, data.toString());
    }

<<<<<<< Updated upstream
    public String get(int i) {
        if (i < 0 || i >= data.size() || data.get(i).getAsString() == "") {
            return "ERROR";
        }
        return data.get(i).getAsString();
    }

    public String set(int i, String value) {
        if (i < 0 || i >= data.size()) {
            return "ERROR";
        } else {
            data.set(i, new JsonPrimitive(value));
            return "OK";
=======
    public JsonObject get(String key) {
        try {
            System.out.println("before JsonStorage.loadDatabase()");
            loadDatabase();
            System.out.println("JsonStorage.loadDatabase() | Database loaded");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        JsonObject response = new JsonObject();
        if (data.has(key)) {
            System.out.println("JsonStorage.data.has(key) | true");
            response.addProperty("response", "OK");
            response.addProperty("value", data.get(key).getAsString());
        } else {
            System.out.println("JsonStorage.data.has(key) | false");
            response.addProperty("response", "ERROR");
            response.addProperty("reason", "No such key");
>>>>>>> Stashed changes
        }
    }

    public String remove(int i) {
        if (i < 0 || i >= data.size()) {
            return "ERROR";
        } else {
            data.set(i, new JsonPrimitive(""));
            return "OK";
        }
    }

    //INIT
    public void initialize() throws IOException {
        data = new JsonArray();
        for (int i = 0; i < 1000; i++) {
            data.add("");
        }
        Files.writeString(FILE, data.toString());
    }

}
