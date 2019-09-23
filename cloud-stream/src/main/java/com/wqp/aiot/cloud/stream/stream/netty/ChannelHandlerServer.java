package com.wqp.aiot.cloud.stream.stream.netty;


import com.google.protobuf.InvalidProtocolBufferException;
import com.wqp.aiot.cloud.stream.stream.protof.StreamTransferProtof;
import com.wqp.aiot.cloud.stream.stream.protof.StreamTransferTopic;
import com.wqp.common.stream.netty.server.AbstractChannelHandlerServer;
import com.wqp.common.util.common.JacksonMapperUtil;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ChannelHandlerServer extends AbstractChannelHandlerServer {
    @Value("${aiot.edge.sense.uploaddirectory}")
    private String aiotEdgeSenseUploaddirectory;
    @Value("${aiot.edge.heartbeat.uploaddirectory}")
    private String aiotEdgeHeartbeatUploaddirectory;

    @Override
    public Object receive(ChannelHandlerContext ctx, byte[] bytes) {
        System.out.println("服务端接收到客户端的数据了");
        StreamTransferProtof.StreamTransferData streamTransferData = null;
        try {
            streamTransferData = StreamTransferProtof.StreamTransferData.parseFrom(bytes);
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
        if(streamTransferData == null) return "Netty服务端接收数据异常";

        String filePath;
        if (StreamTransferTopic.EDGE_UPLOAD_SENSOR_DATA.getType().equalsIgnoreCase(streamTransferData.getOrigintopic())){
            // 传感器数据存储
            filePath = aiotEdgeSenseUploaddirectory + streamTransferData.getOriginname();
        }else if (StreamTransferTopic.EDGE_UPLOAD_HEARTBEAT_DATA.getType().equals(streamTransferData.getOrigintopic())){
            // 心跳数据存储
            filePath = aiotEdgeHeartbeatUploaddirectory + streamTransferData.getOriginname();
        }else {
            // 默认传感器数据存储
            filePath = aiotEdgeSenseUploaddirectory + streamTransferData.getOriginname();
        }
        String origindata = streamTransferData.getOrigindata();
        JacksonMapperUtil.json2file(origindata, filePath);

        return "server receive data success,origin data is ：(" + origindata + ")";
    }
}
