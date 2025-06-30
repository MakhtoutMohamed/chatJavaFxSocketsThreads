package client;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.io.*;
import java.net.*;

public class ChatController {
    @FXML private TextArea chatArea;
    @FXML private TextField messageField;

    private PrintWriter writer;
    private BufferedReader reader;
    private Socket socket;

    public void initialize() {
        try {
            socket = new Socket("localhost", 911);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(), true);

            Thread receiveThread = new Thread(() -> {
                try {
                    String message;
                    while ((message = reader.readLine()) != null) {
                        String msg = message;
                        Platform.runLater(() -> chatArea.appendText(msg + "\n"));
                    }
                } catch (IOException e) {
                    Platform.runLater(() -> chatArea.appendText("Déconnecté du serveur.\n"));
                }
            });
            receiveThread.setDaemon(true);
            receiveThread.start();
        } catch (IOException e) {
            chatArea.appendText("Impossible de se connecter au serveur.\n");
        }
    }

    @FXML
    private void handleSend() {
        String message = messageField.getText().trim();
        if (!message.isEmpty()) {
            writer.println("Client: " + message);
            //writer.println(message);
            messageField.clear();
        }
    }
}