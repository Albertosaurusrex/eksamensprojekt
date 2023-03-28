module com.mycompany.coviddatabase {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.mycompany.coviddatabase to javafx.fxml;
    exports com.mycompany.coviddatabase;
}
