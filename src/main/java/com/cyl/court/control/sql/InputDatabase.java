package com.cyl.court.control.sql;

import com.cyl.court.anotation.Resolver;
import com.cyl.court.model.ArticleNodeModel;
import com.cyl.court.model.ArticleStructModel;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


@Resolver
public class InputDatabase {

  public List<String> createSqlStatement(ArticleNodeModel rootNode,
                                         List<ArticleStructModel> articleNodeModelList,
                                         String data) {

    if (rootNode == null || articleNodeModelList == null || articleNodeModelList.size() == 0) {
      throw new RuntimeException("Parameters are not accept, fount null Object or empty list");
    }
    //获取表名
    String tableName = articleNodeModelList.get(0).getTableField().split("\\.")[0];

    List<String> sqlList = new ArrayList<>();
    createSql(rootNode, data,
        articleNodeModelList,
        new ArrayList<ArticleNodeModel>(),
        sqlList, tableName
    );
    return sqlList;
  }

  public boolean insert(List<String> sqlList) throws SQLException {

    if (sqlList == null || sqlList.isEmpty()) {
      return false;
    }

    Connection conn = DBUtil.getConnection();
    Statement stat = null;

    conn.setAutoCommit(false);
    stat = conn.createStatement();

    for (String sql : sqlList) {
      stat.addBatch(sql);
      System.out.println(sql);
    }

    stat.executeBatch();
    conn.commit();

    return true;
  }

  private void createSql(ArticleNodeModel articleNodeModel, String data,
                         final List<ArticleStructModel> articleStructModelList,
                         List<ArticleNodeModel> articleNodeModelList,
                         List<String> sqlList,
                         String tableName) {

    if (articleNodeModel.getParent() != null)
      articleNodeModelList.add(articleNodeModel);

    //如果是叶子节点
    if (articleNodeModel.getChildren().isEmpty()) {
      List<DataField> dataFields = new ArrayList<>();
      for (ArticleNodeModel n : articleNodeModelList) {
        dataFields.add(getDataField(n, articleStructModelList, data));
      }
      sqlList.add(getSql(dataFields, tableName));
    }

    for (ArticleNodeModel child : articleNodeModel.getChildren()) {
      createSql(child, data, articleStructModelList, articleNodeModelList, sqlList, tableName);
    }

    //移除最后一个
    if (!articleNodeModelList.isEmpty())
      articleNodeModelList.remove(articleNodeModelList.size() - 1);

  }


  private DataField getDataField(ArticleNodeModel articleNodeModel,
                                 List<ArticleStructModel> articleNodeModelList,
                                 String data) {
    DataField dataField = new DataField();
    dataField.field = articleNodeModelList.get(articleNodeModel.getLevel() - 1).getTableField();
    dataField.data = data.substring(articleNodeModel.getStart(), articleNodeModel.getEndContext());
    return dataField;

  }


  private String getSql(List<DataField> dataFieldList, String tableName) {
    String sql = null;

    sql = "insert into " + tableName;
    String fields = "";
    String data = "";
    for (DataField dataField : dataFieldList) {
      fields += dataField.field + " ,";
      data += "'" + dataField.data + "' ,";
    }
    fields = fields.substring(0, fields.length() - 1);
    data = data.substring(0, data.length() - 1);

    sql += " ( " + fields + " ) " + " value " + " ( " + data + " ) ;";

    return sql;
  }

  private class DataField {

    String data;

    String field;
  }

}
