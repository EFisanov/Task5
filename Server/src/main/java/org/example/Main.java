package org.example;

import com.example.message.MessagesDocument;
import org.apache.xmlbeans.XmlOptions;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Iterator;

public class Main {
    private final static String FILE_NAME_XML = "C:\\Users\\efisanov\\IdeaProjects\\Task5\\Server\\src\\main\\resources\\data.xml";
    private static final int PORT = 8080;

    public static void main(String[] args) {
        MessagesDocument document = MessagesDocument.Factory.newInstance();
        MessagesDocument.Messages messages = document.addNewMessages();

        try (
                ServerSocket server = new ServerSocket(PORT);
                Socket clientSocket = server.accept();
                BufferedReader br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()))
        ) {
            System.out.println("Сервер запущен");
            String incomingMessage;
            do {
                incomingMessage = br.readLine();

                if (incomingMessage.equals("exit")) {
                    sendResponse("Сервер закрыт.", bw);
                    System.out.println("Сервер закрыт.");
                    break;
                }
                System.out.println(incomingMessage);

                MessagesDocument.Messages.Message message = messages.addNewMessage();
                message.setDate(convertTime(LocalDateTime.now()));
                message.setText(incomingMessage);
                sendResponse("Получено сообщение: " + incomingMessage + "\n", bw);
            } while (incomingMessage.equals("exit") == false);

            serialisationToXml(FILE_NAME_XML, document);
            System.out.println("Количество принятых сообщений: " + countMessagesNumber(FILE_NAME_XML));
        } catch (IOException | SAXException | ParserConfigurationException | XPathExpressionException e) {
            e.printStackTrace();
        }
    }

    public static void serialisationToXml(String fileName, MessagesDocument document) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(fileName)) {
            XmlOptions options = new XmlOptions();
            options.setSavePrettyPrint();
            document.save(fos, options);
        }
    }

    public static Calendar convertTime(LocalDateTime dateTime) {
        long epochSec = dateTime.atZone(ZoneId.systemDefault()).toEpochSecond();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(epochSec * 1000);
        return calendar;
    }

    public static int countMessagesNumber(String fileName) throws IOException, SAXException, ParserConfigurationException, XPathExpressionException {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setNamespaceAware(true);
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document doc = documentBuilder.parse(fileName);

        XPathFactory xpathFactory = XPathFactory.newInstance();
        XPath xpath = xpathFactory.newXPath();

        xpath.setNamespaceContext(new NamespaceContext() {
            @Override
            public String getNamespaceURI(String prefix) {
                return "http://example.com/message";
            }

            @Override
            public String getPrefix(String namespaceURI) {
                return "";
            }

            @Override
            public Iterator<String> getPrefixes(String namespaceURI) {
                return null;
            }
        });

        XPathExpression expr = xpath.compile("count(//mes:messages/mes:message)");
        Number result = (Number) expr.evaluate(doc, XPathConstants.NUMBER);
        return result.intValue();
    }

    private static void sendResponse(String response, BufferedWriter bw) throws IOException {
        bw.write(response);
        bw.flush();
    }
}
