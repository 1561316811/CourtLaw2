package com.cyl.court.control.sql;

import com.cyl.court.anotation.Bean;
import com.cyl.court.beanfactory.BeanFactory;
import com.cyl.court.control.sql.ConnSqlServerResolver;

import java.net.ConnectException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Bean
public class SqlTableResolver {

    private ConnSqlServerResolver connSqlServerResolver
        = BeanFactory.getBean(ConnSqlServerResolver.class);

    private List<String> listTableName = new ArrayList<>();

    public List<String> getListTableName() {
        return listTableName;
    }

    /**
     * 从数据库中获取所有的表名
     * @return
     */
    public List<String> getAllTableName() {
        //1. JDBC连接MYSQL的代码很标准。

        Connection conn = null;
        try {
            conn = connSqlServerResolver.getConnection();
        } catch (ConnectException e) {
            e.printStackTrace();
        }
        //先清空list中的值
        listTableName.clear();
        //2. 下面就是获取表的信息。
        DatabaseMetaData m_DBMetaData = null;
        ResultSet tableRet = null;
        try {
            m_DBMetaData = conn.getMetaData();
            tableRet = m_DBMetaData.getTables(null, "%", "%", new String[]{"TABLE"});
            while (tableRet.next()) {
                System.out.println(tableRet.getString("TABLE_NAME"));
                listTableName.add(tableRet.getString("TABLE_NAME"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listTableName;
/*
//4. 提取表内的字段的名字和类型
        String columnName;
        String columnType;
        ResultSet colRet = m_DBMetaData.getColumns(null,"%", m_TableName,"%");
        while(colRet.next()) {
　　columnName = colRet.getString("COLUMN_NAME");
　　columnType = colRet.getString("TYPE_NAME");
　　int datasize = colRet.getInt("COLUMN_SIZE");
　　int digits = colRet.getInt("DECIMAL_DIGITS");
　　int nullable = colRet.getInt("NULLABLE");
　　System.out.println(columnName+" "+columnType+" "+datasize+" "+digits+" "+ nullable);*/
//        }

/*JDBC里面通过getColumns的接口，实现对字段的查询。跟getTables一样，"%"表示所有任意的（字段），而m_TableName就是数据表的名字。

getColumns的返回也是将所有的字段放到一个类似的内存中的表，而COLUMN_NAME就是字段的名字，TYPE_NAME就是数据类型，比如"int","int unsigned"等等，COLUMN_SIZE返回整数，就是字段的长度，比如定义的int(8)的字段，返回就是8，最后NULLABLE，返回1就表示可以是Null,而0就表示Not Null。*/
    }

}
