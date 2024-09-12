import java.io.*;
import java.net.Socket;

public class Main {
    private static Socket clientSocket;
    private static BufferedReader reader;
    private static BufferedReader in;
    private static BufferedWriter out;

    public static void main(String[] args) {
        try {
            try {
                clientSocket = new Socket("localhost", 8080);
                System.out.println("Введите сообщение:");
                reader = new BufferedReader(new InputStreamReader(System.in));
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

                do {
                    String word = reader.readLine();
                    out.write(word + "\n");
                    out.flush();

                    String serverWord = in.readLine();
                    System.out.println(serverWord);
                } while (true);

            } finally {
                System.out.println("Клиент закрыт.");
                clientSocket.close();
                in.close();
                out.close();
            }
        } catch (IOException e) {
            System.err.println(e);
        }
    }
}