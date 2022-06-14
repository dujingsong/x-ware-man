package cn.imadc.application.xwareman;

import cn.imadc.application.xwareman.core.data.client.RocketMQClient;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.protocol.body.TopicList;
import org.apache.rocketmq.common.protocol.route.BrokerData;
import org.apache.rocketmq.common.protocol.route.TopicRouteData;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.apache.rocketmq.tools.admin.DefaultMQAdminExt;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

/**
 * <p>
 *
 * </p>
 *
 * @author 杜劲松
 * @since 2022-06-13
 */
public class SimpleTests {

    @Test
    public void testDefaultMQAdminExt() throws InterruptedException, MQClientException, RemotingException, MQBrokerException {
        RocketMQClient rocketMQClient = new RocketMQClient();
        DefaultMQAdminExt mqAdmin = rocketMQClient.getDefaultMQAdminExt("10.100.2.51:9876", "xsbankRocketmq", "xsbankP@ssw0rd");

        TopicList topicList = mqAdmin.fetchAllTopicList();

        Set<String> topicSet = topicList.getTopicList();

        for (String topic : topicSet) {

            TopicRouteData topicRouteData = mqAdmin.examineTopicRouteInfo(topic);
            List<BrokerData> brokerDataList = topicRouteData.getBrokerDatas();

            System.out.println(brokerDataList);

        }
    }
}
