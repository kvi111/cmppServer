/**
 * Project Name cmpp-gateway
 * File Name package-info.java
 * Create Time 2018年3月19日
 * Create by name：kvi
 * Copyright © 2015, 2017, All rights reserved.
 */
package com.kvi.cmpp.gateway.codec;

import java.net.InetSocketAddress;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.kvi.cmpp.gateway.action.ActionService;
import com.kvi.cmpp.gateway.context.MessageFactory;
import com.kvi.cmpp.gateway.stack.BaseMessage;
import com.kvi.cmpp.gateway.stack.MsgCommand;
import com.kvi.cmpp.gateway.stack.MsgConnect;
import com.kvi.cmpp.gateway.stack.MsgHead;
import com.kvi.cmpp.gateway.stack.MsgDeliverResp;
import com.kvi.cmpp.gateway.stack.MsgSubmit;
import com.kvi.cmpp.gateway.utils.Utils;

/**
 * ClassName: MPMessageDecoder.java <br>
 * Description: <br>
 * Create by: name：kvi
 * Create Time: 2017年6月6日<br>
 */
public class MPMessageDecoder extends CumulativeProtocolDecoder {
    private static Logger logger   = LoggerFactory.getLogger(MPMessageDecoder.class);

    private String charset;

    public MPMessageDecoder() {
        this.setCharset("UTF-8");
    }

    public MPMessageDecoder(String charset) {
        this.setCharset(charset);
    }

    @Override
    protected boolean doDecode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {

        // 需要至少 12 字节来读取消息头（totalLength 4 + commandId 4 + sequenceId 4）
        if (in.remaining() < 12) {
            return false;
        }
        in.mark();

        Integer totalLength = in.getInt();
        if (totalLength < 12) {
            logger.error("无效的消息总长度: {}", totalLength);
            return false;
        }
        logger.debug("totalLength={}", totalLength);

        // 检查是否收到了完整的消息体
        if (in.remaining() < totalLength - 4) {
            // 数据不完整，等待更多数据
            in.reset();
            return false;
        }

        Integer commandId = in.getInt();
        logger.debug("commandId={}", commandId);
        Integer sequenceId = in.getInt();
        logger.debug("sequenceId={}", sequenceId);

        BaseMessage message = new BaseMessage();
        InetSocketAddress socketAddress = (InetSocketAddress) session.getRemoteAddress();
        message.setClientIp(socketAddress.getAddress().getHostAddress());
        message.setTotalLength(totalLength);
        message.setMsgCommand(commandId);
        message.setSequenceId(sequenceId);

        MsgHead head = new MsgHead(totalLength, commandId, sequenceId);
        ActionService service = MessageFactory.createService(commandId);
        switch (commandId) {
        	case MsgCommand.CMPP_ACTIVE_TEST:
        		logger.info("<{} active test,序列号：{}>" , Utils.getNowData(), sequenceId);
	            break;
            case MsgCommand.CMPP_CONNECT:
            	MsgConnect msgConnect = service.readMessage(in);
                msgConnect.setHead(head);
                logger.info("<{} 链接短信网关,version:{},序列号：{}>" , Utils.getNowData(), msgConnect.getVersion(), sequenceId);
                message.setBodys((JSONObject)JSONObject.toJSON(msgConnect));

                break;
            case MsgCommand.CMPP_SUBMIT:
            	MsgSubmit submitInfo = service.readMessage(in);
            	submitInfo.setHead(head);
                logger.info("<{} 提交短信,序列号：{}>" , Utils.getNowData(), sequenceId);
                message.setBodys((JSONObject)JSONObject.toJSON(submitInfo));
                break;
            case MsgCommand.CMPP_DELIVER_RESP:
                MsgDeliverResp deliverResp = service.readMessage(in);
                deliverResp.setHead(head);
                logger.info("<{} 接收 Deliver 应答消息,序列号：{}>" , Utils.getNowData(), sequenceId);
                message.setBodys((JSONObject)JSONObject.toJSON(deliverResp));
                break;
            default:
                logger.warn("未知命令ID: {}", commandId);
                break;
        }
        out.write(message);

        if(in.remaining() > 0){
            return true;
        }

        return false;
    }

    /**
     * @return the charset
     */
    public String getCharset() {
        return charset;
    }

    /**
     * @param charset the charset to set
     */
    public void setCharset(String charset) {
        this.charset = charset;
    }

}
