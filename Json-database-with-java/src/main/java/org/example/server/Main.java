package org.example.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main {
    private static final String address = "127.0.0.1";
    private static final int port = 23456;
    public static void main(String[] args) {
        // **-- STAGE 1 --**
//        Controller controller = new Controller();
//        controller.readClientRequests();

        // ** -- STAGE 2 --**
        try (
                ServerSocket serverSocket = new ServerSocket(port, 50, InetAddress.getByName(address));
        ) {
            System.out.println("Server started!");
            try (
                    Socket socket = serverSocket.accept();
                    DataOutputStream output = new DataOutputStream(socket.getOutputStream());
                    DataInputStream input = new DataInputStream(socket.getInputStream());
            ) {
                String msg = input.readUTF();
                String[] parts = msg.split(" ");
                System.out.println("Received: " + msg);
                output.writeUTF("A record # " + parts[parts.length - 1] + " was sent!");
                System.out.println("Sent: A record # " + parts[parts.length - 1] + " was sent!");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
}
