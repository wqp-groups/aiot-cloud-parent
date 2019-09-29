package com.wqp.aiot.cloud.stream;

import com.wqp.aiot.cloud.stream.stream.netty.ChannelHandlerServer;
import com.wqp.common.stream.netty.core.NettyConf;
import com.wqp.common.stream.netty.server.NettyExecutorServer;
import io.netty.channel.ChannelHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class CloudStreamApplication {

    @Autowired
    private ChannelHandlerServer channelHandlerServer;

    public static void main(String[] args) {
        SpringApplication.run(CloudStreamApplication.class, args);
    }

    @PostConstruct
    public void launcher(){
        new Thread(() ->{
            List<ChannelHandler> ch = new ArrayList<>();
//            IdleStateHandler idleStateHandler = new IdleStateHandler(10, 0,0, TimeUnit.SECONDS);
//            ChannelHandlerServer handlerServer = new ChannelHandlerServer();
            ch.add(channelHandlerServer);
            NettyConf conf = new NettyConf().setPort(9999).setFrameDelimiter("$$@$$").setChannelHandler(ch);
            NettyExecutorServer server = new NettyExecutorServer();
            server.setNettyConf(conf);
            server.launcher();
        }).start();
    }

}
