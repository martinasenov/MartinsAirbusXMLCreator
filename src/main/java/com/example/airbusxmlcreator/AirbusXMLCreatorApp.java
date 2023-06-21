package com.example.airbusxmlcreator;

import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class AirbusXMLCreatorApp extends Application {

    private AirbusXMLCreator airbusXMLCreator;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Airbus Task Card XML Creator");
        primaryStage.getIcons().add(new Image("C:\\Users\\mitha\\IdeaProjects\\MartinsAirbusXMLCreator\\src\\main\\resources\\Icons and Labels\\Airbus.png"));

        airbusXMLCreator = new AirbusXMLCreator();

        VBox root = new VBox();
        root.setSpacing(10);

        TextField excelPathField = new TextField();
        excelPathField.setPromptText("Enter Excel path here");

        TextField getCellField = new TextField();
        getCellField.setPromptText("Enter the column to be read");

        TextField customizationField = new TextField();
        customizationField.setPromptText("Enter customization");

        TextField serialNumberField = new TextField();
        serialNumberField.setPromptText("Enter Serial Number");

        TextField dmIDField = new TextField();
        dmIDField.setPromptText("Enter dmID");

        TextArea console = new TextArea();
        console.setEditable(false);

        Button startButton = new Button("Start");
        startButton.setOnAction(e -> {
            console.clear();

            String excelPath = excelPathField.getText();
            int columnToBeRead = Integer.parseInt(getCellField.getText());
            String customization = customizationField.getText();
            String serialNumber = serialNumberField.getText();
            String dmID = dmIDField.getText();

            console.appendText("Starting process...\n");

            Task<Void> task = airbusXMLCreator.run(excelPath, columnToBeRead, customization, serialNumber, dmID);

            task.setOnRunning((succeEvent) -> {
                console.appendText("Task is running...\n");
            });

            task.setOnSucceeded((successEvent) -> {
                console.appendText("Task completed successfully!\n");
            });

            task.setOnFailed((failedEvent) -> {
                console.appendText("Task failed!\n");
            });

            new Thread(task).start();
        });

        Label developerLabel = new Label("Developed by Martin Asenov");
        developerLabel.setFont(Font.font("Arial", FontWeight.BOLD, FontPosture.ITALIC, 14));
        developerLabel.setTextFill(Color.web("#0076a3"));

        root.getChildren().addAll(excelPathField, getCellField, customizationField, serialNumberField, dmIDField, startButton, console, developerLabel);

        primaryStage.setScene(new Scene(root, 400, 300));
        primaryStage.show();
    }
}
