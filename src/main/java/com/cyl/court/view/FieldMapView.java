package com.cyl.court.view;

import com.cyl.court.anotation.Resolver;
import com.cyl.court.anotation.View;
import com.cyl.court.beanfactory.BeanFactory;
import com.cyl.court.control.core.ArticlePropResolver;
import com.cyl.court.control.sql.FieldDesc;
import com.cyl.court.control.sql.SqlFieldResolver;
import com.cyl.court.control.sql.SqlTableResolver;
import com.cyl.court.control.sql.TableDesc;
import com.cyl.court.event.BasicCallbackImpl;
import com.cyl.court.model.ArticleStructModel;

import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

@Resolver
@View(title = "表字段映射配置", resourcePath = "field-map")
public class FieldMapView extends AbstractView implements BaseView, Initializable {

  @FXML
  Pane fieldMap;

  @FXML
  ComboBox selectTable;

  @FXML
  ComboBox startMergeLevel;

  private TableDesc tableDesc;

  private SqlFieldResolver sqlFieldResolver = BeanFactory.getBean(SqlFieldResolver.class);

  @FXML
  private void selectTable(ActionEvent event) {
    Object obj = selectTable.getSelectionModel().getSelectedItem();
    tableDesc = sqlFieldResolver.getFieldDesc(obj.toString(), new BasicCallbackImpl());
    List<String> fields = new ArrayList<>();
    for (FieldDesc fd : tableDesc.getFieldDescList()) {
      fields.add(fd.getName() + "\t\t" + fd.getType() + "(" + fd.getDataSize() + ")");
    }
    for (GridPaneNode.RowNodes ns : gridPaneNode.listNodes) {
      ns.tableField.getItems().clear();
      ns.tableField.getItems().addAll(fields);
    }

  }

  private GridPaneNode gridPaneNode;

  public FieldMapView() {

    this.gridPaneNode = new GridPaneNode();

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

  @FXML
  Button finish;

  private ArticlePropResolver propResolver = BeanFactory.getBean(ArticlePropResolver.class);

  @FXML
  public void finish(ActionEvent event) {

    int mergeL = 0;
    if(startMergeLevel.getValue() != null){
      mergeL = Integer.parseInt(startMergeLevel.getValue().toString());
    }
    propResolver.uploadMergeLevel(mergeL);

    //先载入数据
    gridPaneNode.loadData(articleStructList);

    propResolver.uploadTreeStruct(articleStructList, new BasicCallbackImpl() {
      @Override
      public <T> void success(T t) {
        super.success("配置成功");
        getStage().close();
      }
    });

    System.out.println("finish");

  }

  @FXML
  Button previous;

  @FXML
  public void previous(ActionEvent event) {

    getStage().getScene().setRoot(
        BeanFactory.getBean(TreeStructView.class).getLevelProp());

    System.out.println("previous");

  }

  private SqlTableResolver sqlTableResolver = BeanFactory.getBean(SqlTableResolver.class);

  private List<ArticleStructModel> articleStructList;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    BeanFactory.hostBean(this);

    //获取所有表名
    selectTable.getItems().addAll(sqlTableResolver.getTablesName(new BasicCallbackImpl()));

    articleStructList = propResolver.getArticleStructList(new BasicCallbackImpl());

    //获取上次的树结构
    for (ArticleStructModel articleStructModel : articleStructList) {
      gridPaneNode.newRowNodes(articleStructModel, null);
      startMergeLevel.getItems().add(articleStructModel.getLevel());
    }

  }

  @Override
  public Pane getRootPane() {
    return fieldMap;
  }

  class GridPaneNode {

    List<RowNodes> listNodes = new LinkedList<>();

    public GridPaneNode() {
    }

    int num = 1;

    class RowNodes {

      final Label label = new Label();
      final TextField tfName = new TextField();
      final ComboBox tableField = new ComboBox();

      public void loadData(ArticleStructModel articleStruct) {

        articleStruct.setTableField(
            selectTable.getSelectionModel().getSelectedItem() + "." +
                tableField.getSelectionModel().getSelectedItem().toString().split("\t\t")[0]);

      }

      RowNodes() {

      }

      public RowNodes(int num, ArticleStructModel articleStruct, List tableFieldDesc) {

        label.setText(articleStruct.getLevel());
        tfName.setText(articleStruct.getTitleName());
        tableField.setValue(articleStruct.getTableField());
        if (tableFieldDesc != null)
          tableField.getItems().addAll(tableFieldDesc);

//        tableField

      }

      private Node[] nodes() {
        List<Node> listNode = new ArrayList<>();
        for (Field f : this.getClass().getDeclaredFields()) {
          try {
            if (f.get(this) instanceof Node) {
              f.setAccessible(true);
              listNode.add((Node) f.get(this));
              f.setAccessible(false);
            }

          } catch (IllegalAccessException e) {
            e.printStackTrace();
          } catch (ClassCastException e) {
            throw new RuntimeException("Field must is a Node type !");
          }
        }
        return listNode.toArray(new Node[]{});
      }
    }

    private boolean isFirst = true;

    public void newRowNodes(ArticleStructModel articleStruct, List tableFieldDesc) {
      RowNodes rowNodes = new RowNodes(num, articleStruct, tableFieldDesc);
      Node[] nodes = rowNodes.nodes();
      propGridPane.addRow(num, nodes);
      listNodes.add(rowNodes);
      num++;
    }


    public void loadData(List<ArticleStructModel> articleStructModelList) {

      //排序
      articleStructModelList.sort((e1, e2) -> {
        return Integer.parseInt(e1.getLevel()) < Integer.parseInt(e2.getLevel()) ? -1 : 1;
      });

      listNodes.sort((e1, e2) -> {
        return Integer.parseInt(e1.label.getText()) < Integer.parseInt(e2.label.getText()) ? -1 : 1;
      });

      //加载数据
      for (int i = 0; i < articleStructModelList.size(); i++) {
        listNodes.get(i).loadData(articleStructModelList.get(i));
      }
    }

  }

}
