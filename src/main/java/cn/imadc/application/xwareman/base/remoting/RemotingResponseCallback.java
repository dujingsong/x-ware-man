package cn.imadc.application.xwareman.base.remoting;

import org.apache.rocketmq.remoting.protocol.RemotingCommand;

public interface RemotingResponseCallback {
    void callback(RemotingCommand response);
}
