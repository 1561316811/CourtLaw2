package com.cyl.court.view;

import com.cyl.court.anotation.Resolver;
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
import javafx.stage.Stage;

@Resolver
@View(resourcePath = "connect-sql-view", title = "Connect to sql")
public class ConnectSqlView extends AbstractView implements BaseView, Initializable {

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
        connSqlServer.connSqlServer(fileJar,
            userName.getText(), password.getText(),
            url.getText(), driverName.getText(),
            new TestConnectCallback(){
                @Override
                public <T> void success(T t) {
                    super.success(t);
                    closeWindow();
                }
            });
        System.out.println("connect");
    }

    @FXML
    private void testConnect(ActionEvent event){

        SqlConnDataModel sqlConnDataModel = new SqlConnDataModel();

        connSqlServer.testConnSqlServer(fileJar,
                userName.getText(), password.getText(),
                url.getText(), driverName.getText(),
                new TestConnectCallback());

        System.out.println("testConnect");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        BeanFactory.hostBean(this);
        SqlConnDataModel sqlConnData = connSqlServer.getCurConnData();
        if(sqlConnData == null)
            return ;
        userName.setText(sqlConnData.getUserName());
        password.setText(sqlConnData.getPassword());
        url.setText(sqlConnData.getUrl());
        driverName.setText(sqlConnData.getDriverName());
        fileJar = new File(sqlConnData.getJarPath());
//        System.out.println(sqlConnData.getJarPath());
        String[] temp = sqlConnData.getJarPath().split(File.separator.equals("\\") ? "\\\\" : File.separator);
        jarPath.setText(temp[temp.length - 1]);
    }

    @Override
    public Pane getRootPane() {
        return rootPane;
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
