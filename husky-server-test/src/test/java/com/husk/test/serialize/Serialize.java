package com.husk.test.serialize;

import com.husk.test.bean.User;
import org.apache.commons.lang3.StringUtils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by google on 16/6/3.
 */
public class Serialize {
    public void serialize(Object obj) throws Exception {
        if(obj==null) throw new NullPointerException();
        FileOutputStream fos = new FileOutputStream("/Users/google/Desktop/user.info");
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.close();
        fos.close();
        oos.writeObject(obj);
        return ;
    }
    public Object decode(String fileName) throws Exception{
        if(StringUtils.isBlank(fileName)) throw new NullPointerException();
        FileInputStream fis = new FileInputStream(fileName);
        ObjectInputStream ois = new ObjectInputStream(fis);
        Object obj = ois.readObject();
        ois.close();
        fis.close();
        return obj;
    }

    public static void main(String[] args){
        User user = new User();
        user.setId(9527);
        user.setName("唐伯虎");
        String fileName = "/Users/google/Desktop/user.info";
        Serialize serialize = new Serialize();
        try {
            long start = System.currentTimeMillis();
//            serialize.serialize(user);
            User newUser = (User)serialize.decode(fileName);
            long end =  System.currentTimeMillis();
            System.out.println(newUser.getId()+","+newUser.getName());
            System.out.println(end-start);

        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
