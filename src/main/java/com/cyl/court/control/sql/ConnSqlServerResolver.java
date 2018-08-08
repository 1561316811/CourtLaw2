package com.cyl.court.control.sql;

import com.cyl.court.anotation.Resolver;
import com.cyl.court.beanfactory.BeanFactory;
import com.cyl.court.config.CourtAutoPropertyConfig;
import com.cyl.court.control.basic.Persistence;
import com.cyl.court.event.Callback;
import com.cyl.court.model.SqlConnDataModel;
import com.cyl.court.util.JsonIO;
import com.cyl.court.util.JsonUtil;

import java.io.File;
import java.io.IOException;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.util.Properties;


@Resolver
public class ConnSqlServerResolver implements Persistence {

  public SqlConnDataModel getCurConnData() {
    if(curConnData == null){
      curConnData = JsonUtil.fromJson(SqlConnDataModel.class,read());
    }
    return curConnData;
  }

  private SqlConnDataModel curConnData;

  public void testConnSqlServer(File fileJar, String user, String pwd,
                                String url, String driverName, Callback callback) {
    Connection conn = null;
    try {
      conn = connSqlServer(fileJar, user, pwd, url, driverName);
      if (!conn.isClosed()) {
        callback.success("连接成功！");
        conn.close();
      }
    } catch (MalformedURLException | ClassNotFoundException | IllegalAccessException | InstantiationException
        | SQLException | NullPointerException e) {
      e.printStackTrace();
      callback.fail("Error " + e.getMessage() + " 连接失败！");
    } finally {
      if (conn != null) {
        try {
          conn.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    }
  }

  public void connSqlServer(File fileJar, String user, String pwd,
                                  String url, String driverName, Callback callback) {
    Connection connection = null;
    try {
      connection = connSqlServer(fileJar, user, pwd, url, driverName);
    } catch (Exception e) {
      e.printStackTrace();
      callback.fail("连接失败");
    }
    callback.success("连接成功");
    if(connection != null){
      try {
        connection.close();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
  }

  Connection connSqlServer(SqlConnDataModel sqlConnDataModel) {
    try {
      return connSqlServer(new File(sqlConnDataModel.getJarPath()), sqlConnDataModel.getUserName(),
          sqlConnDataModel.getPassword(), sqlConnDataModel.getUrl(), sqlConnDataModel.getDriverName());
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (InstantiationException e) {
      e.printStackTrace();
    } catch (MalformedURLException e) {
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  private Connection connSqlServer(File fileJar, String user, String pwd,
                                   String url, String driverName)
      throws IllegalAccessException, InstantiationException, MalformedURLException, ClassNotFoundException, SQLException {

    if(fileJar == null){
      throw new RuntimeException("fileJar can not be null !");
    }

    SqlConnDataModel sqlConnDataModel = new SqlConnDataModel();
    sqlConnDataModel.setDriverName(driverName);
    sqlConnDataModel.setJarPath(fileJar.getAbsolutePath());
    sqlConnDataModel.setPassword(pwd);
    sqlConnDataModel.setUserName(user);
    sqlConnDataModel.setUrl(url);
    curConnData = sqlConnDataModel;

    write(curConnData);

    URL r = fileJar.toURI().toURL();
    URLClassLoader myClassLoader = new URLClassLoader(
        new URL[]{fileJar.toURI().toURL()}, Thread.currentThread()
        .getContextClassLoader());

    Driver d = (Driver) myClassLoader.loadClass(driverName).newInstance();
    Properties props = new Properties();
    props.setProperty("user", user);
    props.setProperty("password", pwd);

    return d.connect(url, props);
  }

  CourtAutoPropertyConfig courtAutoPropertyConfig = BeanFactory.getBean(CourtAutoPropertyConfig.class);

  @Override
  public String read() {

    String data = null;
    try {
      data = JsonIO.readStream(courtAutoPropertyConfig.getSqlConnDataPath());
      this.curConnData = JsonUtil.fromJson(SqlConnDataModel.class, data);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return data;
  }

  @Override
  public void write(Object dataModel) {
    try {
      JsonIO.writeStream(JsonUtil.toJsonDisableHtmlEscaping(dataModel), courtAutoPropertyConfig.getSqlConnDataPath());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
