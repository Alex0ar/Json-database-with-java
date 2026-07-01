package server;

import com.google.gson.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class JsonStorage {
    private static final Path FILE = Paths.get("../data.json");
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private JsonObject data;

    public JsonStorage() {
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
    }

//    public void load() throws IOException {
//        String content = Files.readString(FILE);
//        data = JsonParser.parseString(content).getAsJsonArray();
//    }

    public void save() throws IOException {
        try (FileWriter fileWriter = new FileWriter(FILE.toFile())) {
            gson.toJson(data, fileWriter);
        }
    }

    public String get(int i) {
        if (i < 1 || i > data.size() || data.get(i - 1).getAsString() == "") {
            return "ERROR";
        }
        return data.get(i - 1).getAsString();
    }

    public String set(int i, String value) {
        if (i < 1 || i > data.size()) {
            return "ERROR";
        } else {
            data.set(i - 1, new JsonPrimitive(value));
            return "OK";
        }
    }

    public String remove(int i) {
        if (i < 1 || i > data.size()) {
            return "ERROR";
        } else {
            data.set(i - 1, new JsonPrimitive(""));
            return "OK";
        }
    }

}
