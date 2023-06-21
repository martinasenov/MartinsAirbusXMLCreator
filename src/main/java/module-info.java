module com.example.martinsairbusxmlcreator {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.xml;
    requires org.apache.poi.ooxml;


    opens com.example.martinsairbusxmlcreator to javafx.fxml;
    exports com.example.martinsairbusxmlcreator;
    exports com.example.airbusxmlcreator;
    opens com.example.airbusxmlcreator to javafx.fxml;
}