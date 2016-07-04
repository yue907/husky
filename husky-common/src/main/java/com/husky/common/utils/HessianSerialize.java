package com.husky.common.utils;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import com.caucho.hessian.io.SerializerFactory;

import java.io.*;

/**
 * 借助hessian实现序列化
 * Created by google on 16/6/24.
 */
public class HessianSerialize {
    private static SerializerFactory serializerFactory = new SerializerFactory();
    public static byte[] serialize(Object obj) throws Exception {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        Hessian2Output hout = new Hessian2Output(os);
        hout.setSerializerFactory(serializerFactory);//这里要设置serializerFactory，否则影响性能
        hout.writeObject(obj);
        hout.flush();
        byte[] bytes = os.toByteArray();
        hout.close();
        os.close();
        return bytes;
    }
    public static Object decode(byte[] bytes) throws Exception{
        if(null == bytes) throw new NullPointerException();
        ByteArrayInputStream is = new ByteArrayInputStream(bytes);
        Hessian2Input hin = new Hessian2Input(is);
        hin.setSerializerFactory(serializerFactory);
        Object object = hin.readObject();
        hin.close();
        return object;
    }
}
