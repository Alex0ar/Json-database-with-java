package org.example.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Controller {
    private static final String address = "127.0.0.1";
    private static final int port = 23456;

    private Scanner scanner = new Scanner(System.in);
    private String request;
    private JsonStorage jsonStorage = new JsonStorage();

    public void startServer() {
        try (
                ServerSocket serverSocket = new ServerSocket(port, 50, InetAddress.getByName(address));
        ) {
            System.out.println("Server started!");
            while (true) {
                try (
                        Socket socket = serverSocket.accept();
                        DataOutputStream output = new DataOutputStream(socket.getOutputStream());
                        DataInputStream input = new DataInputStream(socket.getInputStream());
                ) {
                    String msg = input.readUTF();
                    String[] msgArray = msg.split(" ", 3);
                    String response = switch (msgArray[0]) {
                        case "get" -> jsonStorage.get(Integer.parseInt(msgArray[1]));
                        case "set" -> jsonStorage.set(Integer.parseInt(msgArray[1]), msgArray[2]);
                        case "delete" -> jsonStorage.remove(Integer.parseInt(msgArray[1]));
                        case "exit" -> "OK";
                        default -> throw new IllegalStateException("Unexpected value: " + msgArray[0]);
                    };
                    output.writeUTF(response);
                    if (msgArray[0].equals("exit")) {
                        break;
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
