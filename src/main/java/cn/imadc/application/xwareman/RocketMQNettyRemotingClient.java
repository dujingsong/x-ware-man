package cn.imadc.application.xwareman;

import cn.imadc.application.xwareman.base.config.NettyClientConfig;
import cn.imadc.application.xwareman.base.listener.ChannelEventListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.remoting.InvokeCallback;
import org.apache.rocketmq.remoting.exception.RemotingConnectException;
import org.apache.rocketmq.remoting.exception.RemotingSendRequestException;
import org.apache.rocketmq.remoting.exception.RemotingTimeoutException;
import org.apache.rocketmq.remoting.exception.RemotingTooMuchRequestException;
import org.apache.rocketmq.remoting.netty.NettyDecoder;
import org.apache.rocketmq.remoting.netty.NettyEncoder;
import org.apache.rocketmq.remoting.protocol.RemotingCommand;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * rocketmq通信客户端
 * </p>
 *
 * @author 杜劲松
 * @since 2022-03-03
 */
@Slf4j
@Getter
@Setter
public class RocketMQNettyRemotingClient extends SimpleNettyRemotingClientAbstract {

    public RocketMQNettyRemotingClient(NettyClientConfig nettyClientConfig, ChannelEventListener channelEventListener) {
        super(nettyClientConfig, channelEventListener);
    }

    @Override
    public void start() {
        // 连接参数设置
        @SuppressWarnings("rawtypes")
        Map<ChannelOption, Object> options = new HashMap<>();
        options.put(ChannelOption.TCP_NODELAY, true);
        options.put(ChannelOption.SO_KEEPALIVE, false);
        options.put(ChannelOption.CONNECT_TIMEOUT_MILLIS, nettyClientConfig.getConnectTimeoutMillis());
        options.put(ChannelOption.SO_SNDBUF, nettyClientConfig.getClientSocketSndBufSize());
        options.put(ChannelOption.SO_RCVBUF, nettyClientConfig.getClientSocketRcvBufSize());

        // 编解码等处理器
        List<ChannelHandler> channelHandlers = new ArrayList<>();
        channelHandlers.add(new NettyEncoder());
        channelHandlers.add(new NettyDecoder());
        channelHandlers.add(new IdleStateHandler(0, 0
                , nettyClientConfig.getClientChannelMaxIdleTimeSeconds()));
        channelHandlers.add(new NettyConnectManageHandler(null, this));
        channelHandlers.add(new NettyClientHandler(this));

        simpleStart(this.getClass().getSimpleName(), options, channelHandlers);
    }

    @Override
    public RemotingCommand invokeSync(String address, RemotingCommand request, long timeoutMillis)
            throws InterruptedException, RemotingConnectException, RemotingTooMuchRequestException
            , RemotingTimeoutException, RemotingSendRequestException {
        return super.invokeSync(address, request, timeoutMillis);
    }

    @Override
    public void invokeAsync(String address, RemotingCommand request, long timeoutMillis, InvokeCallback invokeCallback)
            throws InterruptedException, RemotingConnectException, RemotingTooMuchRequestException
            , RemotingTimeoutException, RemotingSendRequestException {
        super.invokeAsync(address, request, timeoutMillis, invokeCallback);
    }
}
