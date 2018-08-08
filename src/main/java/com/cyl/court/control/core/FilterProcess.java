package com.cyl.court.control.core;

import com.cyl.court.model.ArticleNodeContextModel;
import com.cyl.court.model.ArticleNodeModel;

import java.util.Objects;

public class FilterProcess {

  private FilterChain filterChain = new FilterChain();


  public ArticleNodeModel doFilter(ArticleNodeModel articleNodeModel, StringBuilder dataCan) {

    ArticleNodeContextModel articleNodeContextModel = nodeAdaptorToContext(articleNodeModel, dataCan.toString());

    ArticleNodeModel newArticleNode = nodeAdaptorToArticle(articleNodeContextModel, dataCan);

    return newArticleNode;
  }


  /**
   * ArticleNodeModel 转成 ArticleNodeContextModel
   *
   * @param articleRoot
   * @param data
   * @return
   */
  private ArticleNodeContextModel nodeAdaptorToContext(ArticleNodeModel articleRoot, String data) {
    ArticleNodeContextModel root
        = new ArticleNodeContextModel(null,
        data.substring(articleRoot.getStart(), articleRoot.getEndContext()),
        articleRoot.getLevel());
    doAdaptorContext(root, articleRoot, data);
    return root;
  }

  private void doAdaptorContext(ArticleNodeContextModel contextRoot, ArticleNodeModel articleRoot, final String data) {
    for (ArticleNodeModel childNode : articleRoot.getChildren()) {
      ArticleNodeContextModel childContext
          = new ArticleNodeContextModel(contextRoot,
          data.substring(childNode.getStart(), childNode.getEndContext()),
          childNode.getLevel());
      //添加子类
      contextRoot.getChildren().add(childContext);
      doAdaptorContext(childContext, childNode, data);
    }
  }

  /**
   * ArticleNodeContextModel 转成 ArticleNodeModel
   *
   * @param contextRoot
   * @param dataCan
   * @return
   */
  private ArticleNodeModel nodeAdaptorToArticle(ArticleNodeContextModel contextRoot, StringBuilder dataCan) {
    Objects.requireNonNull(dataCan);
    if (dataCan.length() > 0) dataCan.delete(0, dataCan.length());

    ArticleNodeModel articleRoot = new ArticleNodeModel();

    articleRoot.setParent(null);
    articleRoot.setStart(dataCan.length());
    articleRoot.setLevel(contextRoot.getLevel());

    doAdaptorArticle(contextRoot, articleRoot, dataCan);

    articleRoot.setEndContext(dataCan.length());
    articleRoot.setEndAll(dataCan.length());

    return articleRoot;
  }

  private void doAdaptorArticle(ArticleNodeContextModel contextRoot, ArticleNodeModel articleRoot, StringBuilder dataCan) {

//    articleRoot.setStart(dataCan.length());
//    articleRoot.setLevel(contextRoot.getLevel());
    //放入父节点的数据
//    dataCan.append(contextRoot.getContext());
//    articleRoot.setEndContext(dataCan.length());
    for (ArticleNodeContextModel contextChild : contextRoot.getChildren()) {

      ArticleNodeModel articleChild = new ArticleNodeModel();
      articleChild.setStart(articleRoot.getEndContext());
      articleChild.setParent(articleRoot);

      dataCan.append(contextChild.getContext());
      articleChild.setEndContext(dataCan.length());

      articleChild.setLevel(contextChild.getLevel());

      articleRoot.getChildren().add(articleChild);

      if (!contextChild.getChildren().isEmpty())
        doAdaptorArticle(contextChild, articleChild, dataCan);
      articleChild.setEndAll(dataCan.length());
    }

//    articleRoot.setEndAll(dataCan.length());

  }


}
