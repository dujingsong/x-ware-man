package cn.imadc.application.xwareman;

import cn.imadc.application.xwareman.base.config.NettyClientConfig;
import org.apache.rocketmq.common.protocol.body.TopicConfigSerializeWrapper;
import org.apache.rocketmq.common.protocol.header.namesrv.QueryDataVersionRequestHeader;
import org.apache.rocketmq.remoting.exception.RemotingConnectException;
import org.apache.rocketmq.remoting.exception.RemotingSendRequestException;
import org.apache.rocketmq.remoting.exception.RemotingTimeoutException;
import org.apache.rocketmq.remoting.exception.RemotingTooMuchRequestException;
import org.apache.rocketmq.remoting.protocol.RemotingCommand;

/**
 * <p>
 *
 * </p>
 *
 * @author 杜劲松
 * @since 2022-03-02
 */
public class XWareManInstance {

    public static void main(String[] args) throws RemotingConnectException, RemotingSendRequestException
            , RemotingTimeoutException, InterruptedException, RemotingTooMuchRequestException {
        NettyClientConfig nettyClientConfig = new NettyClientConfig();
        RocketMQNettyRemotingClient remotingClient = new RocketMQNettyRemotingClient(nettyClientConfig
                , null);
        remotingClient.start();
        QueryDataVersionRequestHeader requestHeader = new QueryDataVersionRequestHeader();
        requestHeader.setBrokerAddr("10.100.0.190:30921");
        requestHeader.setBrokerId(0L);
        requestHeader.setBrokerName("RaftNode00");
        requestHeader.setClusterName("RaftCluster");
        RemotingCommand remotingCommand = RemotingCommand.createRequestCommand(322, requestHeader);

        TopicConfigSerializeWrapper topicConfigWrapper = new TopicConfigSerializeWrapper();
        remotingCommand.setBody(topicConfigWrapper.getDataVersion().encode());

        RemotingCommand response = remotingClient.invokeSync("127.0.0.1:9876", remotingCommand, 3000);
        System.out.println(response);

        remotingClient.shutdown();
    }

}
