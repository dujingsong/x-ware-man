package cn.imadc.application.xwareman;

import org.apache.rocketmq.common.DataVersion;
import org.apache.rocketmq.common.protocol.ResponseCode;
import org.apache.rocketmq.common.protocol.body.TopicConfigSerializeWrapper;
import org.apache.rocketmq.common.protocol.header.namesrv.QueryDataVersionRequestHeader;
import org.apache.rocketmq.common.protocol.header.namesrv.QueryDataVersionResponseHeader;
import org.apache.rocketmq.remoting.exception.*;
import org.apache.rocketmq.remoting.protocol.RemotingCommand;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *
 * </p>
 *
 * @author 杜劲松
 * @since 2022-03-02
 */
public class XWareManInstance {

    public static void main(String[] args) {
        NettyClientConfig nettyClientConfig = new NettyClientConfig();
        RemotingClient remotingClient = new NettyRemotingClient(nettyClientConfig, null);


        remotingClient.start();

        QueryDataVersionRequestHeader requestHeader = new QueryDataVersionRequestHeader();
        requestHeader.setBrokerAddr("10.100.0.190:30921");
        requestHeader.setBrokerId(0L);
        requestHeader.setBrokerName("RaftNode00");
        requestHeader.setClusterName("RaftCluster");
        RemotingCommand remotingCommand = RemotingCommand.createRequestCommand(322, requestHeader);

        TopicConfigSerializeWrapper topicConfigWrapper = new TopicConfigSerializeWrapper();
        remotingCommand.setBody(topicConfigWrapper.getDataVersion().encode());

        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

        executorService.scheduleAtFixedRate(() -> {
            try {
                RemotingCommand response = remotingClient.invokeSync("127.0.0.1:9876", remotingCommand, 3000);
                System.out.println(response);

                DataVersion nameServerDataVersion = null;
                Boolean changed = false;
                switch (response.getCode()) {
                    case ResponseCode.SUCCESS: {
                        QueryDataVersionResponseHeader queryDataVersionResponseHeader =
                                (QueryDataVersionResponseHeader) response.decodeCommandCustomHeader(QueryDataVersionResponseHeader.class);
                        changed = queryDataVersionResponseHeader.getChanged();
                        byte[] body = response.getBody();
                        if (body != null) {
                            nameServerDataVersion = DataVersion.decode(body, DataVersion.class);
                            if (!topicConfigWrapper.getDataVersion().equals(nameServerDataVersion)) {
                                changed = true;
                            }
                        }
                    }
                    default:
                        break;
                }

            } catch (InterruptedException
                    | RemotingConnectException
                    | RemotingTimeoutException
                    | RemotingSendRequestException
                    | RemotingTooMuchRequestException
                    | RemotingCommandException e
            ) {
                e.printStackTrace();
            }
        }, 1000, 1000, TimeUnit.MILLISECONDS);


//        remotingClient.shutdown();
    }

}
