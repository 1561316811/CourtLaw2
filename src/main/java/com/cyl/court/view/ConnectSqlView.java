package com.cyl.court.view;

import com.cyl.court.anotation.Bean;
import com.cyl.court.anotation.View;
import com.cyl.court.beanfactory.BeanFactory;
import com.cyl.court.control.sql.ConnSqlServerResolver;
import com.cyl.court.event.Callback;
import com.cyl.court.model.SqlConnDataModel;
import com.cyl.court.util.ViewUtil;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

@Bean
@View(resourcePath = "connect-sql-view", title = "Connect to sql")
public class ConnectSqlView implements BaseView, Initializable {

    @FXML
    private Pane rootPane;

    @FXML
    private TextField userName;

    @FXML
    private PasswordField password;

    @FXML
    private TextField url;
    @FXML
    private TextField driverName;

    @FXML
    private Button cTest;
    @FXML
    private Button addJar;

    @FXML
    private Text jarPath;

    ConnSqlServerResolver connSqlServer = BeanFactory.getBean(ConnSqlServerResolver.class);

    File fileJar;

    @FXML
    private void addJar(ActionEvent event){
        fileJar = ViewUtil.openFileChooser("Add jar", "*.jar");

        if(fileJar != null)
            jarPath.setText(fileJar.getName());

    }

    public ConnectSqlView(){

    }

    @FXML
    private void connect(ActionEvent event){

        System.out.println("connect");
    }
    @FXML
    private void testConnect(ActionEvent event){

        SqlConnDataModel sqlConnDataModel = new SqlConnDataModel();

        connSqlServer.testConnSqlServer(fileJar,
                userName.getText(), password.getText(),
                url.getText(), driverName.getText(),
                new TestConnectCallback());

        System.out.println("addJar");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        BeanFactory.hostBean(this);
    }

    class TestConnectCallback implements Callback{

        @Override
        public <T> void success(T t) {
            ViewUtil.alertInfoDialog("连接成功");
        }

        @Override
        public <T> void fail(T t) {
            ViewUtil.alertInfoDialog(t.toString());
        }
    }

}
