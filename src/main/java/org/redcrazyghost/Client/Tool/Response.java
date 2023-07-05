package org.redcrazyghost.Client.Tool;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.redcrazyghost.Client.ClientRun;
import org.redcrazyghost.Client.VIEW.HomeView;
import org.redcrazyghost.Client.VIEW.SystemView;

/**
 * 返回值处理类
 *
 * @author wenxingzhan
 * @date 2021/12/02 23:39
 **/
public class Response {

    JSONObject head;
    JSONObject body;

    public Response(String jsonstr) {
       JSONObject response= JSON.parseObject(jsonstr);
        this.head= response.getJSONObject("head");
        this.body= response.getJSONObject("body");
        if (ClientRun.TOKEN.length()==0){
            ClientRun.TOKEN=head.getString("TOKEN");
        }
        if (ClientRun.TOKEN.length()!=0){
            HomeView.code=1;
        }else{
            HomeView.code=0;
        }
        if (body.getString("message").equals("下号成功!")){
            SystemView.code=1;
            HomeView.code=0;
        }else{
            SystemView.code=0;
        }
    }

    public String getMessage(){
        return body.getString("message");
    }
}
