package cn.imadc.application.xwareman;

import org.apache.rocketmq.remoting.protocol.RemotingCommand;

public interface RemotingResponseCallback {
    void callback(RemotingCommand response);
}
