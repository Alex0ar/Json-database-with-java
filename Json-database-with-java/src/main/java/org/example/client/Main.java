package org.example.client;

<<<<<<< Updated upstream
=======
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

>>>>>>> Stashed changes
public class Main {
    public static void main(String[] args) {
<<<<<<< Updated upstream
        System.out.println("Hello, world!");
=======
        Main main = new Main();
        JCommander.newBuilder()
                .addObject(main)
                .build()
                .parse(args);

        if (args[0] == "-in") {
            String fileName = args[1];
            Path path = Paths.get("/data/" + fileName);
            try (FileReader fileReader = new FileReader(path.toFile())) {
                JsonElement element = JsonParser.parseReader(fileReader).getAsJsonObject();
                if (element.isJsonObject()) {
                    main.type = element.getAsJsonObject().get("type").getAsString();
                    main.key = element.getAsJsonObject().get("key").getAsString();
                    if (main.key.equals("set"))
                        main.value = element.getAsJsonObject().get("value").getAsString();
                }
            }  catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        try(
                Socket socket = new Socket(InetAddress.getByName(address), port);
                BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
                ) {
            System.out.println("Client started!");
            JsonObject request = new JsonObject();
            request.addProperty("type", main.type);
            if (!main.type.equals("exit")) {
                request.addProperty("key", main.key);
            }
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


>>>>>>> Stashed changes
    }
}
