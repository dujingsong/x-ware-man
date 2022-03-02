package cn.imadc.application.xwareman;

import org.apache.rocketmq.remoting.exception.RemotingCommandException;

public interface CommandCustomHeader {
    void checkFields() throws RemotingCommandException;
}
