package cn.imadc.application.xwareman;

import cn.imadc.application.xwareman.base.remoting.NettyRemotingAbstract;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.apache.rocketmq.remoting.protocol.RemotingCommand;

public class NettyClientHandler extends SimpleChannelInboundHandler<RemotingCommand> {

    private final NettyRemotingAbstract nettyRemotingAbstract;

    public NettyClientHandler(NettyRemotingAbstract nettyRemotingAbstract) {
        this.nettyRemotingAbstract = nettyRemotingAbstract;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RemotingCommand msg) throws Exception {
        nettyRemotingAbstract.processMessageReceived(ctx, msg);
    }
}