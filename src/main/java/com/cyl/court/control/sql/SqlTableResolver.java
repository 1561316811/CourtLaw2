package com.cyl.court.control.sql;

import com.cyl.court.anotation.Resolver;
import com.cyl.court.control.basic.BasicResolver;
import com.cyl.court.event.Callback;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Resolver
public class SqlTableResolver implements BasicResolver {

  private List<String> tableNameList = new ArrayList<>();

  public List<String> getTablesName(Callback callback) {
    if(tableNameList.isEmpty()) {
      try {
        refresh();
      } catch (IOException e) {
        e.printStackTrace();
        callback.fail("连接数据失败！");
      }
    }
    return tableNameList;
  }


  /**
   * 从数据库中获取所有的表名
   *
   * @return
   */
  private List<String> refreshTables() throws IOException {
    //1. JDBC连接MYSQL的代码很标准。

    Connection conn = null;
    conn = DBUtil.getConnection();

    if(conn == null){
      throw new IOException("数据库连接失败");
    }

    //先清空list中的值
    tableNameList.clear();
    //2. 下面就是获取表的信息。
    DatabaseMetaData m_DBMetaData = null;
    ResultSet tableRet = null;
    try {

      m_DBMetaData = conn.getMetaData();
      tableRet = m_DBMetaData.getTables(null, "%", "%", new String[]{"TABLE"});
      while (tableRet.next()) {
        System.out.println(tableRet.getString("TABLE_NAME"));
        tableNameList.add(tableRet.getString("TABLE_NAME"));
      }

    } catch (SQLException e) {
      e.printStackTrace();
    }

    DBUtil.closeConn(conn);
    return tableNameList;
  }

  @Override
  public void refresh() throws IOException {
    refreshTables();
  }
}
