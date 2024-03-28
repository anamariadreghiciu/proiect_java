module com.example.lab789 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.lab789 to javafx.fxml;
    exports com.example.lab789;
}