package org.example.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

public class Main {
    private static final String address = "127.0.0.1";
    private static final int port = 23456;

    @Parameter(names = "-t")
    private String type;
    @Parameter(names = "-i")
    private int index;
    @Parameter(names = "-m")
    private String message = "";

    public static void main(String[] args) {
        Main main = new Main();
        JCommander.newBuilder()
                .addObject(main)
                .build()
                .parse(args);

        try(
                Socket socket = new Socket(InetAddress.getByName(address), port);
                DataInputStream input = new DataInputStream(socket.getInputStream());
                DataOutputStream output = new DataOutputStream(socket.getOutputStream());
        ) {
            System.out.println("Client started!");
            System.out.println("type: " + main.type);
            System.out.println("index: " + main.index);
            System.out.println("message: " + main.message);
            if (main.type.equals("set")) {
                output.writeUTF(main.type + " " + main.index + " " + main.message);
                System.out.println("sent: " + main.type + " " + main.index + " " + main.message);
            } else {
                output.writeUTF(main.type + " " + main.index);
                System.out.println("sent: " + main.type + " " + main.index);
            }

            String msg = input.readUTF();
            System.out.println("Received: " + msg);

        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
}
