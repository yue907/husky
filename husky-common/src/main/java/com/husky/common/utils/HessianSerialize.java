package com.husky.common.utils;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import com.caucho.hessian.io.SerializerFactory;

import java.io.*;

/**
 * Created by google on 16/6/24.
 */
public class HessianSerialize {
    private static SerializerFactory serializerFactory = new SerializerFactory();
    public static byte[] serialize(Object obj) throws Exception {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        Hessian2Output hout = new Hessian2Output(os);
        hout.writeObject(obj);
        return os.toByteArray();
    }
    public static Object decode(byte[] bytes) throws Exception{
        if(null == bytes) throw new NullPointerException();

        ByteArrayInputStream is = new ByteArrayInputStream(bytes);
        Hessian2Input hin = new Hessian2Input(is);
        return hin.readObject();
    }
}
