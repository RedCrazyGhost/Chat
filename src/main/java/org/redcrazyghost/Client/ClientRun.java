package org.redcrazyghost.Client;

import org.redcrazyghost.Client.Tool.Response;
import org.redcrazyghost.Client.VIEW.HomeView;

import java.io.IOException;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.NoRouteToHostException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 客户端运行
 *
 * @author wenxingzhan
 * @date 2021/12/01 16:41
 **/
public class ClientRun {
    public static String TOKEN="";

    public static  Thread thread;

    public static SocketChannel client;

    public static String encoding;

    public static void main(String[] args) {
        try {
            encoding=System.getProperty("file.encoding");
            Scanner scanner = new Scanner(System.in,encoding);
            System.out.println("请输入Server的IP地址和端口号(示例: 127.0.0.1:80)：");
            String[] strs=scanner.next().split(":");
            client = SocketChannel.open(new InetSocketAddress(strs[0] , Integer.parseInt(strs[1])));
           thread= new Thread(()->{
               run: while(true) {
                    ByteBuffer buffer = ByteBuffer.allocate(102400);
                    try {
                        client.read(buffer);
                        System.out.println(new Response(new String(buffer.array()).trim()).getMessage());
                        Thread.sleep(1);
                    } catch (InterruptedException  e) {
                        e.printStackTrace();
                    }catch (IOException e){
                        break run;
                    }catch (NullPointerException e){
                        System.out.println("Server服务器跑路！");
                        System.exit(0);
                    }
                }
                });
           thread.start();

            new HomeView(scanner).run();

        }catch (ConnectException e){
            System.out.println("请检测IP地址！");
            System.out.println("服务端不在线！");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException |ArrayIndexOutOfBoundsException e){
            System.out.println("请输入正确的IP地址！");
        }

    }
}