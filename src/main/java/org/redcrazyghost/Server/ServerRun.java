package org.redcrazyghost.Server;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.redcrazyghost.Client.Tool.VerifyStr;
import org.redcrazyghost.Server.Tool.DataBase;
import org.redcrazyghost.Server.Tool.Verify;
import org.yaml.snakeyaml.Yaml;


import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.BindException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * 主启动类
 *
 * @author wenxingzhan
 * @date 2021/11/28 21:44
 **/
public class ServerRun {
    /*
反射
 Method get = Class.forName("org.redcrazyghost.Server.Service."+path[0]).getMethod(path[1],String.class,String.class);
 out.println(get.invoke(Class.forName("org.redcrazyghost.Server.Service."+path[0]).newInstance(),request.getBody().values().toArray()));

 */
    private static final Logger log= LogManager.getLogger(ServerRun.class);

    public static  Map<String,Map<String,String>> CONFIG=new Yaml().load(ServerRun.class.getClassLoader().getResourceAsStream("Server.yaml"));

    public static Map<String,SocketChannel> map=new HashMap<>();

    public static void main(String[] args) throws IOException, ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        Scanner scanner=new Scanner(System.in);
        try {
        config:while(true) {
            System.out.println("(只支持MySQL！只能输入数字！)\n1.默认配置\t2.手动输入");
            System.out.println("请输入操作：");
                switch (scanner.nextInt()) {
                case 1:
                    log.info("默认配置启动！");
                    break config;
                case 2:
                    log.info("手动输入配置启动！");
                    System.out.println("请输入数据库连接url（characterEncoding=UTF-8）：");
                    CONFIG.get("database").put("url", scanner.next());
                    System.out.println("请输入数据库用户名：");
                    CONFIG.get("database").put("username", scanner.next());
                    System.out.println("请输入数据库密码：");
                    CONFIG.get("database").put("password", scanner.next());
                    log.info("手动配置启动！");
                    break config;
                default:
                    System.out.println("请输入正确的操作数！");
                    break;
                }
            }
        }catch (Exception e){
            log.warn("输入内容错误！尝试启用默认配置！");
            scanner.nextLine();
        }
//        测试连接
        new DataBase();
        if (DataBase.flag) {


            Selector selector = Selector.open();
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            System.out.println("请输入端口:");
            try {
                serverSocketChannel.bind(new InetSocketAddress(scanner.nextInt()));
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);


            log.info("码农聊天室服务器启动！");

            while (selector.select() > 0) {
                Set<SelectionKey> keys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = keys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    if (key.isAcceptable()) {
                        log.info(key + "：连接成功");
                        SocketChannel client = serverSocketChannel.accept();
                        client.configureBlocking(false);
                        client.register(selector, SelectionKey.OP_READ);

                    } else if (key.isReadable()) {
                        SocketChannel client = (SocketChannel) key.channel();
                        ByteBuffer buffer = ByteBuffer.allocate(102400);
                        try {
                            client.read(buffer);
                        } catch (IOException e) {
                            if(map.size()!=0) {
                                for (String username : map.keySet()) {
                                    if (map.get(username).equals(client)) {
                                        map.remove(username);
                                        log.warn("[" + username + "]:强制关闭!");
                                    }
                                }
                            }
                        }
                        String jsonstr = new String(buffer.array()).trim();
                        if (!jsonstr.isEmpty()) {
                            String[] strings=jsonstr.split("\\|");
                                if (JSONValidator.from(strings[0]).validate()){
                                String ClientENCODING=JSONObject.parseObject(strings[0]).getJSONObject("head").getString("ENCODING");
                                jsonstr = new String(buffer.array(),ClientENCODING).trim();
                                strings=jsonstr.split("\\|");
                                for (String str:strings) {
                                    if (JSONValidator.from(str).validate()) {
                                        log.info(client + ":" + str);
                                        String[] paths = JSONObject.parseObject(str).getJSONObject("head").getString("PATH").split("-");

                                        // 反射 动态调用类的方法
                                        Method method = Class.forName("org.redcrazyghost.Server.Service." + paths[0]).getMethod(paths[1], String.class);
                                        String responsestr = (String) method.invoke(Class.forName("org.redcrazyghost.Server.Service." + paths[0]).newInstance(), str);
                                        String token = JSON.parseObject(responsestr).getJSONObject("head").getString("TOKEN");
                                        String to = JSON.parseObject(responsestr).getJSONObject("head").getString("TO");
                                        if (paths[1].equals("login") && !token.equals("")) {
                                            ServerRun.map.put(new Verify().TOKEN(token), client);
                                        }


                                        if (to == null) {
                                            client.write(ByteBuffer.wrap(responsestr.getBytes(ClientENCODING)));
                                        } else if (!to.equals("all") && !to.equals("")) {
                                            if (map.containsKey(to)) {
                                                map.get(to).write(ByteBuffer.wrap(responsestr.getBytes(ClientENCODING)));
                                            } else {
                                                JSONObject response = JSONObject.parseObject(responsestr);
                                                JSONObject body = response.getJSONObject("body");
                                                body.put("message", "当前用户不在线！");
                                                client.write(ByteBuffer.wrap(response.toJSONString().getBytes(ClientENCODING)));
                                            }
                                        } else if (to.equals("all")) {
                                            for (SocketChannel toClient : map.values()) {
                                                toClient.write(ByteBuffer.wrap(responsestr.getBytes(ClientENCODING)));
                                            }
                                        }
                                    }
                                }
                            }else{
                                    log.warn("TCP/IP请求");
                                }
                        }
                    }
                    iterator.remove();
                }
            }
        }catch (BindException e){
                log.error("端口占用！");
            }
        }
    }
}
