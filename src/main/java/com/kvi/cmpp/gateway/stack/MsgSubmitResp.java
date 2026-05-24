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

/** 
 * ClassName: MsgSubmitResp.java <br>
 * Description: Submit消息结构应答定义<br>
 * Create by: name：kvi <br>
 * Create Time: 2017年4月24日<br>
 */
public class MsgSubmitResp extends MsgHead {
    private long          msgId;
    private int           result;                                        //结果 0：正确 1：消息结构错 2：命令字错 3：消息序号重复 4：消息长度错 5：资费代码错 6：超过最大信息长 7：业务代码错 8：流量控制错 9：本网关不负责服务此计费号码 10：Src_Id错误 11：Msg_src错误 12：Fee_terminal_Id错误 13：Dest_terminal_Id错误

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
            logger.error("封装CMPP消息头二进制数组失败。");
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
