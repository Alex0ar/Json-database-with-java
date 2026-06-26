package org.example.client;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.google.gson.JsonObject;

public class Main {
    private static final String address = "127.0.0.1";
    private static final int port = 23456;

    @Parameter(names = "-t")
    private String type;
    @Parameter(names = "-k")
    private String key;
    @Parameter(names = "-v")
    private String value = "";

    public static void main(String[] args) {
        Main main = new Main();
        JCommander.newBuilder()
                .addObject(main)
                .build()
                .parse(args);

        try(
                Socket socket = new Socket(InetAddress.getByName(address), port);
                BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
        ) {
            System.out.println("Client started!");
            JsonObject request = new JsonObject();
            request.addProperty("type", main.type);
            request.addProperty("key", main.key);

            if (main.type.equals("set")) {
                request.addProperty("value", main.value);
            }

            output.println(request.toString());
            System.out.println("Sent: " + request);

            String received = input.readLine();
            System.out.println("Received: " + received);

        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
}
