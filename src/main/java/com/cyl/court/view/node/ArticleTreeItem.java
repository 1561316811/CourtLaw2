package com.cyl.court.view.node;

import com.cyl.court.model.ArticleNodeModel;

import javafx.scene.control.TreeItem;

public class ArticleTreeItem extends TreeItem {

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    private String title;

    private ArticleNodeModel articleNode;

    public ArticleNodeModel getArticleNode() {
        return articleNode;
    }

    public void setArticleNode(ArticleNodeModel articleNode) {
        this.articleNode = articleNode;
    }

    public ArticleTreeItem() {
    }

    public ArticleTreeItem(Object value) {
        super(value);
    }
}
