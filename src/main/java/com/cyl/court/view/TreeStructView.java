package com.cyl.court.view;

import com.cyl.court.anotation.Resolver;
import com.cyl.court.anotation.View;
import com.cyl.court.beanfactory.BeanFactory;
import com.cyl.court.control.core.ArticlePropResolver;
import com.cyl.court.event.BasicCallbackImpl;
import com.cyl.court.model.ArticleStructModel;
import com.cyl.court.util.StringUtils;
import com.cyl.court.util.ViewUtil;

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
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

@Resolver
@View(title = "属性配置", resourcePath = "tree-struct-view")
public class TreeStructView extends AbstractView implements BaseView, Initializable {

  public AnchorPane getLevelProp() {
    return levelProp;
  }

  @FXML
  private AnchorPane levelProp;

  {
//    levelProp.getScene().get
//    getStage().set
  }

  private GridPaneNode gridPaneNode;

  public TreeStructView() {

    this.gridPaneNode = new GridPaneNode();

  }

  @FXML
  GridPane propGridPane;

  @FXML
  Button addLine;

  @FXML
  public void addLine(ActionEvent event) {

    gridPaneNode.newRowNodes(null);

    System.out.println("addLine");

  }



  @FXML
  Button delLine;

  @FXML
  private void delLine(ActionEvent event) {

    gridPaneNode.delRow();
    System.out.println("delLine");

  }

  @FXML
  Button cancel;

  @FXML
  private void cancel(ActionEvent event) {
    Stage stage = (Stage) cancel.getScene().getWindow();
    stage.close();

  }

  @FXML
  Button next;

  private ArticlePropResolver treeLevelResolver = BeanFactory.getBean(ArticlePropResolver.class);

  @FXML
  public void next(ActionEvent event) {

    if (gridPaneNode.checkInput()) {
      BeanFactory.getBean(ArticlePropResolver.class)
          .uploadTreeStruct(gridPaneNode.getData(), new BasicCallbackImpl(){
            @Override
            public <T> void fail(T t) {
              super.fail(t);
              ViewUtil.f_alert_informationDialog("错误！", "文件存储失败！");
            }

            @Override
            public <T> void success(T t) {
              if( BeanFactory.isExist(FieldMapView.class)){
                levelProp.getScene().setRoot(
                    BeanFactory.getBean(FieldMapView.class).getRootPane());
                return ;
              }
              Pane pane = ViewDispatcher.loadFxml(FieldMapView.class);
              levelProp.getScene().setRoot(pane);
            }
          });

      //保存数据
//      treeLevelResolver.saveProperty(gridPaneNode.getData(), new BasicCallbackImpl());
    }

    System.out.println("finish");

  }


  /*@FXML
  Button saveToFile;

  @FXML
  public void saveToFile(ActionEvent event) {

    Gson gson = new Gson();
//        String str = gson.toJson(gridPaneNode.getData());

    treeLevelResolver.saveProperty(gridPaneNode.getData(), new BasicCallbackImpl());
    System.out.println("saveToFile");

  }*/


  @Override
  public void initialize(URL location, ResourceBundle resources) {
    BeanFactory.hostBean(this);
    //只要是先启动这个界面那么就优先加载这个界面
//    this.getBasicWindow().getScene().setRoot(propGridPane);

    List<ArticleStructModel> articleStructS =
        treeLevelResolver.getArticleStructList(new BasicCallbackImpl());

    if (articleStructS != null)
      for (ArticleStructModel articleStruct : articleStructS) {
        gridPaneNode.newRowNodes(articleStruct);
      }

  }

  @Override
  public Pane getRootPane() {
    return levelProp;
  }

  class GridPaneNode {

    List<Node[]> listNodes = new LinkedList<>();

    public GridPaneNode() {
    }

    int num = 1;

    List<String> delRow = new LinkedList<>();

    class RowNodes {

      //            final CheckBox cb = new CheckBox();
      final Label label = new Label();
      final TextField tfName = new TextField();
      final TextField tfRegex = new TextField();
//      final TextField tfFieldName = new TextField();
      ColorPicker cp;

      public ArticleStructModel extractData(Node[] node) {
        ArticleStructModel articleStruct = new ArticleStructModel();

        articleStruct.setLevel(((Label) node[0]).getText());
        articleStruct.setTitleName(((TextField) node[1]).getText());
        articleStruct.setRegex(((TextField) node[2]).getText());
        articleStruct.setColor(((ColorPicker) node[3]).getValue().toString());

        return articleStruct;
      }

      RowNodes() {

      }

      public RowNodes(int num, ArticleStructModel articleStruct) {

        cp = new ColorPicker();
        if (articleStruct != null) {
          num = Integer.parseInt(articleStruct.getLevel());
          label.setText(articleStruct.getLevel());
          tfName.setText(articleStruct.getTitleName());
//          tfFieldName.setText(articleStruct.getField());
          tfRegex.setText(articleStruct.getRegex());
          cp = new ColorPicker(Color.web(articleStruct.getColor()));
        }

        label.setText(num + "");
        label.setAccessibleText(num + "");
        tfName.setAccessibleText(num + "");
        tfRegex.setAccessibleText(num + "");
//        tfFieldName.setAccessibleText(num + "");
        cp.setAccessibleText(num + "");

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

    public void newRowNodes(ArticleStructModel articleStruct) {
      Node[] nodes = new RowNodes(num, articleStruct).nodes();
      propGridPane.addRow(num, nodes);
      listNodes.add(nodes);
      num++;

    }

    public boolean delRow() {
//            if (delRow.isEmpty())
//                return false;
//            for (String str : delRow) {
      delRowNodes(0);
//            }
//            delRow.clear();
      return true;
    }

    private void delRowNodes(int rowNum) {

      num--;

      for (Node n : listNodes.get(listNodes.size() - 1)) {
        propGridPane.getChildren().remove(n);
//                treeGridPane.getRowConstraints().remove(num - 1);
      }
      listNodes.remove(listNodes.size() - 1);

    }

    public Node getNode(int row, int col) {
      return listNodes.get(row)[col];
    }

    public boolean checkInput() {
      for (Node node : propGridPane.getChildren()) {
        if (node instanceof TextField) {
          String data = ((TextField) node).getText();
          if (StringUtils.isEmpty(data)) {
            ViewUtil.f_alert_informationDialog("提示", "亲，还有字段没有输入");
            node.requestFocus();
            return false;
          }
        }
      }
      return true;
    }

    public List<ArticleStructModel> getData() {
      List<ArticleStructModel> listData = new ArrayList<>();
      RowNodes rowNodes = new RowNodes();
      for (Node[] ns : listNodes) {
        ArticleStructModel a = rowNodes.extractData(ns);
        listData.add(a);
      }
      return listData;
    }

  }

}
