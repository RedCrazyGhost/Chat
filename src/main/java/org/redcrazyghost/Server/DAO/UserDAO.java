package org.redcrazyghost.Server.DAO;

import org.redcrazyghost.Server.OBJ.User;
import org.redcrazyghost.Server.Tool.DataBase;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

/**
 * UserDAO
 *
 * @author wenxingzhan
 * @date 2021/11/29 12:31
 **/
public class UserDAO {

    private DataBase dataBase=new DataBase();


    public User getUser(String username) throws SQLException {
        String sql="select * from user where username=?";
        PreparedStatement preparedStatement =dataBase.getConnection().prepareStatement(sql);
        preparedStatement.setString(1,username);
       ResultSet resultSet= preparedStatement.executeQuery();
       User user=null;
       if (resultSet.next()) {
           user = new User(username, resultSet.getString("password"), resultSet.getString("email"));
       }
        preparedStatement.close();
        return user;
    }

    public String addUser(String username,String password,String email) throws SQLException {
        String sql="insert into user values (?,?,?)";
        PreparedStatement preparedStatement =dataBase.getConnection().prepareStatement(sql);
        preparedStatement.setString(1,username);
        preparedStatement.setString(2,password);
        preparedStatement.setString(3,email);

        String result = null;
        try {
            if( preparedStatement.executeUpdate()==1){
                result ="操作成功";
            }
        }catch (SQLIntegrityConstraintViolationException e){
            result ="操作失败，失败原因：存在相同主键！";
        }
        preparedStatement.close();
        return result;
    }

    public String delUser(String username) throws SQLException {
        String sql="delete from user where username=?";
        PreparedStatement preparedStatement =dataBase.getConnection().prepareStatement(sql);
        preparedStatement.setString(1,username);
        String result=null;
        if(preparedStatement.executeUpdate()==1){
            result="操作成功";
        }else{
            result="操作失败，失败原因：当前用户已不存在！";
        }

        preparedStatement.close();
        return result;
    }

    public String updateUser(String username,String password) throws SQLException {
        String sql="update user set password=? where username=?";
        PreparedStatement preparedStatement =dataBase.getConnection().prepareStatement(sql);
        preparedStatement.setString(1,password);
        preparedStatement.setString(2,username);

        String result=null;
        if(preparedStatement.executeUpdate()==1){
            result="操作成功";
        }else{
            result="操作失败，失败原因：当前用户已不存在！";
        }
        preparedStatement.close();
        return result;
    }

}
