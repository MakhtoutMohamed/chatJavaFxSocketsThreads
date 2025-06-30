package server;

import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer {
    private static final int PORT = 911;
    private static final List<ClientHandler> clients = Collections.synchronizedList(new ArrayList<>());

    public static void main(String[] args) {
        System.out.println("serveur démarré sur le port " + PORT);
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket socket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(socket);
                clients.add(clientHandler);
                new Thread(clientHandler).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void broadcast(String message, ClientHandler excludeClient) {
        synchronized (clients) {
            for (ClientHandler client : clients) {
                if (client != excludeClient) {
                    client.sendMessage(message);
                }
            }
        }
    }

    static class ClientHandler implements Runnable {
        private final Socket socket;
        private PrintWriter writer;
        private String clientName = "Client";

        ClientHandler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try (
                    BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            ) {
                writer = new PrintWriter(socket.getOutputStream(), true);

                clientName = socket.getInetAddress().getHostAddress() + ":" + socket.getPort();
                System.out.println("nv client connecté : " + clientName);

                broadcast("@" + clientName + " a rejoint le chat.", this);

                String message;
                while ((message = reader.readLine()) != null) {
                    System.out.println("@" + clientName + ": " + message);
                    broadcast("@" + clientName + ": " + message, this);
                }

            } catch (IOException e) {
                System.out.println("@" + clientName + " s'est déconnecté.");
            } finally {
                try {
                    socket.close();
                } catch (IOException ignored) {}
                clients.remove(this);
                broadcast("@" + clientName + " a quitté le chat.", this);
            }
        }

        public void sendMessage(String message) {
            if (writer != null) {
                writer.println(message);
            }
        }
    }
}