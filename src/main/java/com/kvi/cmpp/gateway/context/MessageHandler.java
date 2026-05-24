/**
 * Project Name pushServer
 * File Name MessageHandler.java
 * Package Name com.kvi.pushServer.context
 * Create Time 2018年3月15日
 * Create by name：kvi
 * Copyright © 2015, 2017, kvi. All rights reserved.
 */
package com.kvi.cmpp.gateway.context;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kvi.cmpp.gateway.stack.BaseMessage;

/**
 * ClassName: MessageHandler.java <br>
 * Description: 服务端IO处理器<br>
 * @author name：kvi <br>
 * @date: 2018年3月15日<br>
 */
public class MessageHandler extends IoHandlerAdapter {
    private static final Logger log = LoggerFactory.getLogger(MessageHandler.class);

    protected static final String TSSESSIONKEY = "MPSession_Key";

    @Override
    public void sessionCreated(IoSession session) throws Exception {
        log.debug("<sessionCreated sessionId={}>", session.getId());
    }

    @Override
    public void sessionOpened(IoSession session) throws Exception {
        log.debug("<sessionOpened sessionId={}>", session.getId());
    }

    @Override
    public void sessionClosed(IoSession session) throws Exception {
        log.info("<sessionClosed sessionId={}>", session.getId());
        session.close(true);
    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
        log.debug("<sessionIdle sessionId={}, status={}>", session.getId(), status);
    }

    @Override
    public void messageSent(IoSession session, Object message) throws Exception {
        log.debug("<messageSent sessionId={}, message={}>", session.getId(),
                ToStringBuilder.reflectionToString(message));
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
        log.error("<exceptionCaught sessionId={}, message={}>", session.getId(), cause.getMessage(), cause);
    }

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        log.debug("<messageReceived sessionId={}, message={}>", session.getId(),
                ToStringBuilder.reflectionToString(message));
        //业务消息处理
        ActionHandler.process(session, (BaseMessage) message);

    }
}
