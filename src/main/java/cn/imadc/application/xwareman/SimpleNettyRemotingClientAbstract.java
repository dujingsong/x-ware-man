package cn.imadc.application.xwareman;

import cn.imadc.application.xwareman.base.config.NettyClientConfig;
import cn.imadc.application.xwareman.base.listener.ChannelEventListener;
import cn.imadc.application.xwareman.base.remoting.NettyRemotingAbstract;
import cn.imadc.application.xwareman.base.remoting.RPCHook;
import cn.imadc.application.xwareman.base.remoting.RemotingClient;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.remoting.InvokeCallback;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.apache.rocketmq.remoting.exception.RemotingConnectException;
import org.apache.rocketmq.remoting.exception.RemotingSendRequestException;
import org.apache.rocketmq.remoting.exception.RemotingTimeoutException;
import org.apache.rocketmq.remoting.exception.RemotingTooMuchRequestException;
import org.apache.rocketmq.remoting.protocol.RemotingCommand;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <p>
 * netty通信客户端
 * </p>
 *
 * @author 杜劲松
 * @since 2022-03-03
 */
@Slf4j
@Getter
@Setter
public abstract class SimpleNettyRemotingClientAbstract extends NettyRemotingAbstract implements RemotingClient {

    // ------------------------------------netty------------------------------------
    protected final NettyClientConfig nettyClientConfig;
    protected final Bootstrap bootstrap = new Bootstrap();
    protected final EventLoopGroup eventLoopGroupWorker;
    protected final ChannelEventListener channelEventListener;
    protected DefaultEventExecutorGroup defaultEventExecutorGroup;

    // ------------------------------------executor------------------------------------
    /**
     * Invoke the callback methods in this executor when process response.
     */
    private ExecutorService callbackExecutor;
    private final ExecutorService publicExecutor;

