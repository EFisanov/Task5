package org.example;

import org.example.model.Message;
import org.example.service.MessageService;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    private static final int PORT = 8080;
    private static final MessageService messageService = new MessageService();

    public static void main(String[] args) {
        try (
                ServerSocket server = new ServerSocket(PORT);
                Socket clientSocket = server.accept();
                BufferedReader br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()))
        ) {
            String incomingMessage;
            System.out.println("Сервер запущен");

            do {
                incomingMessage = br.readLine();
                if (incomingMessage.equals("exit")) {
                    sendResponse("Сервер закрыт.", bw);
                    System.out.println("Сервер закрыт.");
                    break;
                }

                Message savedMessage = messageService.createMessage(incomingMessage);
                int counter = messageService.getMessagesCount();

                String responseMessage = "Сохранено сообщение : " + savedMessage + "\n" +
                        "Количество сообщений: " + counter + "\n";

                sendResponse(responseMessage, bw);
                System.out.println(responseMessage);
            } while (incomingMessage.equals("exit") == false);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    private static void sendResponse(String response, BufferedWriter bw) throws IOException {
        bw.write(response);
        bw.flush();
    }
}
