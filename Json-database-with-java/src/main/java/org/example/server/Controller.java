package org.example.server;

import java.util.Scanner;

public class Controller {
    private Scanner scanner = new Scanner(System.in);
    private String request;
    private JsonStorage jsonStorage = new JsonStorage();

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
        }
    }


}
