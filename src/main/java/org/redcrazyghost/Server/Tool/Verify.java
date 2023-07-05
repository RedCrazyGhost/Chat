package org.redcrazyghost.Server.Tool;

/**
 * 验证身份
 *
 * @author wenxingzhan
 * @date 2021/12/03 00:43
 **/
public class Verify {
    public String TOKEN(String username){
        char[] chars=username.toCharArray();
        for (int i = 0; i <chars.length/2 ; i++) {
            char c=chars[i];
            chars[i]=chars[chars.length-i-1];
            chars[chars.length-i-1]=c;
        }
        return new String(chars);
    }
}
