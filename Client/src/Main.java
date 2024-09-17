import java.io.*;
import java.net.Socket;

public class Main {
    private static Socket clientSocket;
    private static BufferedReader in;
    private static BufferedWriter out;

    public static void main(String[] args) {
        try {
            try {
                clientSocket = new Socket("localhost", 8080);
                System.out.println("Для завершения введите \"exit\".");
                System.out.println("Введите сообщение: ");
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                String word;
                String serverWord;
                do {
                    word = reader.readLine();
                    out.write(word + "\n");
                    out.flush();

                    serverWord = in.readLine();
                    System.out.println(serverWord);
                    if(word.equals("exit")) {
                        break;
                    }
                } while (word.equals("exit")==false);

            } finally {
                System.out.println("Клиент закрыт.");
                clientSocket.close();
                in.close();
                out.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}