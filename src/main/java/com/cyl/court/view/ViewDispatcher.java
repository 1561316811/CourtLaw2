package com.cyl.court.view;


import com.cyl.court.anotation.View;
import com.cyl.court.beanfactory.BeanFactory;
import com.cyl.court.config.CourtAutoResourcePathConfig;
import com.cyl.court.util.StringUtils;

import java.io.IOException;
import java.net.URL;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class ViewDispatcher {

  private static Pane loadFxmlForPan(Class<? extends BaseView> viewClass) {
    Pane myPane = null;
    View view = (View) viewClass.getAnnotation(View.class);
    String fxmlName = view.resourcePath();

    CourtAutoResourcePathConfig courtAutoPathConfig =
        BeanFactory.getBean(CourtAutoResourcePathConfig.class);

    if (!StringUtils.isEmpty(fxmlName)) {
      fxmlName = courtAutoPathConfig.getPath()
          + fxmlName + courtAutoPathConfig.getPostfix();
    } else {
      throw new RuntimeException("resourcePath property is not specify");
    }

    try {
      URL url = ViewDispatcher.class.getClassLoader().getResource(fxmlName);
      if (url == null) {
        throw new RuntimeException(" fxml 资源文件获取失败 ");
      }
      myPane = FXMLLoader.load(url);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return myPane;

  }

  public static Pane loadFxml(Class<? extends BaseView> viewClass) {
    return loadFxmlForPan(viewClass);
  }

  public static void open(Class<? extends BaseView> viewClass, Modality modality) {
    View view = (View) viewClass.getAnnotation(View.class);
    if (view == null)
      throw new RuntimeException("View must note for node");
    try {
      if ( !(viewClass.newInstance() instanceof AbstractView))
        throw new RuntimeException(viewClass.getName() + " must be extends " + AbstractView.class.getName());
    } catch (InstantiationException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }

    String title = view.title();
    Pane pane = loadFxmlForPan(viewClass);
    Scene scene = new Scene(pane);
    Stage window = new Stage();

    ((AbstractView) BeanFactory.getBean(viewClass)).setBasicWindow(new BasicWindow(window, scene, pane));
    window.setScene(scene);
    window.setTitle(title);//Set the title of the new window
    window.initModality(modality == null ? Modality.APPLICATION_MODAL : modality);
    window.showAndWait();


    /*if(viewClass.getSuperclass().equals(AbstractView.class)){
      ((AbstractView)BeanFactory.getBean(viewClass)).setBasicWindow(new BasicWindow(window, scene, pane));
    }*/
//    BeanFactory.getBean(viewClass)
//    return new BasicWindow(window, scene, pane);
  }

}
