package org.redcrazyghost.Client.VIEW;

import com.alibaba.fastjson.JSONObject;
import org.redcrazyghost.Client.ClientRun;
import org.redcrazyghost.Client.Tool.Response;
import org.redcrazyghost.Client.Tool.VerifyStr;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import static org.redcrazyghost.Client.ClientRun.encoding;

/**
 * 首页视图
 *
 * @author wenxingzhan
 * @date 2021/12/02 15:17
 **/
public class HomeView {

    public static int code=0;
    private Scanner scanner;
    private VerifyStr verifyStr;

    public HomeView(Scanner scanner) throws IOException {
        this.scanner=scanner;
        this.verifyStr=new VerifyStr();

    }
    public void run() throws IOException, InterruptedException {
       run: while(true) {
           Thread.sleep(500);
           switch (code) {
               case 0:
                   System.out.println("欢迎使用码农聊天室\n（请输入下面的选项操作）");
                   System.out.println("1.人生初始化\t2.上号\t3.找回史诗级装备\t4.跑路");
                   switch (verifyStr.verify(scanner.next())) {
                    case 1:
                       ClientRun.client.write(ByteBuffer.wrap(addUser(this.scanner).getBytes(encoding)));
                       code=3;
                       break;
                    case 2:
                        ClientRun.client.write(ByteBuffer.wrap(loginUser(this.scanner).getBytes(encoding)));
                        code=3;
                        break;
                    case 3:
                        ClientRun.client.write(ByteBuffer.wrap(getPassword(this.scanner).getBytes(encoding)));
                        code=3;
                        break;
                    case 4:
                        code=2;
                        ClientRun.client.close();
                        System.exit(0);
                        break;
                    default:
                        continue;
                }break;
               case 1:
                   new SystemView(scanner).run();break;
               case 2:
                        break run;
               case 3:continue;
           }

        }
    }
    private String addUser(Scanner scanner){
        System.out.println("请输入用户名(可以使用中文、英文、数值、下划线 长度1到10):");
        String username=scanner.next();
        System.out.println("请输入密码(可以使用中文、英文、数值、下划线 长度6到18):");
        String password=scanner.next();
        System.out.println("请输入邮箱:");
        String email=scanner.next();


        JSONObject request=new JSONObject();
        JSONObject head=new JSONObject();
        JSONObject body=new JSONObject();

        head.put("PATH","Home-addUser");
        head.put("TOKEN", ClientRun.TOKEN);
        body.put("username",username);
        body.put("password",password);
        body.put("email",email);
        request.put("head",head);
        request.put("body",body);
        head.put("ENCODING",encoding);
        return request.toJSONString();
    }


    private String loginUser(Scanner scanner){
        System.out.println("请输入用户名：");
        String username=scanner.next();
        System.out.println("请输入密码：");
        String password=scanner.next();

//        发送json给SERVER
        this.code=2;

        JSONObject request=new JSONObject();
        JSONObject head=new JSONObject();
        JSONObject body=new JSONObject();

        head.put("PATH","Home-login");
        head.put("TOKEN",ClientRun.TOKEN);
        body.put("username",username);
        body.put("password",password);
        request.put("head",head);
        request.put("body",body);
        head.put("ENCODING",encoding);

        return request.toJSONString();
    }

    private String getPassword(Scanner scanner){
        System.out.println("请输入用户名：");
        String username=scanner.next();
        System.out.println("请输入邮箱：");
        String email=scanner.next();

        JSONObject request=new JSONObject();
        JSONObject head=new JSONObject();
        JSONObject body=new JSONObject();

        head.put("PATH","Home-getPassword");
        head.put("TOKEN",ClientRun.TOKEN);
        body.put("username",username);
        body.put("email",email);
        request.put("head",head);
        request.put("body",body);
        head.put("ENCODING",encoding);

        return request.toJSONString();
    }


}