    public SimpleNettyRemotingClientAbstract(NettyClientConfig nettyClientConfig
            , ChannelEventListener channelEventListener) {
        super(nettyClientConfig.getClientAsyncSemaphoreValue());

        this.nettyClientConfig = nettyClientConfig;
        this.channelEventListener = channelEventListener;

        int publicThreadNums = nettyClientConfig.getClientCallbackExecutorThreads();
        if (publicThreadNums <= 0) {
            publicThreadNums = 4;
        }

        this.publicExecutor = Executors.newFixedThreadPool(publicThreadNums, new ThreadFactory() {
            private final AtomicInteger threadIndex = new AtomicInteger(0);

            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, this.getClass().getSimpleName() + "PublicExecutor_"
                        + this.threadIndex.incrementAndGet());
            }
        });

        this.eventLoopGroupWorker = new NioEventLoopGroup(1, new ThreadFactory() {
            private final AtomicInteger threadIndex = new AtomicInteger(0);

            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, String.format(this.getClass().getSimpleName() + "_%d"
                        , this.threadIndex.incrementAndGet()));
            }
        });
    }

    // ------------------------------------simple implementation------------------------------------

    protected void initDefaultEventExecutorGroup(String threadNamePrefix) {
        this.defaultEventExecutorGroup = new DefaultEventExecutorGroup(
                nettyClientConfig.getClientWorkerThreads(),
                new ThreadFactory() {

                    private final AtomicInteger threadIndex = new AtomicInteger(0);

                    @Override
                    public Thread newThread(Runnable r) {
                        return new Thread(r, threadNamePrefix + this.threadIndex.incrementAndGet());
                    }
                });
    }

    protected void startNettyEventExecutor() {
        if (this.channelEventListener != null) {
            this.nettyEventExecutor.start();
        }
    }

    @SuppressWarnings("rawtypes")
    protected void simpleStart(String clientName, Map<ChannelOption, Object> options
            , List<ChannelHandler> channelHandlers) {
        initDefaultEventExecutorGroup(clientName + "WorkerThread_");

        SimpleNettyRemotingClientAbstract nettyRemotingAbstract = this;

        this.bootstrap.group(this.eventLoopGroupWorker).channel(NioSocketChannel.class);
        options.forEach(this.bootstrap::option);

        this.bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(SocketChannel ch) throws Exception {
                ChannelPipeline pipeline = ch.pipeline();

                ChannelHandler[] channelHandlerArray = channelHandlers.toArray(new ChannelHandler[0]);

                pipeline.addLast(defaultEventExecutorGroup, channelHandlerArray);
            }
        });

        startNettyEventExecutor();
    }

    @Override
    public void shutdown() {
        try {
            for (ChannelWrapper cw : channelTables.values()) {
                closeChannel(null, cw.getChannel());
            }

            this.channelTables.clear();

            this.eventLoopGroupWorker.shutdownGracefully();

            this.nettyEventExecutor.shutdown();

            if (this.defaultEventExecutorGroup != null) {
                this.defaultEventExecutorGroup.shutdownGracefully();
            }
        } catch (Exception e) {
            log.error(this.getClass().getSimpleName() + " shutdown exception, ", e);
        }
    }

    @Override
    public void registerRPCHook(RPCHook rpcHook) {
        if (rpcHook != null && !rpcHooks.contains(rpcHook)) {
            rpcHooks.add(rpcHook);
        }
    }

    @Override
    public ChannelEventListener getChannelEventListener() {
        return channelEventListener;
    }

    @Override
    public RemotingCommand invokeSync(String address, RemotingCommand request, long timeoutMillis)
            throws InterruptedException, RemotingConnectException, RemotingTooMuchRequestException
            , RemotingTimeoutException, RemotingSendRequestException {
        long beginStartTime = System.currentTimeMillis();
        final Channel channel = getAndCreateChannel(address);
        if (channel != null && channel.isActive()) {
            try {
                doBeforeRpcHooks(address, request);
                long costTime = System.currentTimeMillis() - beginStartTime;
                if (timeoutMillis < costTime) {
                    throw new RemotingTimeoutException("invokeSync call timeout");
                }
                RemotingCommand response = this.invokeSyncImpl(channel, request, timeoutMillis - costTime);
                doAfterRpcHooks(RemotingHelper.parseChannelRemoteAddr(channel), request, response);
                return response;
            } catch (RemotingSendRequestException e) {
                log.warn("invokeSync: send request exception, so close the channel[{}]", address);
                this.closeChannel(address, channel);
                throw e;
            } catch (RemotingTimeoutException e) {
                if (nettyClientConfig.isClientCloseSocketIfTimeout()) {
                    this.closeChannel(address, channel);
                    log.warn("invokeSync: close socket because of timeout, {}ms, {}", timeoutMillis, address);
                }
                log.warn("invokeSync: wait response timeout exception, the channel[{}]", address);
                throw e;
            }
        } else {
            this.closeChannel(address, channel);
            throw new RemotingConnectException(address);
        }
    }

    @Override
    public void invokeAsync(String address, RemotingCommand request, long timeoutMillis, InvokeCallback invokeCallback)
            throws InterruptedException, RemotingConnectException, RemotingTooMuchRequestException
            , RemotingTimeoutException, RemotingSendRequestException {
        long beginStartTime = System.currentTimeMillis();
        final Channel channel = this.getAndCreateChannel(address);
        if (channel != null && channel.isActive()) {
            try {
                doBeforeRpcHooks(address, request);
                long costTime = System.currentTimeMillis() - beginStartTime;
                if (timeoutMillis < costTime) {
                    throw new RemotingTooMuchRequestException("invokeAsync call timeout");
                }
                this.invokeAsyncImpl(channel, request, timeoutMillis - costTime, invokeCallback);
            } catch (RemotingSendRequestException e) {
                log.warn("invokeAsync: send request exception, so close the channel[{}]", address);
                this.closeChannel(address, channel);
                throw e;
            }
        } else {
            this.closeChannel(address, channel);
            throw new RemotingConnectException(address);
        }
    }

    // ------------------------------------simple channel implementation------------------------------------

    protected Channel getAndCreateChannel(final String address) throws RemotingConnectException, InterruptedException {

        ChannelWrapper cw = this.channelTables.get(address);
        if (cw != null && cw.isOK()) {
            return cw.getChannel();
        }

        return this.createChannel(address);
    }

    private Channel createChannel(final String address) throws InterruptedException {
        ChannelWrapper cw = this.channelTables.get(address);
        if (cw != null && cw.isOK()) {
            return cw.getChannel();
        }

        if (lockChannelTables.tryLock(LOCK_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)) {
            try {
                boolean createNewConnection;
                cw = this.channelTables.get(address);
                if (cw != null) {

                    if (cw.isOK()) {
                        return cw.getChannel();
                    } else if (!cw.getChannelFuture().isDone()) {
                        createNewConnection = false;
                    } else {
                        this.channelTables.remove(address);
                        createNewConnection = true;
                    }
                } else {
                    createNewConnection = true;
                }

                if (createNewConnection) {
                    ChannelFuture channelFuture = this.bootstrap.connect(RemotingHelper.string2SocketAddress(address));
                    log.info("createChannel: begin to connect remote host[{}] asynchronously", address);
                    cw = new ChannelWrapper(channelFuture);
                    this.channelTables.put(address, cw);
                }
            } catch (Exception e) {
                log.error("createChannel: create channel exception", e);
            } finally {
                this.lockChannelTables.unlock();
            }
        } else {
            log.warn("createChannel: try to lock channel table, but timeout, {}ms", LOCK_TIMEOUT_MILLIS);
        }

        if (cw != null) {
            ChannelFuture channelFuture = cw.getChannelFuture();
            if (channelFuture.awaitUninterruptibly(this.nettyClientConfig.getConnectTimeoutMillis())) {
                if (cw.isOK()) {
                    log.info("createChannel: connect remote host[{}] success, {}", address, channelFuture);
                    return cw.getChannel();
                } else {
                    log.warn("createChannel: connect remote host[" + address + "] failed, "
                            + channelFuture.toString(), channelFuture.cause());
                }
            } else {
                log.warn("createChannel: connect remote host[{}] timeout {}ms, {}", address
                        , this.nettyClientConfig.getConnectTimeoutMillis(), channelFuture);
            }
        }

        return null;
    }
}
