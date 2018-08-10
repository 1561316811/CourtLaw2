package com.cyl.court.control.core;

import com.cyl.court.model.ArticleNodeContextModel;
import com.cyl.court.model.ArticleNodeModel;

public interface ArticleFilter {

  void doFilter(ArticleNodeContextModel articleNode);

}
