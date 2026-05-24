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
 * CMPP_DELIVER 消息结构定义（短信下发/状态报告）
 *
 * 状态报告消息体格式（Registered_Delivery=1）：
 * - Msg_Id (8字节)
 * - Dest_Id (21字节)
 * - Service_Id (10字节)
 * - TP_pid (1字节)
 * - TP_udhi (1字节)
 * - Msg_Fmt (1字节)
 * - Src_terminal_Id (32字节)
 * - Src_terminal_type (1字节)
 * - Registered_Delivery (1字节) = 1
 * - Msg_Length (1字节)
 * - Msg_Content (Msg_Length字节，状态报告内容)
 * - LinkID (20字节)
 *
 * 状态报告内容格式：
 * - Msg_Id (8字节) - 对应Submit消息的MsgId
 * - Stat (7字节) - 状态，如"DELIVRD"
 * - Submit_time (10字节) - 格式YYMMDDHHMM
 * - Done_time (10字节) - 格式YYMMDDHHMM
 * - Dest_terminal_Id (32字节) - 目的终端标识
 * - SMSC_sequence (4字节) - 短信中心序列号
 */
public class MsgDeliver extends MsgHead {
    private static Logger logger = Logger.getLogger(MsgDeliver.class);

    // 状态报告内容字段
    private long   msgId;           // 对应Submit消息的MsgId
    private String stat;            // 状态
    private String subTime;         // 提交时间 YYMMDDHHMM
    private String doneTime;        // 完成时间 YYMMDDHHMM
    private String destTerminalId;  // 目的终端标识
    private int    smscSequence;    // 短信中心序列号

    // 外层消息字段
    private String destId;          // 目的号码（SP的服务代码）
    private String serviceId;       // 业务标识
    private byte   tpPid = 0;
    private byte   tpUdhi = 0;
    private byte   msgFmt = 0;
    private String srcTerminalId;   // 源终端标识（发送方号码）
    private byte   srcTerminalType = 0;
    private byte   registeredDelivery = 1; // 1表示状态报告
    private String linkId = "";     // 链接标识

    public byte[] toByteArry() {
        ByteArrayOutputStream bous = new ByteArrayOutputStream();
        DataOutputStream dous = new DataOutputStream(bous);
        try {
            // 消息头
            dous.writeInt(this.getTotalLength());
            dous.writeInt(this.getCommandId());
            dous.writeInt(this.getSequenceId());

            // 构建状态报告内容 (71字节)
            ByteArrayOutputStream contentBous = new ByteArrayOutputStream();
            DataOutputStream contentDous = new DataOutputStream(contentBous);
            contentDous.writeLong(this.msgId);
            contentDous.write(getFixedBytes(this.stat, 7));
            contentDous.write(getFixedBytes(this.subTime, 10));
            contentDous.write(getFixedBytes(this.doneTime, 10));
            contentDous.write(getFixedBytes(this.destTerminalId, 32));
            contentDous.writeInt(this.smscSequence);
            contentDous.close();
            byte[] msgContent = contentBous.toByteArray();

            // 消息体
            dous.writeLong(this.msgId);  // Msg_Id (8字节)
            dous.write(getFixedBytes(this.destId, 21));      // Dest_Id (21字节)
            dous.write(getFixedBytes(this.serviceId, 10));   // Service_Id (10字节)
            dous.writeByte(this.tpPid);                      // TP_pid (1字节)
            dous.writeByte(this.tpUdhi);                     // TP_udhi (1字节)
            dous.writeByte(this.msgFmt);                     // Msg_Fmt (1字节)
            dous.write(getFixedBytes(this.srcTerminalId, 32)); // Src_terminal_Id (32字节)
            dous.writeByte(this.srcTerminalType);            // Src_terminal_type (1字节)
            dous.writeByte(this.registeredDelivery);         // Registered_Delivery (1字节)
            dous.writeByte((byte) msgContent.length);        // Msg_Length (1字节)
            dous.write(msgContent);                          // Msg_Content
            dous.write(getFixedBytes(this.linkId, 20));      // LinkID (20字节)

            dous.close();
        } catch (IOException e) {
            logger.error("封装CMPPDELIVER消息二进制数组失败。", e);
        }
        return bous.toByteArray();
    }

    private byte[] getFixedBytes(String s, int len) {
        byte[] bytes = new byte[len];
        if (s != null) {
            byte[] src = s.getBytes();
            for (int i = 0; i < src.length && i < len; i++) {
                bytes[i] = src[i];
            }
        }
        return bytes;
    }

    // Getters and Setters
    public long getMsgId() {
        return msgId;
    }

    public void setMsgId(long msgId) {
        this.msgId = msgId;
    }

    public String getStat() {
        return stat;
    }

    public void setStat(String stat) {
        this.stat = stat;
    }

    public String getSubTime() {
        return subTime;
    }

    public void setSubTime(String subTime) {
        this.subTime = subTime;
    }

    public String getDoneTime() {
        return doneTime;
    }

    public void setDoneTime(String doneTime) {
        this.doneTime = doneTime;
    }

    public String getDestTerminalId() {
        return destTerminalId;
    }

    public void setDestTerminalId(String destTerminalId) {
        this.destTerminalId = destTerminalId;
    }

    public int getSmscSequence() {
        return smscSequence;
    }

    public void setSmscSequence(int smscSequence) {
        this.smscSequence = smscSequence;
    }

    public String getDestId() {
        return destId;
    }

    public void setDestId(String destId) {
        this.destId = destId;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public byte getTpPid() {
        return tpPid;
    }

    public void setTpPid(byte tpPid) {
        this.tpPid = tpPid;
    }

    public byte getTpUdhi() {
        return tpUdhi;
    }

    public void setTpUdhi(byte tpUdhi) {
        this.tpUdhi = tpUdhi;
    }

    public byte getMsgFmt() {
        return msgFmt;
    }

    public void setMsgFmt(byte msgFmt) {
        this.msgFmt = msgFmt;
    }

    public String getSrcTerminalId() {
        return srcTerminalId;
    }

    public void setSrcTerminalId(String srcTerminalId) {
        this.srcTerminalId = srcTerminalId;
    }

    public byte getSrcTerminalType() {
        return srcTerminalType;
    }

    public void setSrcTerminalType(byte srcTerminalType) {
        this.srcTerminalType = srcTerminalType;
    }

    public byte getRegisteredDelivery() {
        return registeredDelivery;
    }

    public void setRegisteredDelivery(byte registeredDelivery) {
        this.registeredDelivery = registeredDelivery;
    }

    public String getLinkId() {
        return linkId;
    }

    public void setLinkId(String linkId) {
        this.linkId = linkId;
    }
}
