package com.husky.server.test;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

/**
 * Created by google on 16/5/30.
 */
public class Server {
    public static void main(String[] args){
        ClassPathXmlApplicationContext context =new ClassPathXmlApplicationContext("classpath*:config/spring/appcontext-service.xml");
        context.start();

    }
}
