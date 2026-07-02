package org.example.server;

import com.google.gson.*;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class JsonStorage {
    private static final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private static final ReentrantReadWriteLock.ReadLock readLock = lock.readLock();
    private static final ReentrantReadWriteLock.WriteLock writeLock = lock.writeLock();
    private static final Path FILE = Paths.get("src", "server", "data", "db.json");
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();


    private JsonObject loadDatabase() throws IOException {
        Files.createDirectories(FILE.getParent());
        if (!Files.exists(FILE) || Files.size(FILE) == 0) {
            return new JsonObject();
        }

        try (Reader reader = new FileReader(FILE.toFile())) {
            JsonElement element = JsonParser.parseReader(reader);
            return element != null && element.isJsonObject()
                    ? element.getAsJsonObject()
                    : new JsonObject();
        } catch (JsonSyntaxException e) {
            return new JsonObject();
        }
    }

    private void save(JsonObject data) throws IOException {
        Files.createDirectories(FILE.getParent());
        try (Writer writer = new FileWriter(FILE.toFile())) {
            gson.toJson(data, writer);
        }
    }

    public JsonObject get(String key) {
        readLock.lock();
        try {
            JsonObject data = loadDatabase();
            JsonObject response = new JsonObject();
            if (data.has(key)) {
                response.addProperty("response", "OK");
                response.addProperty("value", data.get(key).getAsString());
            } else {
                response.addProperty("response", "ERROR");
                response.addProperty("reason", "No such key");
            }
            return response;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            readLock.unlock();
        }
    }

    public JsonObject set(String key, String value) {
        writeLock.lock();
        try {
            JsonObject data = loadDatabase();
            data.addProperty(key, value);
            save(data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            writeLock.unlock();
        }
        JsonObject response = new JsonObject();
        response.addProperty("response", "OK");
        return response;
    }

    public JsonObject delete(String key) {
        writeLock.lock();
        try {
            JsonObject data = loadDatabase();
            JsonObject response = new JsonObject();
            if (data.remove(key) != null) {
                response.addProperty("response", "OK");
                save(data);
            } else {
                response.addProperty("response", "ERROR");
                response.addProperty("reason", "No such key");
            }
            return response;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            writeLock.unlock();
        }
    }

}
