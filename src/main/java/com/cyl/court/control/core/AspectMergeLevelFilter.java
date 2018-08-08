package com.cyl.court.control.core;

import com.cyl.court.model.ArticleNodeContextModel;
import com.cyl.court.model.ArticleNodeModel;

import java.util.ArrayList;
import java.util.List;

public class AspectMergeLevelFilter implements ArticleFilter {

  private int startLevel;

  public AspectMergeLevelFilter() {

  }

  /**
   * 开始的树的合并等级
   * 包括当前等级
   *
   * @param startLevel
   */
  public AspectMergeLevelFilter(int startLevel) {
    this.startLevel = startLevel;
  }

  @Override
  public void doFilter(ArticleNodeContextModel articleNode) {
    if (startLevel <= 0) return;
//    merge(articleNode);
  }

  private String merge(ArticleNodeContextModel contextRoot) {

    if (contextRoot.getLevel() < startLevel - 1) {
      for (ArticleNodeContextModel childNode : contextRoot.getChildren())
        merge(contextRoot);
    } else if (contextRoot.getChildren().isEmpty()) {
      return contextRoot.getContext();
    } else {
      StringBuilder sb = new StringBuilder();
      ArticleNodeContextModel newChildNode = new ArticleNodeContextModel();
      newChildNode.setParent(contextRoot);
      newChildNode.setLevel(contextRoot.getLevel() + 1);
      for (ArticleNodeContextModel childNode : contextRoot.getChildren()) {
        sb.append(merge(childNode));
      }
      contextRoot.getChildren().clear();
      contextRoot.getChildren().add(newChildNode);
    }

    if (contextRoot.getChildren().isEmpty()) {

    }

    if (contextRoot.getChildren().isEmpty()) {
      if (contextRoot.getLevel() >= startLevel) {
        return contextRoot.getContext();
      }
      return null;
    } else {


      StringBuilder sb = new StringBuilder();
      for (ArticleNodeContextModel childNode : contextRoot.getChildren()) {

        if (childNode.getLevel() >= startLevel) {
          ArticleNodeContextModel newChildNode = new ArticleNodeContextModel();

          sb.append(merge(childNode));
        } else {

        }
      }
    }
    return null;
  }
}
