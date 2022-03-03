//package cn.imadc.application.xwareman.base;
//
//import cn.imadc.application.xwareman.base.config.NettyClientConfig;
//import cn.imadc.application.xwareman.base.listener.ChannelEventListener;
//import cn.imadc.application.xwareman.base.processor.NettyRequestProcessor;
//import cn.imadc.application.xwareman.base.remoting.NettyRemotingAbstract;
//import cn.imadc.application.xwareman.base.remoting.RPCHook;
//import cn.imadc.application.xwareman.base.remoting.RemotingClient;
//import io.netty.bootstrap.Bootstrap;
//import io.netty.channel.*;
//import io.netty.channel.nio.NioEventLoopGroup;
//import io.netty.channel.socket.SocketChannel;
//import io.netty.channel.socket.nio.NioSocketChannel;
//import io.netty.handler.timeout.IdleStateHandler;
//import io.netty.util.concurrent.DefaultEventExecutorGroup;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.rocketmq.remoting.InvokeCallback;
//import org.apache.rocketmq.remoting.common.Pair;
//import org.apache.rocketmq.remoting.common.RemotingHelper;
//import org.apache.rocketmq.remoting.common.RemotingUtil;
//import org.apache.rocketmq.remoting.exception.RemotingConnectException;
//import org.apache.rocketmq.remoting.exception.RemotingSendRequestException;
//import org.apache.rocketmq.remoting.exception.RemotingTimeoutException;
//import org.apache.rocketmq.remoting.exception.RemotingTooMuchRequestException;
//import org.apache.rocketmq.remoting.netty.NettyDecoder;
//import org.apache.rocketmq.remoting.netty.NettyEncoder;
//import org.apache.rocketmq.remoting.netty.NettyEvent;
//import org.apache.rocketmq.remoting.netty.NettyEventType;
//import org.apache.rocketmq.remoting.protocol.RemotingCommand;
//
//import java.net.SocketAddress;
//import java.util.Map;
//import java.util.Timer;
//import java.util.TimerTask;
//import java.util.concurrent.*;
//import java.util.concurrent.atomic.AtomicInteger;
//import java.util.concurrent.locks.Lock;
//import java.util.concurrent.locks.ReentrantLock;
//
///**
// * <p>
// *
// * </p>
// *
// * @author 杜劲松
// * @since 2022-03-02
// */
//@Slf4j
//public class NettyRemotingClient extends NettyRemotingAbstract implements RemotingClient {
//
//    private final NettyClientConfig nettyClientConfig;
//    private final Bootstrap bootstrap = new Bootstrap();
//    private final EventLoopGroup eventLoopGroupWorker;
//    private final ChannelEventListener channelEventListener;
//    private DefaultEventExecutorGroup defaultEventExecutorGroup;
//
//    private final Lock lockChannelTables = new ReentrantLock();
//    private final ConcurrentMap<String /* addr */, ChannelWrapper> channelTables = new ConcurrentHashMap<>();
//    private static final long LOCK_TIMEOUT_MILLIS = 3000;
//    /**
//     * Invoke the callback methods in this executor when process response.
//     */
//    private ExecutorService callbackExecutor;
//    private final ExecutorService publicExecutor;
//    private final Timer timer = new Timer("ClientHouseKeepingService", true);
//
//    public NettyRemotingClient(final NettyClientConfig nettyClientConfig, final ChannelEventListener channelEventListener) {
//        super(nettyClientConfig.getClientAsyncSemaphoreValue());
//
//        this.nettyClientConfig = nettyClientConfig;
//        this.channelEventListener = channelEventListener;
//
//        int publicThreadNums = nettyClientConfig.getClientCallbackExecutorThreads();
//        if (publicThreadNums <= 0) {
//            publicThreadNums = 4;
//        }
//
//        this.publicExecutor = Executors.newFixedThreadPool(publicThreadNums, new ThreadFactory() {
//            private final AtomicInteger threadIndex = new AtomicInteger(0);
//
//            @Override
//            public Thread newThread(Runnable r) {
//                return new Thread(r, "NettyClientPublicExecutor_" + this.threadIndex.incrementAndGet());
//            }
//        });
//
//
//        this.eventLoopGroupWorker = new NioEventLoopGroup(1, new ThreadFactory() {
//            private final AtomicInteger threadIndex = new AtomicInteger(0);
//
//            @Override
//            public Thread newThread(Runnable r) {
//                return new Thread(r, String.format("NettyClientSelector_%d", this.threadIndex.incrementAndGet()));
//            }
//        });
//    }
//
//    @Override
//    public void start() {
//        this.defaultEventExecutorGroup = new DefaultEventExecutorGroup(
//                nettyClientConfig.getClientWorkerThreads(),
//                new ThreadFactory() {
//
//                    private final AtomicInteger threadIndex = new AtomicInteger(0);
//
//                    @Override
//                    public Thread newThread(Runnable r) {
//                        return new Thread(r, "NettyClientWorkerThread_" + this.threadIndex.incrementAndGet());
//                    }
//                });
//
//        Bootstrap handler = this.bootstrap.group(this.eventLoopGroupWorker).channel(NioSocketChannel.class)
//                .option(ChannelOption.TCP_NODELAY, true)
//                .option(ChannelOption.SO_KEEPALIVE, false)
//                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, nettyClientConfig.getConnectTimeoutMillis())
//                .option(ChannelOption.SO_SNDBUF, nettyClientConfig.getClientSocketSndBufSize())
//                .option(ChannelOption.SO_RCVBUF, nettyClientConfig.getClientSocketRcvBufSize())
//                .handler(new ChannelInitializer<SocketChannel>() {
//                    @Override
//                    public void initChannel(SocketChannel ch) throws Exception {
//                        ChannelPipeline pipeline = ch.pipeline();
//                        pipeline.addLast(
//                                defaultEventExecutorGroup,
//                                new NettyEncoder(),
//                                new NettyDecoder(),
//                                new IdleStateHandler(0, 0, nettyClientConfig.getClientChannelMaxIdleTimeSeconds()),
//                                new NettyConnectManageHandler(),
//                                new NettyClientHandler());
//                    }
//                });
//
//        this.timer.scheduleAtFixedRate(new TimerTask() {
//            @Override
//            public void run() {
//                try {
//                    NettyRemotingClient.this.scanResponseTable();
//                } catch (Throwable e) {
//                    log.error("scanResponseTable exception", e);
//                }
//            }
//        }, 1000 * 3, 1000);
//
//        if (this.channelEventListener != null) {
//            this.nettyEventExecutor.start();
//        }
//    }
//
//    class NettyClientHandler extends SimpleChannelInboundHandler<RemotingCommand> {
//
//        @Override
//        protected void channelRead0(ChannelHandlerContext ctx, RemotingCommand msg) throws Exception {
//            processMessageReceived(ctx, msg);
//        }
//    }
//
//    @Override
//    public void shutdown() {
//        try {
//            this.timer.cancel();
//
//            for (ChannelWrapper cw : this.channelTables.values()) {
//                this.closeChannel(null, cw.getChannel());
//            }
//
//            this.channelTables.clear();
//
//            this.eventLoopGroupWorker.shutdownGracefully();
//
//            this.nettyEventExecutor.shutdown();
//
//            if (this.defaultEventExecutorGroup != null) {
//                this.defaultEventExecutorGroup.shutdownGracefully();
//            }
//        } catch (Exception e) {
//            log.error("NettyRemotingClient shutdown exception, ", e);
//        }
//
//        if (this.publicExecutor != null) {
//            try {
//                this.publicExecutor.shutdown();
//            } catch (Exception e) {
//                log.error("NettyRemotingServer shutdown exception, ", e);
//            }
//        }
//    }
//
//    @Override
//    public void registerRPCHook(RPCHook rpcHook) {
//        if (rpcHook != null && !rpcHooks.contains(rpcHook)) {
//            rpcHooks.add(rpcHook);
//        }
//    }
//
//    @Override
//    public void registerProcessor(int requestCode, NettyRequestProcessor processor, ExecutorService executor) {
//        ExecutorService executorThis = executor;
//        if (null == executor) {
//            executorThis = this.publicExecutor;
//        }
//
//        Pair<NettyRequestProcessor, ExecutorService> pair = new Pair<NettyRequestProcessor, ExecutorService>(processor, executorThis);
//        this.processorTable.put(requestCode, pair);
//    }
//
//    @Override
//    public RemotingCommand invokeSync(String address, RemotingCommand request, long timeoutMillis) throws InterruptedException, RemotingConnectException, RemotingSendRequestException, RemotingTimeoutException {
//        long beginStartTime = System.currentTimeMillis();
//        final Channel channel = this.getAndCreateChannel(address);
//        if (channel != null && channel.isActive()) {
//            try {
//                doBeforeRpcHooks(address, request);
//                long costTime = System.currentTimeMillis() - beginStartTime;
//                if (timeoutMillis < costTime) {
//                    throw new RemotingTimeoutException("invokeSync call timeout");
//                }
//                RemotingCommand response = this.invokeSyncImpl(channel, request, timeoutMillis - costTime);
//                doAfterRpcHooks(RemotingHelper.parseChannelRemoteAddr(channel), request, response);
//                return response;
//            } catch (RemotingSendRequestException e) {
//                log.warn("invokeSync: send request exception, so close the channel[{}]", address);
//                this.closeChannel(address, channel);
//                throw e;
//            } catch (RemotingTimeoutException e) {
//                if (nettyClientConfig.isClientCloseSocketIfTimeout()) {
//                    this.closeChannel(address, channel);
//                    log.warn("invokeSync: close socket because of timeout, {}ms, {}", timeoutMillis, address);
//                }
//                log.warn("invokeSync: wait response timeout exception, the channel[{}]", address);
//                throw e;
//            }
//        } else {
//            this.closeChannel(address, channel);
//            throw new RemotingConnectException(address);
//        }
//    }
//
//    private Channel getAndCreateChannel(final String addr) throws RemotingConnectException, InterruptedException {
//
//        ChannelWrapper cw = this.channelTables.get(addr);
//        if (cw != null && cw.isOK()) {
//            return cw.getChannel();
//        }
//
//        return this.createChannel(addr);
//    }
//
//    private Channel createChannel(final String addr) throws InterruptedException {
//        ChannelWrapper cw = this.channelTables.get(addr);
//        if (cw != null && cw.isOK()) {
//            return cw.getChannel();
//        }
//
//        if (this.lockChannelTables.tryLock(LOCK_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)) {
//            try {
//                boolean createNewConnection;
//                cw = this.channelTables.get(addr);
//                if (cw != null) {
//
//                    if (cw.isOK()) {
//                        return cw.getChannel();
//                    } else if (!cw.getChannelFuture().isDone()) {
//                        createNewConnection = false;
//                    } else {
//                        this.channelTables.remove(addr);
//                        createNewConnection = true;
//                    }
//                } else {
//                    createNewConnection = true;
//                }
//
//                if (createNewConnection) {
//                    ChannelFuture channelFuture = this.bootstrap.connect(RemotingHelper.string2SocketAddress(addr));
//                    log.info("createChannel: begin to connect remote host[{}] asynchronously", addr);
//                    cw = new ChannelWrapper(channelFuture);
//                    this.channelTables.put(addr, cw);
//                }
//            } catch (Exception e) {
//                log.error("createChannel: create channel exception", e);
//            } finally {
//                this.lockChannelTables.unlock();
//            }
//        } else {
//            log.warn("createChannel: try to lock channel table, but timeout, {}ms", LOCK_TIMEOUT_MILLIS);
//        }
//
//        if (cw != null) {
//            ChannelFuture channelFuture = cw.getChannelFuture();
//            if (channelFuture.awaitUninterruptibly(this.nettyClientConfig.getConnectTimeoutMillis())) {
//                if (cw.isOK()) {
//                    log.info("createChannel: connect remote host[{}] success, {}", addr, channelFuture.toString());
//                    return cw.getChannel();
//                } else {
//                    log.warn("createChannel: connect remote host[" + addr + "] failed, " + channelFuture.toString(), channelFuture.cause());
//                }
//            } else {
//                log.warn("createChannel: connect remote host[{}] timeout {}ms, {}", addr, this.nettyClientConfig.getConnectTimeoutMillis(),
//                        channelFuture.toString());
//            }
//        }
//
//        return null;
//    }
//
//    @Override
//    public void invokeAsync(String address, RemotingCommand request, long timeoutMillis, InvokeCallback invokeCallback)
//            throws InterruptedException, RemotingConnectException, RemotingTooMuchRequestException, RemotingTimeoutException,
//            RemotingSendRequestException {
//
//        long beginStartTime = System.currentTimeMillis();
//        final Channel channel = this.getAndCreateChannel(address);
//        if (channel != null && channel.isActive()) {
//            try {
//                doBeforeRpcHooks(address, request);
//                long costTime = System.currentTimeMillis() - beginStartTime;
//                if (timeoutMillis < costTime) {
//                    throw new RemotingTooMuchRequestException("invokeAsync call timeout");
//                }
//                this.invokeAsyncImpl(channel, request, timeoutMillis - costTime, invokeCallback);
//            } catch (RemotingSendRequestException e) {
//                log.warn("invokeAsync: send request exception, so close the channel[{}]", address);
//                this.closeChannel(address, channel);
//                throw e;
//            }
//        } else {
//            this.closeChannel(address, channel);
//            throw new RemotingConnectException(address);
//        }
//    }
//
//    @Override
//    public ChannelEventListener getChannelEventListener() {
//        return this.channelEventListener;
//    }
//
//    @Override
//    public ExecutorService getCallbackExecutor() {
//        return callbackExecutor != null ? callbackExecutor : publicExecutor;
//    }
//
//    public void closeChannel(final String addr, final Channel channel) {
//        if (null == channel)
//            return;
//
//        final String addrRemote = null == addr ? RemotingHelper.parseChannelRemoteAddr(channel) : addr;
//
//        try {
//            if (this.lockChannelTables.tryLock(LOCK_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)) {
//                try {
//                    boolean removeItemFromTable = true;
//                    final ChannelWrapper prevCW = this.channelTables.get(addrRemote);
//
//                    log.info("closeChannel: begin close the channel[{}] Found: {}", addrRemote, prevCW != null);
//
//                    if (null == prevCW) {
//                        log.info("closeChannel: the channel[{}] has been removed from the channel table before", addrRemote);
//                        removeItemFromTable = false;
//                    } else if (prevCW.getChannel() != channel) {
//                        log.info("closeChannel: the channel[{}] has been closed before, and has been created again, nothing to do.",
//                                addrRemote);
//                        removeItemFromTable = false;
//                    }
//
//                    if (removeItemFromTable) {
//                        this.channelTables.remove(addrRemote);
//                        log.info("closeChannel: the channel[{}] was removed from channel table", addrRemote);
//                    }
//
//                    RemotingUtil.closeChannel(channel);
//                } catch (Exception e) {
//                    log.error("closeChannel: close the channel exception", e);
//                } finally {
//                    this.lockChannelTables.unlock();
//                }
//            } else {
//                log.warn("closeChannel: try to lock channel table, but timeout, {}ms", LOCK_TIMEOUT_MILLIS);
//            }
//        } catch (InterruptedException e) {
//            log.error("closeChannel exception", e);
//        }
//    }
//
//    class NettyConnectManageHandler extends ChannelDuplexHandler {
//        @Override
//        public void connect(ChannelHandlerContext ctx, SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise) throws Exception {
//
//            final String local = localAddress == null ? "UNKNOWN" : RemotingHelper.parseSocketAddressAddr(localAddress);
//            final String remote = remoteAddress == null ? "UNKNOWN" : RemotingHelper.parseSocketAddressAddr(remoteAddress);
//            log.info("NETTY CLIENT PIPELINE: CONNECT  {} => {}", local, remote);
//
//            super.connect(ctx, remoteAddress, localAddress, promise);
//
//            if (NettyRemotingClient.this.channelEventListener != null) {
//                NettyRemotingClient.this.putNettyEvent(new NettyEvent(NettyEventType.CONNECT, remote, ctx.channel()));
//            }
//        }
//
//        @Override
//        public void disconnect(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
//            final String remoteAddress = RemotingHelper.parseChannelRemoteAddr(ctx.channel());
//            log.info("NETTY CLIENT PIPELINE: DISCONNECT {}", remoteAddress);
//            closeChannel(ctx.channel());
//            super.disconnect(ctx, promise);
//
//            if (NettyRemotingClient.this.channelEventListener != null) {
//                NettyRemotingClient.this.putNettyEvent(new NettyEvent(NettyEventType.CLOSE, remoteAddress, ctx.channel()));
//            }
//        }
//
//        @Override
//        public void close(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
//            super.close(ctx, promise);
//        }
//
//        @Override
//        public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
//            super.userEventTriggered(ctx, evt);
//        }
//
//        @Override
//        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
//            super.exceptionCaught(ctx, cause);
//        }
//    }
//
//    static class ChannelWrapper {
//        private final ChannelFuture channelFuture;
//
//        public ChannelWrapper(ChannelFuture channelFuture) {
//            this.channelFuture = channelFuture;
//        }
//
//        public boolean isOK() {
//            return this.channelFuture.channel() != null && this.channelFuture.channel().isActive();
//        }
//
//        public boolean isWritable() {
//            return this.channelFuture.channel().isWritable();
//        }
//
//        private Channel getChannel() {
//            return this.channelFuture.channel();
//        }
//
//        public ChannelFuture getChannelFuture() {
//            return channelFuture;
//        }
//    }
//
//    public void closeChannel(final Channel channel) {
//        if (null == channel)
//            return;
//
//        try {
//            if (this.lockChannelTables.tryLock(LOCK_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)) {
//                try {
//                    boolean removeItemFromTable = true;
//                    ChannelWrapper prevCW = null;
//                    String addrRemote = null;
//                    for (Map.Entry<String, ChannelWrapper> entry : channelTables.entrySet()) {
//                        String key = entry.getKey();
//                        ChannelWrapper prev = entry.getValue();
//                        if (prev.getChannel() != null) {
//                            if (prev.getChannel() == channel) {
//                                prevCW = prev;
//                                addrRemote = key;
//                                break;
//                            }
//                        }
//                    }
//
//                    if (null == prevCW) {
//                        log.info("eventCloseChannel: the channel[{}] has been removed from the channel table before", addrRemote);
//                        removeItemFromTable = false;
//                    }
//
//                    if (removeItemFromTable) {
//                        this.channelTables.remove(addrRemote);
//                        log.info("closeChannel: the channel[{}] was removed from channel table", addrRemote);
//                        RemotingUtil.closeChannel(channel);
//                    }
//                } catch (Exception e) {
//                    log.error("closeChannel: close the channel exception", e);
//                } finally {
//                    this.lockChannelTables.unlock();
//                }
//            } else {
//                log.warn("closeChannel: try to lock channel table, but timeout, {}ms", LOCK_TIMEOUT_MILLIS);
//            }
//        } catch (InterruptedException e) {
//            log.error("closeChannel exception", e);
//        }
//    }
//}
