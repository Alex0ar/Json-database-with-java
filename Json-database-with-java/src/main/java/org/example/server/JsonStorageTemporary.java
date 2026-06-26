package org.example.server;

import com.google.gson.JsonObject;

public class JsonStorageTemporary {
    private JsonObject json;

    public JsonStorageTemporary() {
        json = new JsonObject();
    }

    public JsonObject set(String key, String value) {
        json.addProperty(key, value);
        JsonObject response = new JsonObject();
        response.addProperty("response", "OK");
        return response;
    }

    public JsonObject get(String key) {
        JsonObject response = new JsonObject();
        if (json.has(key)) {
            response.addProperty("response", "OK");
            response.addProperty("value", json.get(key).getAsString());
        } else {
            response.addProperty("response", "ERROR");
            response.addProperty("reason", "No such key");
        }
        return response;
    }

    public JsonObject delete(String key) {
        JsonObject response = new JsonObject();
        if (json.remove(key) != null) {
            response.addProperty("response", "OK");
        } else {
            response.addProperty("response", "ERROR");
            response.addProperty("reason", "No such key");

        }
        return response;
    }



}
