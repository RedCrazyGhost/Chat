package org.redcrazyghost.Server.Tool;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.redcrazyghost.Server.ServerRun;
import java.lang.String;

import java.sql.*;
import java.util.Scanner;

/**
 * 数据库
 *  MySQL version: 8.0.25
 * @author wenxingzhan
 * @date 2021/11/28 23:53
 **/
public class DataBase {
    public static boolean flag=true;
   private Connection connection;
   private  final Logger log= LogManager.getLogger(DataBase.class);

    public DataBase() {
        try {
//            数据库连接
            this.connection = DriverManager.getConnection(
                    ServerRun.CONFIG.get("database").get("url"),
                    ServerRun.CONFIG.get("database").get("username"),
                    ServerRun.CONFIG.get("database").get("password"));
        }catch (SQLException e){
            flag=false;
//            日志打印
            log.error("数据库连接失败,请修改Server.yaml配置！");
        }
    }

    public Connection getConnection() {
        return connection;
    }



}
