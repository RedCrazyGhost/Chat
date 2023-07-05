package org.redcrazyghost.Client.VIEW;

import com.alibaba.fastjson.JSONObject;
import org.redcrazyghost.Client.ClientRun;
import org.redcrazyghost.Client.Tool.VerifyStr;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Scanner;

import static org.redcrazyghost.Client.ClientRun.encoding;

/**
 * 系统页面
 *
 * @author wenxingzhan
 * @date 2021/12/02 16:22
 **/
public class SystemView {
    public static int code=0;
    private Scanner scanner;
    private VerifyStr verifyStr;


    public SystemView(Scanner scanner){
        this.scanner=scanner;
        this.verifyStr=new VerifyStr();
    }

    public void run() throws IOException, InterruptedException {
      run:  while(true){
          Thread.sleep(500);
          switch (code) {
                case 0:
                System.out.println("1.查看线上码友\t2.码友病情交流\t3.大型病情研讨会\t4.修改史诗级装备\t5.解千愁\t6.下号");
                switch (verifyStr.verify(scanner.next())) {
                    case 1:
                        ClientRun.client.write(ByteBuffer.wrap(getOnline().getBytes(encoding)));
                        break;
                    case 2:
                        ClientRun.client.write(ByteBuffer.wrap(OnetoOne(this.scanner).getBytes(encoding)));
                        break;
                    case 3:
                        ClientRun.client.write(ByteBuffer.wrap(OnetoMore(this.scanner).getBytes(encoding)));
                        break;
                    case 4:
                        ClientRun.client.write(ByteBuffer.wrap(changePassword(this.scanner).getBytes(encoding)));
                        break;
                    case 5:
                        ClientRun.client.write(ByteBuffer.wrap((deleteUser(this.scanner)).getBytes(encoding)));
                        Thread.sleep(500);
                    case 6:
                        ClientRun.client.write(ByteBuffer.wrap(offLine().getBytes(encoding)));
                        ClientRun.TOKEN = "";
                        HomeView.code = 0;
                        code=3;
                        break;
                    default:
                }
                case 1:new HomeView(scanner).run(); break run;
                case 3:continue;
            }
        }
    }
    private String offLine(){
        JSONObject request=new JSONObject();
        JSONObject head=new JSONObject();
        JSONObject body=new JSONObject();

        head.put("PATH","System-offLine");
        head.put("TOKEN", ClientRun.TOKEN);
        request.put("head",head);
        request.put("body",body);
        head.put("ENCODING",encoding);

        return request.toJSONString();

    }

    private String changePassword(Scanner scanner){
        System.out.println("请输入旧密码：");
        String oldpassword=scanner.next();
        System.out.println("请输入新密码：");
        String newpassword=scanner.next();


        JSONObject request=new JSONObject();
        JSONObject head=new JSONObject();
        JSONObject body=new JSONObject();

        head.put("PATH","Home-changePassword");
        head.put("TOKEN", ClientRun.TOKEN);
        body.put("oldpassword",oldpassword);
        body.put("newpassword",newpassword);
        request.put("head",head);
        request.put("body",body);
        head.put("ENCODING",encoding);

        return request.toJSONString();
    }
    private String getOnline(){
        JSONObject request=new JSONObject();
        JSONObject head=new JSONObject();
        JSONObject body=new JSONObject();

        head.put("PATH","System-getOnline");
        head.put("TOKEN", ClientRun.TOKEN);
        request.put("head",head);
        request.put("body",body);
        head.put("ENCODING",encoding);

        return request.toJSONString();
    }
    private String OnetoOne(Scanner scanner){
        JSONObject request=new JSONObject();
        JSONObject head=new JSONObject();
        JSONObject body=new JSONObject();

        System.out.println("请输入发送对象：");
        String to=scanner.next();
        System.out.println("请输入发送内容：");
        scanner.nextLine();
        String message=scanner.nextLine();

        head.put("PATH","System-OnetoOne");
        head.put("TOKEN", ClientRun.TOKEN);
        body.put("to",to);
        body.put("message",message);
        request.put("head",head);
        request.put("body",body);
        head.put("ENCODING",encoding);

        return request.toJSONString();
    }

    private String deleteUser(Scanner scanner){
        JSONObject request=new JSONObject();
        JSONObject head=new JSONObject();
        JSONObject body=new JSONObject();

        System.out.println("请输入密码：");
        String password=scanner.next();

        body.put("password",password);
        head.put("PATH","System-deleteUser");
        head.put("TOKEN", ClientRun.TOKEN);
        request.put("head",head);
        request.put("body",body);
        head.put("ENCODING",encoding);

        return request.toJSONString();
    }

    private String OnetoMore(Scanner scanner){
        JSONObject request=new JSONObject();
        JSONObject head=new JSONObject();
        JSONObject body=new JSONObject();

        System.out.println("请输入群体发送内容：");
        scanner.nextLine();
        String message=scanner.nextLine();

        body.put("message",message);
        body.put("to","all");
        head.put("PATH","System-OnetoMore");
        head.put("TOKEN", ClientRun.TOKEN);
        request.put("head",head);
        request.put("body",body);
        head.put("ENCODING",encoding);

        return request.toJSONString();
    }

}
