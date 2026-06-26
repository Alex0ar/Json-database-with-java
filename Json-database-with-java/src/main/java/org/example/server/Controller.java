package org.example.server;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Controller {
    private static final String address = "127.0.0.1";
    private static final int port = 23456;

    private Scanner scanner = new Scanner(System.in);
    //private JsonStorage jsonStorage = new JsonStorage();
    private JsonStorageTemporary jsonStorage = new JsonStorageTemporary();

    public void startServer() {
        try (
                ServerSocket serverSocket = new ServerSocket(port, 50, InetAddress.getByName(address));
        ) {
            System.out.println("Server started!");
            while (true) {
                try (
                        Socket socket = serverSocket.accept();
                        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                ) {
                    String requestString = in.readLine();
                    JsonObject request = new JsonParser().parse(requestString).getAsJsonObject();
                    JsonObject response = switch (request.get("type").getAsString()) {
                        case "get" -> jsonStorage.get(request.get("key").getAsString());
                        case "set" -> jsonStorage.set(request.get("key").getAsString(), request.get("value").getAsString());
                        case "delete" -> jsonStorage.delete(request.get("key").getAsString());
                        case "exit" -> {
                            JsonObject responseJson = new JsonObject();
                            responseJson.addProperty("response", "OK");
                            yield responseJson;
                        }
                        default -> throw new IllegalStateException("Unexpected value: " + request.get("type"));
                    };
                    out.println(response.toString());
                    if (request.get("type").getAsString().equals("exit")) {
                        break;
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
