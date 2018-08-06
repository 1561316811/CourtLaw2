package com.cyl.court.view;

import com.cyl.court.anotation.Bean;
import com.cyl.court.anotation.View;
import com.cyl.court.beanfactory.BeanFactory;
import com.cyl.court.control.TreeLevelResolver;
import com.cyl.court.event.BasicCallbackImpl;
import com.cyl.court.model.ArticleStructModel;
import com.cyl.court.util.StringUtils;
import com.cyl.court.util.ViewUtil;
import com.google.gson.Gson;

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
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

@Bean
@View(title = "属性配置", resourcePath = "tree-struct-view")
public class TreeStructView implements BaseView, Initializable {

    private GridPaneNode gridPaneNode;

    public TreeStructView() {

        this.gridPaneNode = new GridPaneNode();

    }

    @FXML
    GridPane treeGridPane;

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
//        if(!gridPaneNode.delRow()){
//        ViewUtil.f_alert_informationDialog("提示", "经费不足，待开发");
//            return ;
//        }

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
    Button finish;

    private TreeLevelResolver treeLevelResolver = BeanFactory.getBean(TreeLevelResolver.class);

    @FXML
    public void finish(ActionEvent event) {

        if (gridPaneNode.checkInput()) {
            BeanFactory.getBean(TreeLevelResolver.class)
                    .uploadTreeStruct(gridPaneNode.getData());
        }

        System.out.println("finish");

    }


    @FXML
    Button saveToFile;

    @FXML
    public void saveToFile(ActionEvent event) {

        Gson gson = new Gson();
//        String str = gson.toJson(gridPaneNode.getData());
        treeLevelResolver.saveProperty(gridPaneNode.getData(), new BasicCallbackImpl());
        System.out.println("saveToFile");

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        BeanFactory.hostBean(this);

        List<ArticleStructModel> articleStructS = treeLevelResolver.readProperty();
        for (ArticleStructModel articleStruct : articleStructS) {
            gridPaneNode.newRowNodes(articleStruct);
        }

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
            final TextField tfFieldName = new TextField();
            ColorPicker cp;

            public ArticleStructModel extractData(Node[] node) {
                ArticleStructModel articleStruct = new ArticleStructModel();

                articleStruct.setLevel(((Label)node[0]).getText());
                articleStruct.setTitleName(((TextField) node[1]).getText());
                articleStruct.setRegex(((TextField) node[2]).getText());
                articleStruct.setField(((TextField) node[3]).getText());
                articleStruct.setColor(((ColorPicker) node[4]).getValue().toString());

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
                    tfFieldName.setText(articleStruct.getField());
                    tfRegex.setText(articleStruct.getRegex());
                    cp = new ColorPicker(Color.web(articleStruct.getColor()));
                }

      /*          cb.setId(num + "");
                cb.setOnAction((e) -> {
                    if (cb.isSelected()) {
                        delRow.add(cb.getId());
                    } else {
                        delRow.remove(cb.getId());
                    }
                });
                cb.setAccessibleText(num + "");*/
                label.setText(num + "");
                label.setAccessibleText(num + "");
                tfName.setAccessibleText(num + "");
                tfRegex.setAccessibleText(num + "");
                tfFieldName.setAccessibleText(num + "");
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
            treeGridPane.addRow(num, nodes);
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
                treeGridPane.getChildren().remove(n);
//                treeGridPane.getRowConstraints().remove(num - 1);
            }
            listNodes.remove(listNodes.size() - 1);

        }

        public Node getNode(int row, int col) {
            return listNodes.get(row)[col];
        }

        public boolean checkInput() {
            for (Node node : treeGridPane.getChildren()) {
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
