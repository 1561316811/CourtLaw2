package com.cyl.court.control.sql;

import com.cyl.court.beanfactory.BeanFactory;

import java.sql.Connection;
import java.sql.SQLException;

public class DBUtil {

  static ConnSqlServerResolver connSqlServerResolver = BeanFactory.getBean(ConnSqlServerResolver.class);

  public static Connection getConnection(){
    return connSqlServerResolver.connSqlServer(connSqlServerResolver.getCurConnData());
  }

  public static void closeConn(Connection conn){
    try {
      conn.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

}
