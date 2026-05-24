/**
 * Project Name cmpp-gateway
 * File Name DeliverAction.java
 * Package Name com.kvi.cmpp.gateway.action
 * Create Time 2018年8月30日
 * Create by name：kvi
 * Copyright © 2015, 2017, kvi. All rights reserved.
 */
package com.kvi.cmpp.gateway.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kvi.cmpp.gateway.stack.MsgDeliverResp;

/**
 * CMPP_DELIVER_RESP 处理类
 */
public class DeliverAction extends ActionFactoy {

    private final static Logger logger = LoggerFactory.getLogger(DeliverAction.class);

    @Override
    protected void exec() throws Exception {
        int result = message.getBodys().getIntValue("result");
        long msgId = message.getBodys().getLongValue("msgId");
        logger.info("<接收 Deliver 应答消息, msgId={}, result={}>", msgId, result);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected <T> T readMessage() throws Exception {
        MsgDeliverResp resp = new MsgDeliverResp();
        resp.setMsgId(ioBuffer.getLong());
        resp.setResult(ioBuffer.getInt());
        return (T) resp;
    }

}
