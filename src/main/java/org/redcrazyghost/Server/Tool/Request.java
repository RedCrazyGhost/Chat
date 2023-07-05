package org.redcrazyghost.Server.Tool;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONValidator;

import java.util.Map;

/**
 * tcp/ip请求处理
 *
 * @author wenxingzhan
 * @date 2021/12/07 20:06
 **/
public class Request {
    Map<String,String> head=null;
    String body;

    public Request(String str){
        if(str.split("\n").length==1){
            this.body=str;
        }else{
           String[] strs= str.split("\n");
        }
    }
    public String getBody(){
        return this.body;
    }
}
