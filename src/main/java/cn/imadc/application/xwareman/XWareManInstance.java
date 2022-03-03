package cn.imadc.application.xwareman;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.sentinel.api.StatefulRedisSentinelConnection;
import io.lettuce.core.sentinel.api.sync.RedisSentinelCommands;

import java.time.Duration;

/**
 * <p>
 *
 * </p>
 *
 * @author 杜劲松
 * @since 2022-03-02
 */
public class XWareManInstance {

//    public static void main(String[] args) throws RemotingConnectException, RemotingSendRequestException
//            , RemotingTimeoutException, InterruptedException, RemotingTooMuchRequestException {
//        NettyClientConfig nettyClientConfig = new NettyClientConfig();
//        RocketMQNettyRemotingClient remotingClient = new RocketMQNettyRemotingClient(nettyClientConfig
//                , null);
//        remotingClient.start();
//        QueryDataVersionRequestHeader requestHeader = new QueryDataVersionRequestHeader();
//        requestHeader.setBrokerAddr("10.100.0.190:30921");
//        requestHeader.setBrokerId(0L);
//        requestHeader.setBrokerName("RaftNode00");
//        requestHeader.setClusterName("RaftCluster");
//        RemotingCommand remotingCommand = RemotingCommand.createRequestCommand(322, requestHeader);
//
//        TopicConfigSerializeWrapper topicConfigWrapper = new TopicConfigSerializeWrapper();
//        remotingCommand.setBody(topicConfigWrapper.getDataVersion().encode());
//
//        RemotingCommand response = remotingClient.invokeSync("127.0.0.1:9876", remotingCommand, 3000);
//        System.out.println(response);
//
//        remotingClient.shutdown();
//    }

    public static void main(String[] args) {
        RedisClient redisClient = RedisClient.create("redis://192.168.137.200:6379");
        redisClient.setDefaultTimeout(Duration.ofMillis(10 * 1000));
        StatefulRedisConnection<String, String> nodeConnection = redisClient.connect();
        RedisCommands<String, String> nodeCommands = nodeConnection.sync();

        String info = nodeCommands.info();

        StatefulRedisSentinelConnection<String, String> sentinelConnection = redisClient.connectSentinel();

        RedisSentinelCommands<String, String> sentinelCommands = sentinelConnection.sync();
        info = sentinelCommands.info();
    }
}
