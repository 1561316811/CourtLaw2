package com.cyl.court.view;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class BasicWindow {

    private Stage stage;

    private Scene scene;

    private Pane pane;

    public BasicWindow(){

    }

    public BasicWindow(Stage stage, Scene scene, Pane pane) {
        this.stage = stage;
        this.scene = scene;
        this.pane = pane;
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public Scene getScene() {
        return scene;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    public Pane getPane() {
        return pane;
    }

    public void setPane(Pane pane) {
        this.pane = pane;
    }
}
