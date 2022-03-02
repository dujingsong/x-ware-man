package cn.imadc.application.xwareman;

import io.netty.channel.ChannelHandlerContext;
import org.apache.rocketmq.remoting.protocol.RemotingCommand;

/**
 * <p>
 *
 * </p>
 *
 * @author 杜劲松
 * @since 2022-03-02
 */
public interface NettyRequestProcessor {

    RemotingCommand processRequest(ChannelHandlerContext ctx, RemotingCommand request)
            throws Exception;

    boolean rejectRequest();
}
