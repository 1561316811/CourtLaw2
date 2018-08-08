package com.cyl.court.view;

import java.lang.reflect.Field;
import java.util.Objects;

import javafx.scene.Node;
import javafx.stage.Stage;

public interface BaseView {

  default Stage getStage() {
    Stage stage = null;
    for (Field f : this.getClass().getDeclaredFields()) {
      f.setAccessible(true);
      try {
        Object object = f.get(this);
        if (object instanceof Node) {
          stage = (Stage) ((Node) object).getScene().getWindow();
          System.out.print("");
          if (stage != null)
            break;
        }
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      }
      f.setAccessible(false);
    }
    return stage;
  }

  ;

  default void closeWindow() {
    Stage stage = null;
    for (Field f : this.getClass().getDeclaredFields()) {
      f.setAccessible(true);
      try {
        Object object = f.get(this);
        if (object instanceof Node) {
          stage = (Stage) ((Node) object).getScene().getWindow();
          break;
        }
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      }
      f.setAccessible(false);
    }
    stage.close();
  }

}
