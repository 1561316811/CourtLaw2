package com.cyl.court.view;

import javafx.scene.control.ProgressIndicator;

public abstract class AbstractView implements BaseView{


  protected BasicWindow basicWindow;


  public BasicWindow getBasicWindow() {
    return basicWindow;
  }

  public void setBasicWindow(BasicWindow basicWindow) {
    this.basicWindow = basicWindow;
  }



}
