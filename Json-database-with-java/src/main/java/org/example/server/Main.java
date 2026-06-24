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
            while (true) {
                try (
                        Socket socket = serverSocket.accept();
                        DataOutputStream output = new DataOutputStream(socket.getOutputStream());
                        DataInputStream input = new DataInputStream(socket.getInputStream());
                ) {
                    output.writeUTF("Hello World");
                    output.flush();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
}
