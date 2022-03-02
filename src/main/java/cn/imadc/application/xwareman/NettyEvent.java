package cn.imadc.application.xwareman;

import io.netty.channel.Channel;

/**
 * <p>
 *
 * </p>
 *
 * @author 杜劲松
 * @since 2022-03-02
 */
public class NettyEvent {
    private final NettyEventType type;
    private final String remoteAddress;
    private final Channel channel;

    public NettyEvent(NettyEventType type, String remoteAddress, Channel channel) {
        this.type = type;
        this.remoteAddress = remoteAddress;
        this.channel = channel;
    }

    public NettyEventType getType() {
        return type;
    }

    public String getRemoteAddress() {
        return remoteAddress;
    }

    public Channel getChannel() {
        return channel;
    }

    @Override
    public String toString() {
        return "NettyEvent [type=" + type + ", remoteAddress=" + remoteAddress + ", channel=" + channel + "]";
    }
}
