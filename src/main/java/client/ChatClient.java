package client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ChatClient extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/chat-view.fxml"));
        Scene scene = new Scene(loader.load());
        scene.getStylesheets().add(getClass().getResource("/client/chat-style.css").toExternalForm());

        stage.setTitle("Client Chat JavaFX");
        stage.setScene(scene);
        //stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/client/chat-view.fxml"))));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}