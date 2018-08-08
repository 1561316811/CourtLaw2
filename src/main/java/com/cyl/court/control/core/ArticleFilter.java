package com.cyl.court.control.core;

import com.cyl.court.model.ArticleNodeContextModel;
import com.cyl.court.model.ArticleNodeModel;

public interface ArticleFilter {

  void doFilter(ArticleNodeContextModel articleNode);

  default ArticleNodeContextModel transform(ArticleNodeModel articleNodeModel, String data){

    if(articleNodeModel.getParent() == null) {

      ArticleNodeContextModel root = new ArticleNodeContextModel();
      root.setContext(data.substring(articleNodeModel.getStart(), articleNodeModel.getEndContext()));

    }




    return null;
  }


}
