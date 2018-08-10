package com.cyl.court.view;

import com.cyl.court.anotation.Resolver;
import com.cyl.court.anotation.View;
import com.cyl.court.beanfactory.BeanFactory;
import com.cyl.court.config.CourtAutoFileConfig;
import com.cyl.court.control.core.FileResolver;
import com.cyl.court.event.BasicCallbackImpl;
import com.cyl.court.event.Callback;
import com.cyl.court.model.ArticleNodeModel;
import com.cyl.court.view.node.ArticleTreeItem;
import com.cyl.court.util.ViewUtil;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;

@Resolver
@View(resourcePath = "main-view", title = "法律文书录入系统")
public class MainView extends AbstractView implements BaseView, Initializable {

  @FXML
  private Pane rootPane;

  MenuBar menuBar = new MenuBar();

  @FXML
  MenuItem fOpenFile;

  @FXML
  MenuItem fExit;

  @FXML
  MenuItem treeView;

  @FXML
  MenuItem cSql;

  @FXML
  TextArea testArea;

  @FXML
  TreeView levelTreeView;

  @FXML
  Text filePathText;

  private String filePath;


  @FXML
  Button resolve;

  @FXML
  Button generateSql;

  @FXML
  private void generateSql(ActionEvent event) {
//    List<String> sqlList =  fileResolver.generateSql(new BasicCallbackImpl());

    ViewDispatcher.open(SqlTextView.class, Modality.APPLICATION_MODAL);
    System.out.println("generateSql");
  }

  @FXML
  private void resolve(ActionEvent event) {
    StringBuilder sb = new StringBuilder();
    ArticleNodeModel rootNode = fileResolver.resolveFile(sb, new BasicCallbackImpl());
    if (rootNode != null) {
      inputLevelTreeViewData(sb.toString(), rootNode);

      generateSql.setDisable(false);
    }
  }

  FileResolver fileResolver = BeanFactory.getBean(FileResolver.class);

  //Open file
  @FXML
  private void openFile(ActionEvent event) {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Open Resource File");
    fileChooser.getExtensionFilters().add(
        new FileChooser.ExtensionFilter("JPG", "*.jpg"));


    CourtAutoFileConfig courtAutoFileConfig = BeanFactory.getBean(CourtAutoFileConfig.class);
    File file = ViewUtil.openFileChooser("Open Resource File", courtAutoFileConfig.getType());
    if (file != null) {

      filePath = file.getAbsolutePath();
      fileResolver.readFile(filePath, new OpenFileCallback());
    }
  }


  @FXML
  private void exit(ActionEvent event) {
    System.exit(0);
  }

  @FXML
  private void connectSql(ActionEvent event) {
    ViewDispatcher.open(ConnectSqlView.class, Modality.APPLICATION_MODAL);
  }

  @FXML
  private void treeView(ActionEvent event) {
    ViewDispatcher.open(TreeStructView.class, Modality.APPLICATION_MODAL);
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    BeanFactory.hostBean(this);

    generateSql.setDisable(true);
  }

  @Override
  public Pane getRootPane() {
    return rootPane;
  }

  private class OpenFileCallback implements Callback {

    @Override
    public <T> void success(T t) {
      filePathText.setText(filePath);
      testArea.setText(t.toString());
    }

    @Override
    public <T> void fail(T t) {

      ViewUtil.f_alert_informationDialog("提示", t.toString());

    }


  }


  /**
   * 输入treeView数据
   *
   * @param article
   * @param rootNode
   */
  public void inputLevelTreeViewData(final String article, ArticleNodeModel rootNode) {

    ArticleTreeItem articleTreeItem = new ArticleTreeItem("root");
    articleTreeItem.setArticleNode(rootNode);

    buildTreeItem(article, articleTreeItem, rootNode);
    levelTreeView.setRoot(articleTreeItem);

//        levelTreeView.getT
    levelTreeView.addEventFilter(MouseEvent.MOUSE_CLICKED, (event) -> {

      Node node = event.getPickResult().getIntersectedNode();

      if (node instanceof Text || (node instanceof TreeCell && ((TreeCell) node).getText() != null)) {

        ArticleTreeItem at = ((ArticleTreeItem) levelTreeView.getSelectionModel().getSelectedItem());
        ArticleNodeModel an = at.getArticleNode();
        String showStr = article.substring(an.getStart(), an.getEndAll());
        testArea.setText(showStr);

      }
      System.out.println("treeItem click");
    });

  }

  private void buildTreeItem(final String article, TreeItem treeItem, ArticleNodeModel node) {
    for (ArticleNodeModel n : node.getChildren()) {

      String context = article.substring(n.getStart(), n.getEndContext());

      context = context.trim();
      ArticleTreeItem tiChild =
          new ArticleTreeItem(context);

      tiChild.setArticleNode(n);
      buildTreeItem(article, tiChild, n);
      treeItem.getChildren().add(tiChild);
    }
  }

}
