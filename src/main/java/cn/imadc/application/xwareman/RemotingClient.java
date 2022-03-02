package cn.imadc.application.xwareman;

import org.apache.rocketmq.remoting.exception.RemotingConnectException;
import org.apache.rocketmq.remoting.exception.RemotingSendRequestException;
import org.apache.rocketmq.remoting.exception.RemotingTimeoutException;
import org.apache.rocketmq.remoting.exception.RemotingTooMuchRequestException;
import org.apache.rocketmq.remoting.protocol.RemotingCommand;

import java.util.concurrent.ExecutorService;

/**
 * <p>
 *
 * </p>
 *
 * @author 杜劲松
 * @since 2022-03-02
 */
public interface RemotingClient extends RemotingService {

    RemotingCommand invokeSync(final String address, final RemotingCommand request, final long timeoutMillis)
            throws InterruptedException, RemotingConnectException, RemotingTooMuchRequestException,
            RemotingTimeoutException, RemotingSendRequestException;

    void invokeAsync(final String address, final RemotingCommand request, final long timeoutMillis,
                     final InvokeCallback invokeCallback)
            throws InterruptedException, RemotingConnectException, RemotingTooMuchRequestException,
            RemotingTimeoutException, RemotingSendRequestException;

    void registerProcessor(final int requestCode, final NettyRequestProcessor processor, final ExecutorService executor);
}
