package com.cyl.court.control.core;

import com.cyl.court.model.ArticleNodeContextModel;
import com.cyl.court.model.ArticleNodeModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class FilterChain implements ArticleFilter {

  List<ArticleFilter> articleFilters = new ArrayList<>();

  public FilterChain addFilter(ArticleFilter filter) {
    articleFilters.add(filter);
    return this;
  }

  private int index = -1;

  @Override
  public void doFilter(ArticleNodeContextModel articleNode) {
    if (index == articleFilters.size()) return;
    articleFilters.get(index).doFilter(articleNode);
  }




}
