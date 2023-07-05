package org.redcrazyghost.Client.Tool;

/**
 * 检测输入字符串
 *
 * @author wenxingzhan
 * @date 2021/12/02 16:27
 **/
public class VerifyStr {
    public int verify(String str){
        int i=0;
        try {
            i=Integer.parseInt(str);
        }catch (NumberFormatException e){
            System.out.println("请输入正确操作号！");
        }
        return i;
    }
}
