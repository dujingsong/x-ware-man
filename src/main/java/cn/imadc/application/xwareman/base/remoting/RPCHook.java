package cn.imadc.application.xwareman.base.remoting;

import org.apache.rocketmq.remoting.protocol.RemotingCommand;

public interface RPCHook {

    void doBeforeRequest(final String remoteAddress, final RemotingCommand request);

    void doAfterResponse(final String remoteAddress, final RemotingCommand request,
                         final RemotingCommand response);

}
