/**
 * Project Name pushServer
 * File Name KeepAliveMessageFactoryImpl.java
 * Package Name com.kvi.pushServer.filter
 * Create Time 2018年3月15日
 * Create by name：kvi
 * Copyright © 2015, 2017, kvi. All rights reserved.
 */
package com.kvi.cmpp.gateway.filter;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.keepalive.KeepAliveMessageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kvi.cmpp.gateway.stack.BaseMessage;
import com.kvi.cmpp.gateway.stack.MsgActiveTestResp;
import com.kvi.cmpp.gateway.stack.MsgCommand;
import com.kvi.cmpp.gateway.utils.Constants;

/**
 * ClassName: KeepAliveMessageFactoryImpl.java <br>
 * Description: <br>
 *
 * @author name：kvi <br>
 * @date: 2018年3月15日<br>
 */
public class KeepAliveMessageFactoryImpl implements KeepAliveMessageFactory {

    private static final Logger log = LoggerFactory.getLogger(KeepAliveMessageFactoryImpl.class);

    public boolean isRequest(IoSession session, Object message) {

        BaseMessage command = null;
        try {
            command = (BaseMessage) message;
        } catch (Exception e) {
            return false;
        }
        if (command.getMsgCommand() == MsgCommand.CMPP_ACTIVE_TEST) {
            log.info("<接收心跳请求, sessionId={}>", session.getId());
            return true;
        }
        return false;
    }

    public boolean isResponse(IoSession session, Object message) {
        return false;
    }

    public Object getRequest(IoSession session) {
        return null;
    }

    public Object getResponse(IoSession session, Object request) {
        BaseMessage requestMessage = (BaseMessage) request;
        log.debug("<响应心跳请求, sessionId={}, sequenceId={}>", session.getId(), requestMessage.getSequenceId());

        // CMPP_ACTIVE_TEST_RESP: totalLength(12+1) + commandId(0x80000008) + sequenceId + reserved(1)
        MsgActiveTestResp active = new MsgActiveTestResp();
        active.setTotalLength(Constants.MessageTotalLength.ACTIVE_TEST);
        active.setCommandId(MsgCommand.CMPP_ACTIVE_TEST_RESP);
        active.setSequenceId(requestMessage.getSequenceId());
        active.setReserved((byte) 0);
        return active.toByteArry();
    }

}
