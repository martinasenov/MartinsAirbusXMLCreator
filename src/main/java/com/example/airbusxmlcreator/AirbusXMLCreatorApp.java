package com.example.airbusxmlcreator;

import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AirbusXMLCreatorApp extends Application {

        private AirbusXMLCreator airbusXMLCreator;

        public static void main(String[] args) {
            launch(args);
        }

        @Override
        public void start(Stage primaryStage) {
            primaryStage.setTitle("Martin's Airbus XML Creator");

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

            Button startButton = new Button("Start");
            startButton.setOnAction(e -> {
                String excelPath = excelPathField.getText();
                int columnToBeRead = Integer.parseInt(getCellField.getText());
                String customization = customizationField.getText();
                String serialNumber = serialNumberField.getText();
                String dmID = dmIDField.getText();

                Task<Void> task = airbusXMLCreator.run(excelPath, columnToBeRead, customization, serialNumber, dmID);
                new Thread(task).start();
            });

            root.getChildren().addAll(excelPathField, getCellField, customizationField, serialNumberField, dmIDField, startButton);

            primaryStage.setScene(new Scene(root, 400, 300));
            primaryStage.show();
        }
    }
