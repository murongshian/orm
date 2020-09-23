package com.zwd.orm.sqlSession;

import com.zwd.orm.bean.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MyExcutor implements Excutor {

    private MyConfiguration xmlConfiguration = new MyConfiguration();

    @Override
    public <Y> Y query(String sql, Object parameter) {
        Connection connection = getConnection();
        ResultSet set = null;
        PreparedStatement pre = null;
        try {
            User u = new User();
            if(connection != null){
                pre = connection.prepareStatement(sql);
                //设置参数
                pre.setString(1, parameter.toString());
                set = pre.executeQuery();
                //遍历结果集
                while (set.next()) {
                    u.setId(set.getString(1));
                    u.setUsername(set.getString(2));
                    u.setPassword(set.getString(3));
                }
            }
            return (Y) u;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (set != null) {
                    set.close();
                }
                if (pre != null) {
                    pre.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return null;
    }

    private Connection getConnection() {
        try {
            return xmlConfiguration.build("config.xml");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}