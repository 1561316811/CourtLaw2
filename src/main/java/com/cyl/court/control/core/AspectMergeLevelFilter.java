package com.cyl.court.control.core;

import com.cyl.court.model.ArticleNodeContextModel;
import com.cyl.court.model.ArticleNodeModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

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
    System.out.println(AspectMergeLevelFilter.class.getSimpleName() + " is starting ");
    //先找到该层所有的父节点
    Queue<ArticleNodeContextModel> queue = new LinkedList();

    queue.offer(articleNode);

    List<ArticleNodeContextModel> listFatherNodes = new ArrayList<>();
    boolean isGetNodes = false;
    while (!queue.isEmpty()) {
      ArticleNodeContextModel node = queue.poll();
      for (ArticleNodeContextModel n : node.getChildren()) {
        if (node.getLevel() < startLevel - 1) {
          queue.offer(n);
        } else if (node.getLevel() == startLevel - 1) {
          listFatherNodes.add(node);
        } else {
          isGetNodes = true;
        }
      }
      if (isGetNodes)
        break;
    }

    for (ArticleNodeContextModel listFatherNode : listFatherNodes) {
      merge(listFatherNode);
    }

  }

  private void merge(ArticleNodeContextModel contextRoot) {

    Queue<ArticleNodeContextModel> queue = new LinkedList();
    List<ArticleNodeContextModel> childNodes = new ArrayList<>();
    queue.offer(contextRoot);

    while (!queue.isEmpty()) {
      ArticleNodeContextModel node = queue.poll();
      for (ArticleNodeContextModel n : node.getChildren()) {
        queue.offer(n);
        childNodes.add(n);
      }
    }

    Map<Integer, ArticleNodeContextModel> maps = new HashMap<>();

    //init
    for (ArticleNodeContextModel childNode : childNodes) {
      int startLevel = childNode.getLevel();
      if (maps.get(startLevel) == null) {
        maps.put(startLevel, new ArticleNodeContextModel());
      }
      ArticleNodeContextModel a = maps.get(startLevel);
      a.setLevel(startLevel);
      a.setContext((a.getContext() == null ? "" : a.getContext()) + childNode.getContext());
    }

    ArticleNodeContextModel parent = contextRoot;
    for(int i = startLevel;;i++){
      if(maps.get(i) == null)
        break;
      parent.getChildren().clear();
      parent.getChildren().add(maps.get(i));
      maps.get(i).setParent(parent);
      parent = maps.get(i);
    }

  }
}
