package com.cyl.court.view;

import com.cyl.court.anotation.Resolver;
import com.cyl.court.anotation.View;
import com.cyl.court.beanfactory.BeanFactory;
import com.cyl.court.control.core.FileResolver;
import com.cyl.court.event.BasicCallbackImpl;
import com.cyl.court.event.ProcessCallBackImpl;
import com.cyl.court.util.ViewUtil;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

@Resolver
@View(title = " Sql 语句 ", resourcePath = "sql-text-view")
public class SqlTextView extends AbstractView implements BaseView, Initializable {

  @FXML
  Pane rootPane;

  @FXML
  CheckBox editable;

  @FXML
  private void editable(ActionEvent event) {
    if(editable.isSelected())
      sqlText.setEditable(true);
    else{
      sqlText.setEditable(false);
    }
  }


  @FXML
  Button insert;

  @FXML
  private void insert(ActionEvent event) {

    List<String> listSqlProto = Arrays.asList(sqlText.getText().split(";"));
    List<String> listSql = new ArrayList<>();

    for (String s : listSqlProto) {
      if(!s.trim().equals("")){
        listSql.add(s);
      }
    }

    fileResolver.insertToDataBase(listSql,
        new ProcessCallBackImpl(rootPane));
  }

  @FXML
  Button copy;

  @FXML
  javafx.scene.control.TextArea sqlText;

  @FXML
  private void copy(ActionEvent event) {

    Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
    Transferable tText = new StringSelection(sqlText.getText());
    clip.setContents(tText, null);

    ViewUtil.alertInfoDialog("复制到剪贴版成功");
  }

  public SqlTextView() {

  }


  @FXML
  GridPane propGridPane;

  @FXML
  Button cancel;

  @FXML
  private void cancel(ActionEvent event) {
    Stage stage = (Stage) cancel.getScene().getWindow();
    stage.close();
  }

  FileResolver fileResolver = BeanFactory.getBean(FileResolver.class);

  private List<String> sqlList ;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    BeanFactory.hostBean(this);
    editable.setSelected(true);

    sqlList = fileResolver.generateSql(new BasicCallbackImpl());
    
    StringBuilder sb = new StringBuilder();

    for (String s : sqlList) {
      sb.append(s + "\n");
    }
    
    sqlText.setText(sb.toString());

  }

  @Override
  public Pane getRootPane() {
    return rootPane;
  }
}
