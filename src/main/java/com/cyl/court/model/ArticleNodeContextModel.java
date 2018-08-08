package com.cyl.court.model;

import java.util.ArrayList;
import java.util.List;

public class ArticleNodeContextModel {

    //父节点
    private ArticleNodeContextModel parent;

    private List<ArticleNodeContextModel> children = new ArrayList<>();

    private String context;

    //本节点在当前所在树的层次
    private int level;

    public ArticleNodeContextModel() {
    }

    public ArticleNodeContextModel(ArticleNodeContextModel parent, String context, int level) {
        this.parent = parent;
        this.context = context;
        this.level = level;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public ArticleNodeContextModel getParent() {
        return parent;
    }

    public void setParent(ArticleNodeContextModel parent) {
        this.parent = parent;
    }

    public List<ArticleNodeContextModel> getChildren() {
        return children;
    }

    public void setChildren(List<ArticleNodeContextModel> children) {
        this.children = children;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }
}
