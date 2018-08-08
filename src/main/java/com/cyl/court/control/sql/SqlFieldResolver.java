package com.cyl.court.control.sql;

import com.cyl.court.anotation.Resolver;
import com.cyl.court.control.basic.BasicResolver;
import com.cyl.court.event.Callback;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Resolver
public class SqlFieldResolver implements BasicResolver {

  private Map<String, TableDesc> tableDescMap = new HashMap<>();

  public TableDesc getFieldDesc(String tableName, Callback callback) {

    if (tableDescMap.get(tableName) == null) {
      try {
        getFieldByTableName(tableName);
      } catch (SQLException e) {
        e.printStackTrace();
        callback.fail("字段获取失败！");
      }
    }
    return tableDescMap.get(tableName);

  }

  private TableDesc getFieldByTableName(String tableName) throws SQLException {

    Connection conn = null;
    conn = DBUtil.getConnection();

    //2. 下面就是获取表中字段的定义。
    DatabaseMetaData m_DBMetaData = null;
    TableDesc fieldDesc = null;
    m_DBMetaData = conn.getMetaData();
    ResultSet colRet = m_DBMetaData.getColumns(null, "", tableName, "");

    fieldDesc = tableDescMap.get(tableName);
    if (fieldDesc == null) {
      tableDescMap.put(tableName, new TableDesc());
    }
    fieldDesc = tableDescMap.get(tableName);
    fieldDesc.setName(tableName);
    while (colRet.next()) {
      String columnName = colRet.getString("COLUMN_NAME");
      String columnType = colRet.getString("TYPE_NAME");
      int dataSize = colRet.getInt("COLUMN_SIZE");
      int digits = colRet.getInt("DECIMAL_DIGITS");
      int nullable = colRet.getInt("NULLABLE");

      fieldDesc.getFieldDescList().add(new FieldDesc(columnName, columnType, null, dataSize, nullable));
      System.out.println(columnName + " " + columnType + " " + dataSize + " " + digits + " " + nullable);
    }


    DBUtil.closeConn(conn);
    return fieldDesc;

  }

  @Override
  public void refresh() throws SQLException {
    for (String tableName : tableDescMap.keySet()
        ) {
      getFieldByTableName(tableName);
    }
  }
}
