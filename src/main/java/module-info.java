module com.lombardodumb.demo {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;

    opens com.lombardodumb.demo to javafx.fxml;
    exports com.lombardodumb.demo;
}