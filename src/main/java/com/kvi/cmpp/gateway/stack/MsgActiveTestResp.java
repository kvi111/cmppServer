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
 * ClassName: MsgActiveTestResp.java <br>
 * Description: 链路检查消息结构定义<br>
 * Create by: name：kvi <br>
 * Create Time: 2017年4月24日<br>
 */
public class MsgActiveTestResp extends MsgHead {
    private static Logger logger = Logger.getLogger(MsgActiveTestResp.class);
    private byte          reserved;

    public byte[] toByteArry() {
        ByteArrayOutputStream bous = new ByteArrayOutputStream();
        DataOutputStream dous = new DataOutputStream(bous);
        try {
            dous.writeInt(this.getTotalLength());
            dous.writeInt(this.getCommandId());
            dous.writeInt(this.getSequenceId());
            dous.writeByte(this.reserved);
            dous.close();
        } catch (IOException e) {
            logger.error("封装链接二进制数组失败。");
        }
        return bous.toByteArray();
    }

    public byte getReserved() {
        return reserved;
    }

    public void setReserved(byte reserved) {
        this.reserved = reserved;
    }
}
