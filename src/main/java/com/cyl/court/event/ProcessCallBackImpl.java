package com.cyl.court.event;

import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public class ProcessCallBackImpl extends BasicCallbackImpl {

  final private ProgressIndicator pin = new ProgressIndicator(-1f);

  Pane pane;

  public ProcessCallBackImpl(Pane pane) {
    pin.setTranslateZ(-5d);


//    pane.getScene().set
    this.pane = pane;
    pane.getChildren().add(pin);
  }

  @Override
  public <T> void success(T t) {
    pane.getChildren().remove(pin);
    super.success(t);
  }

  @Override
  public <T> void fail(T t) {
    pane.getChildren().remove(pin);
    super.fail(t);
  }


}
