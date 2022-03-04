package cn.imadc.application.xwareman;

import cn.imadc.application.base.toolkit.serialization.JsonUtil;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.sentinel.api.StatefulRedisSentinelConnection;
import io.lettuce.core.sentinel.api.sync.RedisSentinelCommands;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
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
        CharSequence password = "Xsbankredis";
        RedisURI redisUri = RedisURI.Builder.redis("192.168.137.200")
                .withPassword(password)
                .build();

        RedisClient redisClient = RedisClient.create(redisUri);
        redisClient.setDefaultTimeout(Duration.ofMillis(10 * 1000));

        StatefulRedisConnection<String, String> nodeConnection = redisClient.connect();
        RedisCommands<String, String> nodeCommands = nodeConnection.sync();

        String info = nodeCommands.info();
        System.out.println("\n--------------------------------------------\r");
        System.out.println(info);
        System.out.println("\n--------------------------------------------");

        parseRedisInfo(info);

        redisUri = RedisURI.Builder.redis("192.168.137.203")
                .withPort(26379)
                .build();

        redisClient = RedisClient.create(redisUri);
        redisClient.setDefaultTimeout(Duration.ofMillis(10 * 1000));

        StatefulRedisSentinelConnection<String, String> sentinelConnection = redisClient.connectSentinel();

        RedisSentinelCommands<String, String> sentinelCommands = sentinelConnection.sync();
        List<Map<String, String>> masters = sentinelCommands.masters();
        System.out.println(masters);

        info = sentinelCommands.info();
        parseRedisInfo(info);
    }

    private static void parseRedisInfo(String info) {
        String[] lineArray = info.split("\r\n");

        Map<String, Map<String, String>> infoMap = new HashMap<>();

        String section = "";
        Map<String, String> fieldMap = new HashMap<>();
        for (int i = 0; i < lineArray.length; i++) {
            String line = lineArray[i];
            if (StringUtils.isEmpty(line)) continue;

            if (line.startsWith("#")) {
                if (i != 0) infoMap.put(section, fieldMap);

                section = line.replace("#", "").trim();

                fieldMap = new HashMap<>();
                continue;
            }

            String[] kvArray = line.split(":");
            fieldMap.put(kvArray[0], kvArray[1]);

            if (i == lineArray.length - 1) {
                infoMap.put(section, fieldMap);
                break;
            }
        }

        String json = JsonUtil.objectToJson(infoMap);
        RedisInfo redisInfo = JsonUtil.jsonToObject(json, RedisInfo.class);

        Map<String, String> replication = infoMap.get("Replication");

        if (!CollectionUtils.isEmpty(replication)) {
            List<RedisInfo.Replication.Slave> slaves = new ArrayList<>();

            for (Map.Entry<String, String> entry : replication.entrySet()) {
                if (!entry.getKey().startsWith("slave")) continue;

                String val = entry.getValue();

                String[] propArray = val.split(",");
                RedisInfo.Replication.Slave slave = new RedisInfo.Replication.Slave();
                for (String prop : propArray) {
                    String[] propValArray = prop.split("=");
                    if (propValArray[0].equals("ip")) slave.setIp(propValArray[1]);
                    else if (propValArray[0].equals("port")) slave.setPort(propValArray[1]);
                    else if (propValArray[0].equals("state")) slave.setState(propValArray[1]);
                    else if (propValArray[0].equals("offset")) slave.setOffset(propValArray[1]);
                    else if (propValArray[0].equals("lag")) slave.setLag(propValArray[1]);
                }
                slaves.add(slave);
            }

            redisInfo.getReplication().setSlaves(slaves);
        }

        Map<String, String> sentinel = infoMap.get("Sentinel");

        if (!CollectionUtils.isEmpty(sentinel)) {
            List<RedisInfo.Sentinel.Master> masters = new ArrayList<>();

            for (Map.Entry<String, String> entry : sentinel.entrySet()) {
                if (!entry.getKey().startsWith("master")) continue;

                String val = entry.getValue();

                String[] propArray = val.split(",");
                RedisInfo.Sentinel.Master master = new RedisInfo.Sentinel.Master();
                for (String prop : propArray) {
                    String[] propValArray = prop.split("=");
                    if (propValArray[0].equals("name")) master.setName(propValArray[1]);
                    else if (propValArray[0].equals("status")) master.setStatus(propValArray[1]);
                    else if (propValArray[0].equals("address")) master.setAddress(propValArray[1]);
                    else if (propValArray[0].equals("slaves")) master.setSlaves(propValArray[1]);
                    else if (propValArray[0].equals("sentinels")) master.setSentinels(propValArray[1]);
                }
                masters.add(master);
            }

            redisInfo.getSentinel().setMasters(masters);
        }

        System.out.println(redisInfo);
    }
}
