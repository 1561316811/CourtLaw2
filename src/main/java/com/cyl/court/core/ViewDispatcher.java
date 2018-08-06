package com.cyl.court.core;


import com.cyl.court.anotation.View;
import com.cyl.court.beanfactory.BeanFactory;
import com.cyl.court.config.CourtAutoPathConfig;
import com.cyl.court.util.StringUtils;
import com.cyl.court.view.BaseView;
import com.cyl.court.view.BasicWindow;

import java.io.IOException;
import java.net.URL;


import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class ViewDispatcher {

    public static BasicWindow open(Class<? extends BaseView> viewClass, Modality modality){

        Pane myPane = null;

        View view = (View) viewClass.getAnnotation(View.class);

        if(view == null)
            throw new RuntimeException("View must note for node");
        String fxmlName = view.resourcePath();
        String title = view.title();

        CourtAutoPathConfig courtAutoPathConfig =
                BeanFactory.getBean(CourtAutoPathConfig.class);

        if(!StringUtils.isEmpty(fxmlName)){
            fxmlName = courtAutoPathConfig.getPath()
                    + fxmlName + courtAutoPathConfig.getPostfix();
        }else{
            throw new RuntimeException("resourcePath property is not specify");
        }

        try {
            URL url = ViewDispatcher.class.getClassLoader().getResource(fxmlName);
            myPane = FXMLLoader.load(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene scene = new Scene(myPane);
        Stage window = new Stage();
        window.setScene(scene);
        window.setTitle(title);//Set the title of the new window
        window.initModality(modality == null ? Modality.APPLICATION_MODAL : modality);
        window.showAndWait();

        return new BasicWindow(window,scene,myPane);
    }

}
