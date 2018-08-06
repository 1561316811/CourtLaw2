package com.cyl.court.control.sql;

import com.cyl.court.anotation.Bean;
import com.cyl.court.beanfactory.BeanFactory;
import com.cyl.court.config.CourtAutoFileConfig;
import com.cyl.court.event.Callback;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Objects;
import java.util.Properties;

import javax.swing.AbstractAction;

import info.monitorenter.cpdetector.io.ASCIIDetector;
import info.monitorenter.cpdetector.io.CodepageDetectorProxy;
import info.monitorenter.cpdetector.io.JChardetFacade;
import info.monitorenter.cpdetector.io.ParsingDetector;
import info.monitorenter.cpdetector.io.UnicodeDetector;


@Bean
public class ConnSqlServerResolver {

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    private Connection connection;

    public void testConnSqlServer(File fileJar, String user, String pwd,
                                  String url, String driverName, Callback callback) {

        Connection conn = null;
        try {
            conn = connSqlServer(fileJar, user, pwd, url, driverName);
            if (!conn.isClosed()) {
                callback.success("连接成功！");
                conn.close();
            }
        } catch ( MalformedURLException|ClassNotFoundException|IllegalAccessException|InstantiationException
                | SQLException | NullPointerException e) {
            e.printStackTrace();
            callback.fail("Error " + e.getMessage() + " 连接失败！");
        }  finally {
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
        try {
            connection = connSqlServer(fileJar, user, pwd, url, driverName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Connection connSqlServer(File fileJar, String user, String pwd,
                                     String url, String driverName)
            throws IllegalAccessException, InstantiationException, MalformedURLException, ClassNotFoundException, SQLException {
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



}
