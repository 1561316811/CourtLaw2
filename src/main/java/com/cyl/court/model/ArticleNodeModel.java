package com.cyl.court.model;

import java.util.ArrayList;
import java.util.List;

public class ArticleNodeModel {

    //父节点
    private ArticleNodeModel parent;

    private List<ArticleNodeModel> children;

    //本节点在当前所在的位置
    private int index;

    //本节点内容开始
    private int start;

    //本节点范围结束
    private int endAll;

    //本节点录入的内容结束
    private int endContext;

    public List<ArticleNodeModel> initChildrenList() {
        if (this.children == null)
            this.children = new ArrayList<>();
        return this.children;
    }

    public List<ArticleNodeModel> getChildren() {
//        if (this.children == null) {
//            throw new RuntimeException("Children not init, Please firstly invoke initChildrenList method");
//        }
        return children;
    }

    public void setChildren(List<ArticleNodeModel> children) {
        this.children = children;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public ArticleNodeModel getParent() {
        return parent;
    }

    public void setParent(ArticleNodeModel parent) {
        this.parent = parent;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEndAll() {
        return endAll;
    }

    public void setEndAll(int endAll) {
        this.endAll = endAll;
    }

    public int getEndContext() {
        return endContext;
    }

    public void setEndContext(int endContext) {
        this.endContext = endContext;
    }

   /* @Override
    public String toString() {
        return "ArticleNode{" +
                "parent=" + parent +
                ", children=" + children +
                ", index=" + index +
                ", start=" + start +
                ", endAll=" + endAll +
                ", endContext=" + endContext +
                '}';
    }*/
}
