package com.husky.common.codex;



import com.husky.common.utils.HessianSerialize;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

/**
 * Created by google on 16/6/24.
 */
public class Encoder extends MessageToByteEncoder{

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        byte[] data = HessianSerialize.serialize(msg);//使用hessian2编码码
        out.writeInt(data.length);
        out.writeBytes(data);
    }
}
