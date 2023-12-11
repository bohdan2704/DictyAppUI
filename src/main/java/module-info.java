module com.example.dictyappui {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.jsoup;
    requires google.cloud.translate;


    opens com.example.dictyappui to javafx.fxml;
    exports com.example.dictyappui;
}