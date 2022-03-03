package cn.imadc.application.xwareman;

import cn.imadc.application.xwareman.base.listener.ChannelEventListener;
import cn.imadc.application.xwareman.base.remoting.NettyRemotingAbstract;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.apache.rocketmq.remoting.netty.NettyEvent;
import org.apache.rocketmq.remoting.netty.NettyEventType;

import java.net.SocketAddress;

@Slf4j
public class NettyConnectManageHandler extends ChannelDuplexHandler {

    private final ChannelEventListener channelEventListener;
    private final NettyRemotingAbstract nettyRemotingAbstract;

    public NettyConnectManageHandler(ChannelEventListener channelEventListener, NettyRemotingAbstract nettyRemotingAbstract) {
        this.channelEventListener = channelEventListener;
        this.nettyRemotingAbstract = nettyRemotingAbstract;
    }

    @Override
    public void connect(ChannelHandlerContext ctx, SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise) throws Exception {

        final String local = localAddress == null ? "UNKNOWN" : RemotingHelper.parseSocketAddressAddr(localAddress);
        final String remote = remoteAddress == null ? "UNKNOWN" : RemotingHelper.parseSocketAddressAddr(remoteAddress);
        log.info("NETTY CLIENT PIPELINE: CONNECT  {} => {}", local, remote);

        super.connect(ctx, remoteAddress, localAddress, promise);

        if (channelEventListener != null) {
            nettyRemotingAbstract.putNettyEvent(new NettyEvent(NettyEventType.CONNECT, remote, ctx.channel()));
        }
    }

    @Override
    public void disconnect(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
        final String remoteAddress = RemotingHelper.parseChannelRemoteAddr(ctx.channel());
        log.info("NETTY CLIENT PIPELINE: DISCONNECT {}", remoteAddress);
        nettyRemotingAbstract.closeChannel(ctx.channel());
        super.disconnect(ctx, promise);

        if (channelEventListener != null) {
            nettyRemotingAbstract.putNettyEvent(new NettyEvent(NettyEventType.CLOSE, remoteAddress, ctx.channel()));
        }
    }

    @Override
    public void close(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
        super.close(ctx, promise);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        super.userEventTriggered(ctx, evt);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}