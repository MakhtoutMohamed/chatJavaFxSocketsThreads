package server;

import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer {
    private static final int PORT = 911;
    private static final List<PrintWriter> clientWriters = Collections.synchronizedList(new ArrayList<>());

    public static void main(String[] args) {
        System.out.println("Serveur démarré sur le port " + PORT);
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket client = serverSocket.accept();
                System.out.println("Nouveau client connecté : " + client.getInetAddress());
                new Thread(() -> handleClient(client)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleClient(Socket client) {
        try (
                BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
                PrintWriter writer = new PrintWriter(client.getOutputStream(), true)
        ) {
            clientWriters.add(writer);
            String message;
            while ((message = reader.readLine()) != null) {
                System.out.println("Reçu: " + message);
                broadcast(message);
            }
        } catch (IOException e) {
            System.out.println("Client déconnecté.");
        }
    }

    private static void broadcast(String message) {
        synchronized (clientWriters) {
            for (PrintWriter w : clientWriters) {
                w.println(message);
            }
        }
    }
}