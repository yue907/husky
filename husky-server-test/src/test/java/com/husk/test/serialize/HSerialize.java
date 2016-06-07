package com.husk.test.serialize;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import com.caucho.hessian.io.SerializerFactory;
import com.husk.test.bean.User;
import org.apache.commons.lang3.StringUtils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by google on 16/6/3.
 */
public class HSerialize {
    private static SerializerFactory serializerFactory = new SerializerFactory();
    public void serialize(Object obj) throws Exception {
        if(obj==null) throw new NullPointerException();
        FileOutputStream fos = new FileOutputStream("/Users/google/Desktop/huser.info");
        Hessian2Output hOutput = new Hessian2Output(fos);
        hOutput.setSerializerFactory(serializerFactory);
        hOutput.writeObject(obj);
        hOutput.close();
        fos.close();
        return ;
    }
    public Object decode(String fileName) throws Exception{
        if(StringUtils.isBlank(fileName)) throw new NullPointerException();
        FileInputStream fis = new FileInputStream(fileName);
        Hessian2Input hInput = new Hessian2Input(fis);
        hInput.setSerializerFactory(serializerFactory);

        Object obj = hInput.readObject();
        hInput.close();
        fis.close();
        return obj;
    }

    public static void main(String[] args){
        User user = new User();
        user.setId(9527);
        user.setName("唐伯虎");
        String fileName = "/Users/google/Desktop/huser.info";
        HSerialize serialize = new HSerialize();
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
