/**
 * Project Name cmpp-gateway
 * File Name package-info.java
 * Package Name com.kvi.cmpp.gateway.stack
 * Create Time 2018年3月19日
 * Create by name：kvi
 * Copyright © 2015, 2017, kvi. All rights reserved.
 */
package com.kvi.cmpp.gateway.stack;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.apache.log4j.Logger;

/**
 * CMPP_DELIVER_RESP 消息结构定义（下发短信应答）
 */
public class MsgDeliverResp extends MsgHead {
    private static Logger logger = Logger.getLogger(MsgDeliverResp.class);

    private long msgId;
    private int  result;

    public byte[] toByteArry() {
        ByteArrayOutputStream bous = new ByteArrayOutputStream();
        DataOutputStream dous = new DataOutputStream(bous);
        try {
            dous.writeInt(this.getTotalLength());
            dous.writeInt(this.getCommandId());
            dous.writeInt(this.getSequenceId());
            dous.writeLong(this.msgId);
            dous.writeInt(this.result);
            dous.close();
        } catch (IOException e) {
            logger.error("封装CMPPDELIVERRESP消息二进制数组失败。");
        }
        return bous.toByteArray();
    }

    public long getMsgId() {
        return msgId;
    }

    public void setMsgId(long msgId) {
        this.msgId = msgId;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }
}
