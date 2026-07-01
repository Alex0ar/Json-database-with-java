package org.example.server;

<<<<<<< Updated upstream
=======
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;
import java.net.*;
>>>>>>> Stashed changes
import java.util.Scanner;

public class Controller {
    private Scanner scanner = new Scanner(System.in);
    private String request;
    private JsonStorage jsonStorage = new JsonStorage();

<<<<<<< Updated upstream
    public void readClientRequests() {
        while (true) {
            request = scanner.nextLine();
            if (request.equals("exit")) {
                break;
            }
            String[] requestArray = request.strip().split(" ", 3);
            String response = switch (requestArray[0]) {
                case "get" -> {
                    yield jsonStorage.get(Integer.parseInt(requestArray[1]));
                }
                case "set" -> {
                    yield jsonStorage.set(Integer.parseInt(requestArray[1]), requestArray[2]);
                }
                case "delete" -> {
                    yield jsonStorage.remove(Integer.parseInt(requestArray[1]));
                }
                default -> throw new IllegalStateException("Unexpected value: " + requestArray[0]);
            };
            System.out.println(response);
=======
    public void startServer() {
        //AtomicBoolean running = new AtomicBoolean(true);
        try (
                ServerSocket serverSocket = new ServerSocket(port, 50, InetAddress.getByName(address));
        ) {
            System.out.println("Server started!");
            ExecutorService executor = Executors.newCachedThreadPool();
            while (true) { // running.get() == true when AtomicBoolean was used
                //System.out.println("while() loop - beginning | Waiting for connection...");
                Socket socket;
                try{
                    //System.out.println("Waiting for connection... | inside try catch for socket");
                    socket = serverSocket.accept();
                } catch (SocketException e) {
                    break;
                }

                executor.submit(() -> {
                    System.out.println("submit() method - beginning | Accepted connection from " + socket.getInetAddress().getHostName());
                    try (
                            socket;
                            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                    ) {
                        String requestString = in.readLine();
                        System.out.println("requestString: " + requestString);
                        JsonObject request = new JsonParser().parse(requestString).getAsJsonObject();
                        JsonObject response = switch (request.get("type").getAsString()) {
                            case "get" -> {
                                System.out.println("case get");
                                yield jsonStorage.get(request.get("key").getAsString());
                            }
                            case "set" -> jsonStorage.set(request.get("key").getAsString(), request.get("value").getAsString());
                            case "delete" -> jsonStorage.delete(request.get("key").getAsString());
                            case "exit" -> {
                                //running.set(false);
                                serverSocket.close();
                                //System.out.println("socket.close() | Server should be stopped!");
                                JsonObject responseJson = new JsonObject();
                                responseJson.addProperty("response", "OK");
                                //System.out.println("responseJson: " + responseJson.toString());
                                yield responseJson;
                            }
                            default -> throw new IllegalStateException("Unexpected value: " + request.get("type"));
                        };
                        System.out.println("response: " + response.toString());
                        out.println(response.toString());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
            //System.out.println("after while() loop | executor.shutdown() reached");
            executor.shutdown();
        } catch (IOException e) {
            throw new RuntimeException(e);
>>>>>>> Stashed changes
        }
    }


}
