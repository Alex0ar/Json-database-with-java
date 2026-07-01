package org.example.server;

import com.google.gson.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class JsonStorage {
    private static ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private static ReentrantReadWriteLock.ReadLock readLock = lock.readLock();
    private static ReentrantReadWriteLock.WriteLock writeLock = lock.writeLock();
    private static final Path FILE = Paths.get("../data.json");
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private JsonObject data;


    public void loadDatabase() throws IOException {
        readLock.lock();
        try {
            try (FileReader fileReader = new FileReader(FILE.toFile())) {
                JsonElement element = JsonParser.parseReader(fileReader).getAsJsonObject();
                if (element.isJsonObject()) {
                    data = new JsonObject();
                } else {
                    data = element.getAsJsonObject();
                }
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } finally {
            readLock.unlock();
        }
    }

    public void save() throws IOException {
        writeLock.lock();
        try {
            try (FileWriter fileWriter = new FileWriter(FILE.toFile())) {
                gson.toJson(data, fileWriter);
            }
        } finally {
            writeLock.unlock();
        }
    }

    public JsonObject get(String key) {
        try {
            loadDatabase();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        JsonObject response = new JsonObject();
        if (data.has(key)) {
            response.addProperty("response", "OK");
            response.addProperty("value", data.get(key).getAsString());
        } else {
            response.addProperty("response", "ERROR");
            response.addProperty("reason", "No such key");
        }
        return response;
    }

    public JsonObject set(String key, String value) {
        try {
            loadDatabase();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        data.addProperty(key, value);
        try {
            save();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        JsonObject response = new JsonObject();
        response.addProperty("response", "OK");
        return response;
    }

    public JsonObject delete(String key) {
        try {
            loadDatabase();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        JsonObject response = new JsonObject();
        if (data.remove(key) != null) {
            response.addProperty("response", "OK");
        } else {
            response.addProperty("response", "ERROR");
            response.addProperty("reason", "No such key");

        }
        try{
            save();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return response;
    }

}
