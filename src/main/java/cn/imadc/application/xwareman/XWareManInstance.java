package cn.imadc.application.xwareman;

import cn.imadc.application.base.data.structure.RedisParser;
import cn.imadc.application.base.data.structure.RedisSentinel;
import cn.imadc.application.base.data.structure.RedisSentinelMaster;
import cn.imadc.application.base.data.structure.RedisSentinelSlave;
import cn.imadc.application.base.lettuce.RedisClient;
import cn.imadc.application.base.lettuce.RedisSentinelExtensionCommands;
import cn.imadc.application.base.toolkit.serialization.JsonUtil;
import io.lettuce.core.sentinel.api.sync.RedisSentinelCommands;

import java.util.List;
import java.util.Map;

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
//        RocketMQClient remotingClient = new RocketMQClient(nettyClientConfig
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
//    }=

    public static void main(String[] args) throws InterruptedException {

        String nodeHost = "192.168.137.200", sentinelHost = "10.100.0.92";
        int nodePort = 6379, sentinelPort = 51111;

        RedisClient redisClient = new RedisClient();

        RedisSentinelCommands<String, String> sentinelCommands = redisClient.getRedisSentinelCommands(sentinelHost, sentinelPort, null);

        List<Map<String, String>> masters = sentinelCommands.masters();
        List<RedisSentinelMaster> sentinelMasters = RedisParser.parseSentinelMasters(JsonUtil.objectToJson(masters));

        List<Map<String, String>> slaves = sentinelCommands.slaves(sentinelMasters.get(0).getName());
        List<RedisSentinelSlave> sentinelSlaves = RedisParser.parseSentinelSlaves(JsonUtil.objectToJson(slaves));

        RedisSentinelExtensionCommands sentinelExtensionCommands = redisClient.getRedisSentinelExtensionCommands(sentinelHost, sentinelPort, null);
        List<Map<String, String>> sentinels = sentinelExtensionCommands.sentinelSentinels(sentinelMasters.get(0).getName());

        List<RedisSentinel> redisSentinels = RedisParser.parseSentinels(JsonUtil.objectToJson(sentinels));

        System.out.println(redisSentinels);
    }
}
