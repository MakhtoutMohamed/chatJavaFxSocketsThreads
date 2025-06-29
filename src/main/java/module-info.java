module com.programstructure.chat {
    requires javafx.controls;
    requires javafx.fxml;

    opens client to javafx.graphics, javafx.fxml;

    exports client;
    exports server;
}