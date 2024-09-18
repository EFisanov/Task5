import java.io.*;
import java.net.Socket;

public class Main {
    private static final int PORT = 8080;
    private static final String URL = "localhost";

    public static void main(String[] args) {
        try (Socket clientSocket = new Socket(URL, PORT);
             BufferedReader br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
             BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            System.out.println("Для завершения введите \"exit\".");
            System.out.println("Введите сообщение до 200 символов: ");

            String message;
            String responseMessage;
            do {
                message = reader.readLine();
                sendMessage(message + "\n", bw);

                responseMessage = br.readLine();
                System.out.println(responseMessage);

                while (br.ready()) {
                    System.out.println(br.readLine());
                }

            } while (message.equals("exit") == false);
            System.out.println("Клиент закрыт.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void sendMessage(String response, BufferedWriter bw) throws IOException {
        bw.write(response);
        bw.flush();
    }
}