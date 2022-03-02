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
public interface ChannelEventListener {

    void onChannelConnect(final String remoteAddress, final Channel channel);

    void onChannelClose(final String remoteAddress, final Channel channel);

    void onChannelException(final String remoteAddress, final Channel channel);

    void onChannelIdle(final String remoteAddress, final Channel channel);
}
