package org.redcrazyghost.Server.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.redcrazyghost.Client.ClientRun;
import org.redcrazyghost.Server.DAO.UserDAO;
import org.redcrazyghost.Server.OBJ.User;
import org.redcrazyghost.Server.ServerRun;
import org.redcrazyghost.Server.Tool.Verify;

import java.sql.SQLException;

/**
 * 系统
 *
 * @author wenxingzhan
 * @date 2021/11/29 19:17
 **/
public class System {
    private static final Logger log= LogManager.getLogger(System.class);

    private UserDAO userDAO=new UserDAO();

    public String getOnline(String jsonstr) throws SQLException {
        JSONObject response= JSON.parseObject(jsonstr);
        JSONObject head=response.getJSONObject("head");
        JSONObject body=response.getJSONObject("body");


        String username=new Verify().TOKEN(head.getString("TOKEN"));

        if (username.equals("")){
            body.put("message","身份验证失败！");body.put("code","300");
        }else {
            User user = userDAO.getUser(username);
            if (user == null) {
                body.put("message", "无效验证！");body.put("code","300");
            } else {
                StringBuilder message=new StringBuilder();
                message.append("当前在线人员:");
                for(String name:ServerRun.map.keySet()){
                    message.append("[").append(name).append("]");
                }
                body.put("message",message.toString());
                body.put("code","200");
            }

        }
        head.put("PATH","Client");
        response.put("head",head);
        response.put("body",body);
        return response.toString();
    }

    public String OnetoOne(String jsonstr) throws SQLException {
        JSONObject response= JSON.parseObject(jsonstr);
        JSONObject head=response.getJSONObject("head");
        JSONObject body=response.getJSONObject("body");
        String username=new Verify().TOKEN(head.getString("TOKEN"));

        if (username.equals("")){
            body.put("message","身份验证失败！");body.put("code","300");
        }else {
            User user = userDAO.getUser(username);
            if (user == null) {
                body.put("message", "无效验证！");body.put("code","300");
            } else {
                head.put("TO",body.getString("to"));
                body.remove("to");
                body.put("code","200");
                body.put("message","[病情交流]"+username+":"+body.getString("message"));
            }
        }

        head.put("PATH","Client");
        response.put("head",head);
        response.put("body",body);
        return response.toString();
    }
    //    用户注销
    public String deleteUser(String jsonstr) throws SQLException {
        JSONObject response=JSON.parseObject(jsonstr);
        JSONObject head=response.getJSONObject("head");
        JSONObject body=response.getJSONObject("body");

        String username=new Verify().TOKEN(head.getString("TOKEN"));
        String password=body.getString("password");

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
                if (password.equals(user.getPassword())){
                    userDAO.delUser(username);
                    body.put("message","删除成功！");
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
    public String OnetoMore(String jsonstr) throws SQLException {
        JSONObject response= JSON.parseObject(jsonstr);
        JSONObject head=response.getJSONObject("head");
        JSONObject body=response.getJSONObject("body");
        String username=new Verify().TOKEN(head.getString("TOKEN"));

        if (username.equals("")){
            body.put("message","身份验证失败！");body.put("code","300");
        }else {
            User user = userDAO.getUser(username);
            if (user == null) {
                body.put("message", "无效验证！");body.put("code","300");
            } else {
                head.put("TO",body.getString("to"));
                body.remove("to");
                body.put("code","300");
                body.put("message","[大型病情研讨会]"+username+":"+body.getString("message"));
            }
        }

        head.put("PATH","Client");
        response.put("head",head);
        response.put("body",body);
        return response.toString();
    }

    public String offLine(String jsonstr) throws SQLException {
        JSONObject response= JSON.parseObject(jsonstr);
        JSONObject head=response.getJSONObject("head");
        JSONObject body=response.getJSONObject("body");
        String username=new Verify().TOKEN(head.getString("TOKEN"));

        if (username.equals("")){
            body.put("message","身份验证失败！");body.put("code","300");
        }else {
            if (!ServerRun.map.containsKey(username)) {
                body.put("message", "已不在线！");body.put("code","300");
            } else {
                log.info("["+username+"]:下线!");
                ServerRun.map.remove(username);
                body.put("message","下号成功!");body.put("code","200");
            }
        }
        head.put("TOKEN","");
        head.put("PATH","Client");
        response.put("head",head);
        response.put("body",body);
        return response.toString();
    }
}
