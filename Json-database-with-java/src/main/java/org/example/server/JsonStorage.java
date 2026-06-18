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
    private static final Path FILE = Paths.get("data.json");
    private JsonArray data;

    public void load() throws IOException {
        String content = Files.readString(FILE);
        data = JsonParser.parseString(content).getAsJsonArray();
    }

    public void save() throws IOException {
        Files.writeString(FILE, data.toString());
    }

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
