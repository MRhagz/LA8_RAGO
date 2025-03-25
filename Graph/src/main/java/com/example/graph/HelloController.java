package com.example.graph;

import com.example.graph.models.Vertex;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

public class HelloController {
    public AnchorPane apMain;
    public Button btnAdd;
    @FXML
    private Label welcomeText;
    private double xOffset = 0;
    private double yOffset = 0;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }

    public void onVertexClick(MouseEvent mouseEvent) {
        char ch = (char)(Math.random()*26 + 'A');
        Alert a = new Alert(Alert.AlertType.CONFIRMATION, "Would you like " + ch + "?", ButtonType.YES, ButtonType.NO);
//        StackPane sp = (StackPane)mouseEvent.getEventType()
        a.showAndWait().ifPresent(new Consumer<ButtonType>() {
            @Override
            public void accept(ButtonType buttonType) {
                if(buttonType == ButtonType.YES) {
                    StackPane pane = (StackPane) mouseEvent.getSource();
                    Text t = (Text) pane.getChildren().get(1);
                    t.setText(Character.toString(ch));
                }
            }
        });

    }
    public void initialize() {
        // TODO load all the stack panes into apPane
        initializeVertices();
    }

    private void initializeVertices() {
        List<Vertex> vertices = new ArrayList<>();

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("vertices.txt"))) {
            vertices = (List<Vertex>) ois.readObject();

            for (Vertex v : vertices) {
                System.out.println(v.toString());
            }
            setVertices(vertices);
        } catch (EOFException e) {
            System.out.println("File is empty.");
        } catch (IOException e) {
            System.out.println("No file yet or error reading file.");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void onSaveClick(ActionEvent actionEvent) {
        List<Vertex> vertices = new ArrayList<>();

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("vertices.txt"))) {
            vertices = (List<Vertex>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("No existing file, creating a new one.");
        }
        getVertices(vertices);
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("vertices.txt"))) {
            oos.writeObject(vertices);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getVertices(List<Vertex> vertices) {
        for (Object o: apMain.getChildren()) {
            if (o instanceof StackPane) {
                StackPane sp = (StackPane) o;
                Text spT = (Text) sp.getChildren().get(1);
                Vertex v = new Vertex(spT.getText().toString());
                v.setxOffSet(sp.getLayoutX());
                v.setyOffSet(sp.getLayoutY());
                vertices.add(v);
            }
        }
    }
    private void setVertices(List<Vertex> vertices) {
        int i = 0;
        for (Vertex vertex : vertices) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("vertex-view.fxml"));
                StackPane vertexView = loader.load();

                Text text = (Text) vertexView.getChildren().get(1);
                text.setText(vertex.getText());
                vertexView.setOnMouseClicked(this::onVertexClick);
                centerNode(vertexView, apMain);
                makeDraggable(vertexView);
                vertexView.setLayoutX(vertex.getxOffSet());
                vertexView.setLayoutY(vertex.getyOffSet());

                apMain.getChildren().add(vertexView);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void onAddButtonClicked(ActionEvent actionEvent) {
        addVertexView();
    }
    private void addVertexView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("vertex-view.fxml"));
            StackPane vertexView = loader.load(); // Load the FXML as a StackPane

            apMain.getChildren().add(vertexView); // Add it to the AnchorPane
            vertexView.setOnMouseClicked(this::onVertexClick);
            centerNode(vertexView, apMain);
            makeDraggable(vertexView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void centerNode(StackPane node, AnchorPane parent) {
        if (node == null || parent == null) return;

        Bounds parentBounds = parent.getLayoutBounds();
        double centerX = (parentBounds.getWidth() - node.getBoundsInLocal().getWidth()) / 2;
        double centerY = (parentBounds.getHeight() - node.getBoundsInLocal().getHeight()) / 2;

        node.setLayoutX(centerX);
        node.setLayoutY(centerY);
    }

    private void makeDraggable(StackPane node) {
        node.setOnMousePressed(event -> {
            xOffset = event.getSceneX() - node.getLayoutX();
            yOffset = event.getSceneY() - node.getLayoutY();
        });

        node.setOnMouseDragged(event -> {
            node.setLayoutX(event.getSceneX() - xOffset);
            node.setLayoutY(event.getSceneY() - yOffset);
        });
    }

}