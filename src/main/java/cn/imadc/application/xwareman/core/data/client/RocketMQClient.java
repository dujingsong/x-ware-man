package cn.imadc.application.xwareman.core.data.client;

import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.acl.common.AclClientRPCHook;
import org.apache.rocketmq.acl.common.SessionCredentials;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.remoting.RPCHook;
import org.apache.rocketmq.remoting.exception.RemotingConnectException;
import org.apache.rocketmq.remoting.exception.RemotingSendRequestException;
import org.apache.rocketmq.remoting.exception.RemotingTimeoutException;
import org.apache.rocketmq.tools.admin.DefaultMQAdminExt;

import java.io.UnsupportedEncodingException;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * <p>
 * RocketMQ通信客户端
 * </p>
 *
 * @author 杜劲松
 * @since 2022-05-30
 */
public class RocketMQClient {

    private final ConcurrentMap<String, DefaultMQAdminExt> mqAdminExtTables = new ConcurrentHashMap<>();
    protected final Lock lockMQAdminExtTables = new ReentrantLock();
    protected static final long LOCK_TIMEOUT_MILLIS = 3000L;

    /**
     * 获取DefaultMQAdminExt
     *
     * @param namesrvAddr Domain name mode access way does not support the delimiter(;), and only one domain name can be set.
     * @param accessKey   accessKey
     * @param secretKey   secretKey
     * @return DefaultMQAdminExt
     */
    public DefaultMQAdminExt getDefaultMQAdminExt(String namesrvAddr, String accessKey, String secretKey)
            throws InterruptedException, MQClientException {

        DefaultMQAdminExt defaultMQAdminExt = mqAdminExtTables.get(namesrvAddr);
        if (null != defaultMQAdminExt) return defaultMQAdminExt;

        if (!this.lockMQAdminExtTables.tryLock(LOCK_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)) {
            return null;
        }

        try {

            defaultMQAdminExt = createDefaultMQAdminExt(namesrvAddr, accessKey, secretKey);

            this.mqAdminExtTables.put(namesrvAddr, defaultMQAdminExt);

        } finally {
            this.lockMQAdminExtTables.unlock();
        }

        return defaultMQAdminExt;
    }

    /**
     * 创建DefaultMQAdminExt
     *
     * @param namesrvAddr namesrv地址
     * @param accessKey   accessKey
     * @param secretKey   secretKey
     * @return DefaultMQAdminExt
     * @throws MQClientException
     */
    private DefaultMQAdminExt createDefaultMQAdminExt(String namesrvAddr, String accessKey, String secretKey)
            throws MQClientException {

        RPCHook rpcHook = null;
        if (StringUtils.isNotEmpty(accessKey) && StringUtils.isNotEmpty(secretKey)) {
            rpcHook = new AclClientRPCHook(new SessionCredentials(accessKey, secretKey));
        }
        DefaultMQAdminExt defaultMQAdminExt = new DefaultMQAdminExt(rpcHook);
        defaultMQAdminExt.setInstanceName(Long.toString(System.currentTimeMillis()));
        defaultMQAdminExt.setNamesrvAddr(namesrvAddr);
        defaultMQAdminExt.start();

        return defaultMQAdminExt;
    }


    public static void main(String[] args) throws MQClientException, RemotingConnectException
            , RemotingSendRequestException, RemotingTimeoutException, MQBrokerException
            , InterruptedException, UnsupportedEncodingException {

        RocketMQClient rocketMQClient = new RocketMQClient();

        DefaultMQAdminExt defaultMQAdminExt
                = rocketMQClient.getDefaultMQAdminExt("10.100.2.51:9876", "rocketmq2", "12345678");
        Properties properties = defaultMQAdminExt.getBrokerConfig("10.100.12.187:30911");
        System.out.println(properties);
    }
}
