package com.husky.client.test;

import com.husky.server.test.service.EchoService;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by google on 16/5/30.
 */
public class Client {
    public static void main(String[] args){
        ClassPathXmlApplicationContext context =new ClassPathXmlApplicationContext("classpath*:config/spring/appcontext-client.xml");
        EchoService echoService = (EchoService) context.getBean("echoService");
        double ret = echoService.divide(12.4,3.1);
        System.out.println(ret);
    }
}
