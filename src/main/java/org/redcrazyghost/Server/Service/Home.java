package org.redcrazyghost.Server.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.redcrazyghost.Server.DAO.UserDAO;
import org.redcrazyghost.Server.OBJ.User;
import org.redcrazyghost.Server.ServerRun;
import org.redcrazyghost.Server.Tool.Verify;

import java.sql.SQLException;

/**
 * home主页
 *
 * @author wenxingzhan
 * @date 2021/11/29 18:08
 **/
public class Home {
    private UserDAO userDAO=new UserDAO();

    private static final Logger log= LogManager.getLogger(Home.class);


    //    用户登录
    public String login(String jsonstr) throws SQLException {
        JSONObject response=JSON.parseObject(jsonstr);
        JSONObject head=response.getJSONObject("head");
        JSONObject body=response.getJSONObject("body");

        String username=body.getString("username");
        String password=body.getString("password");

        body.clear();
        User user= userDAO.getUser(username);
       if (user==null){
           body.put("message","用户名错误！");
           head.put("TOKEN","");

       }else if(user.getPassword().equals(password)){
            if (!ServerRun.map.containsKey(username)) {
                log.info("[" + username + "]:上线!");
                body.put("message", "登陆成功！");
                head.put("TOKEN", new Verify().TOKEN(username));
            }else{
                body.put("message", "用户已在线！请稍后尝试！");
                head.put("TOKEN", "");
            }
       }else{
           body.put("message","登陆失败！");
           head.put("TOKEN","");
       }


       head.put("PATH","Client");
        response.put("head",head);
        response.put("body",body);
        return response.toString();
    }


//    注册用户
    public String addUser(String jsonstr) throws SQLException {
        JSONObject response=JSON.parseObject(jsonstr);
        JSONObject head=response.getJSONObject("head");
        JSONObject body=response.getJSONObject("body");

        String username=body.getString("username");
        String password=body.getString("password");
        String email=body.getString("email");

        body.clear();
        if (username==null){
            body.put("message","无效用户名！");        body.put("code","300");

        }else if (password==null){
            body.put("message","无效密码！");        body.put("code","300");

        }else if (email==null){
            body.put("message","无效邮箱！");        body.put("code","300");
        }else {
            User user = userDAO.getUser(username);
            if (user == null) {
                if (!username.matches("^[\\u4E00-\\u9FA5A-Za-z0-9_]{0,10}$")){
                    body.put("message", "账号注册失败，失败原因：用户名不符合要求！");
                    body.put("code","300");

                }else if (!password.matches("^[A-Za-z0-9_]\\w{5,17}$")){
                    body.put("message", "账号注册失败，失败原因：密码不符合要求！");
                    body.put("code","300");

                }else if(!email.matches("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$")){
                    body.put("message", "账号注册失败，失败原因：邮箱不符合要求！");
                    body.put("code","300");
                }else{
                    userDAO.addUser(username, password, email);
                    body.put("message", "账号注册成功");
                    body.put("code","200");
                }
            } else {
                body.put("message", "账号注册失败，失败原因：账号已存在！");
                body.put("code","300");

            }
        }


        head.put("PATH","Client");
        response.put("head",head);
        response.put("body",body);
        return response.toString();

    }



//    修改密码
    public String changePassword(String jsonstr) throws SQLException {
        JSONObject response=JSON.parseObject(jsonstr);
        JSONObject head=response.getJSONObject("head");
        JSONObject body=response.getJSONObject("body");

        String username=new Verify().TOKEN(head.getString("TOKEN"));
        String newpassword=body.getString("newpassword");
        String oldpassword=body.getString("oldpassword");

        body.clear();

        if (username.equals("")){
            body.put("message","身份验证失败！");
            body.put("code","300");
        }else{
            User user=userDAO.getUser(username);
            if(user==null){
                body.put("message","无效验证！");
                body.put("code","300");
            }else{
                if (oldpassword.equals(user.getPassword())){
                    userDAO.updateUser(username,newpassword);
                    body.put("message","修改成功！");
                    body.put("code","200");
                }else{
                    body.put("message","密码错误！");
                    body.put("code","300");
                }
            }
        }



        head.put("PATH","Client");
        response.put("head",head);
        response.put("body",body);
        return response.toString();
    }

//    找回密码
    public String getPassword(String jsonstr) throws SQLException {
        JSONObject response=JSON.parseObject(jsonstr);
        JSONObject head=response.getJSONObject("head");
        JSONObject body=response.getJSONObject("body");

        String username=body.getString("username");
        String email=body.getString("email");

        body.clear();
        User user= userDAO.getUser(username);
        if(user==null){
            body.put("message","请输入正确的用户名！");body.put("code","300");
        }else{
            if(user.getEmai().equals(email)){
                body.put("message","找回密码成功! 密码:"+user.getPassword());body.put("code","200");
            }else{
                body.put("message","身份验证不通过！");body.put("code","300");
            }
        }

        head.put("PATH","Client");
        response.put("head",head);
        response.put("body",body);
        return response.toString();
    }



}
