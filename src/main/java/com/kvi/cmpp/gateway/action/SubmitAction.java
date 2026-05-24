/**
 * Project Name cmpp-gateway
 * File Name ConnectAction.java
 * Package Name com.kvi.cmpp.gateway.action
 * Create Time 2018年8月30日
 * Create by name：kvi
 * Copyright © 2015, 2017, kvi. All rights reserved.
 */
package com.kvi.cmpp.gateway.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kvi.cmpp.gateway.stack.MsgCommand;
import com.kvi.cmpp.gateway.stack.MsgDeliver;
import com.kvi.cmpp.gateway.stack.MsgSubmit;
import com.kvi.cmpp.gateway.stack.MsgSubmitResp;
import com.kvi.cmpp.gateway.utils.Constants;
import com.kvi.cmpp.gateway.utils.MsgUtils;

/**
 * ClassName: ConnectAction.java <br>
 * Description: <br>
 * Create by: name：kvi <br>
 * Create Time: 2018年8月30日<br>
 */
public class SubmitAction extends ActionFactoy {

	private final static Logger logger = LoggerFactory.getLogger(SubmitAction.class);

	/* (non-Javadoc)
	 * @see com.kvi.cmpp.gateway.action.ActionFactoy#exec()
	 */
	@Override
	protected void exec() throws Exception {
		logger.info("<接收 submit 消息>");
		logger.debug("<submit消息体: {}>", message.getBodys() != null ? message.getBodys().toJSONString() : "null");

		// 网关生成唯一 msgId（客户端提交的 msgId 为 0，不能直接使用）
		long msgId = MsgUtils.getMsgId();
		logger.info("<生成网关msgId: {}>", msgId);

		MsgSubmitResp resp = new MsgSubmitResp();
		resp.setCommandId(MsgCommand.CMPP_SUBMIT_RESP);
		resp.setSequenceId(message.getSequenceId());
		resp.setTotalLength(Constants.MessageTotalLength.SUBMIT_REST);
		resp.setMsgId(msgId);
		resp.setResult(Constants.RESP_SUCCESS);
		session.write(resp.toByteArry());

		// 发送状态报告
		String destTerminalId = message.getBodys().getString("destTerminalId");
		if (destTerminalId != null) {
			destTerminalId = destTerminalId.replace("\0", "").trim();
		}
		String srcId = message.getBodys().getString("srcId");
		if (srcId != null) {
			srcId = srcId.replace("\0", "").trim();
		}

		MsgDeliver deliver = new MsgDeliver();
		deliver.setTotalLength(12 + 8 + 21 + 10 + 1 + 1 + 1 + 32 + 1 + 1 + 1 + 71 + 20); // 标准CMPP_DELIVER头 + 状态报告内容
		deliver.setCommandId(MsgCommand.CMPP_DELIVER);
		deliver.setSequenceId(MsgUtils.getSequence());

		// 状态报告内容，使用网关生成的 msgId
		deliver.setMsgId(msgId);
		deliver.setStat("DELIVRD");
		deliver.setSubTime(MsgUtils.getReportTimestamp());
		deliver.setDoneTime(MsgUtils.getReportTimestamp());
		deliver.setDestTerminalId(destTerminalId != null ? destTerminalId : "");
		deliver.setSmscSequence(MsgUtils.getSequence());

		// 外层消息字段
		deliver.setDestId(srcId != null ? srcId : "");  // SP的服务代码
		deliver.setServiceId(message.getBodys().getString("serviceId"));
		deliver.setSrcTerminalId(destTerminalId != null ? destTerminalId : ""); // 发送方号码
		deliver.setRegisteredDelivery((byte) 1); // 状态报告

		logger.info("<发送状态报告 DELIVRD, msgId={}, 目标号码: {}>", deliver.getMsgId(), destTerminalId);
		session.write(deliver.toByteArry());
	}

	/* (non-Javadoc)
	 * @see com.kvi.cmpp.gateway.action.ActionFactoy#readMessage()
	 */
	@SuppressWarnings({ "unchecked" })
	@Override
	protected <T> T readMessage() throws Exception {
        MsgSubmit submit = new MsgSubmit();

		//12+8+1+1+1+1+10+1+32+1+1+1+1+6+2+6+17+17+21+1+32+1+1+msg.getBytes("GBK").length+20
		long msgId = ioBuffer.getLong();//8
		submit.setMsgId(msgId);
		logger.info("<读取到submit msgId: {}>", msgId);
		this.rawMessage = submit; // 保存原始对象供exec使用
		submit.setPkTotal(ioBuffer.get());//1
		submit.setPkNumber(ioBuffer.get());//1
		submit.setRegisteredDelivery(ioBuffer.get());//1
		submit.setMsgLevel(ioBuffer.get());//1
		byte[] serviceId = new byte[10];
		ioBuffer.get(serviceId);
		submit.setServiceId(new String(serviceId)); //10
		submit.setFeeUserType(ioBuffer.get());
		byte[] feeTerminalId = new byte[32];
		ioBuffer.get(feeTerminalId);
		submit.setFeeTerminalId(new String(feeTerminalId)); //32
		submit.setFeeTerminalType(ioBuffer.get()); //1
		submit.setTpPId(ioBuffer.get()); //1
		submit.setTpUdhi(ioBuffer.get()); //1
		submit.setMsgFmt(ioBuffer.get()); //1
		byte[] msgRsc = new byte[6];
		ioBuffer.get(msgRsc);
		submit.setMsgSrc(new String(msgRsc));//6
		byte[] feeType = new byte[2];
		ioBuffer.get(feeType);
		submit.setFeeType(new String(feeType));//2
		byte[] feeCode = new byte[6];
		ioBuffer.get(feeCode);
		submit.setFeeCode(new String(feeCode));//6
		byte[] ValId_Time = new byte[17];
		ioBuffer.get(ValId_Time);
		submit.setValIdTime(new String(ValId_Time));//17
		byte[] At_Time = new byte[17];
		ioBuffer.get(At_Time);
		submit.setAtTime(new String(At_Time));//17
		byte[] src_id = new byte[21];
		ioBuffer.get(src_id);
		int srcLen = 0;
		while (srcLen < src_id.length && src_id[srcLen] != 0) srcLen++;
		submit.setSrcId(new String(src_id, 0, srcLen));//21
		byte DestUsrTl = ioBuffer.get();
		submit.setDestUsrTl(DestUsrTl); //1
		Integer msisdnLength = (int) DestUsrTl * 32;
		byte[] Dest_terminal_Id = new byte[msisdnLength];
		ioBuffer.get(Dest_terminal_Id);
		int dstLen = 0;
		while (dstLen < Dest_terminal_Id.length && Dest_terminal_Id[dstLen] != 0) dstLen++;
		submit.setDestTerminalId(new String(Dest_terminal_Id, 0, dstLen));//32*DestUsrTl
		submit.setDestTerminalType(ioBuffer.get());//1
		byte messageLength = ioBuffer.get();
		submit.setMsgLength(messageLength);

		byte[] Msg_Content = new byte[messageLength];
		ioBuffer.get(Msg_Content);
		submit.setMsgContent(new String(Msg_Content,"GBK"));
		byte[] LinkID = new byte[20];
		ioBuffer.get(LinkID);
		int linkLen = 0;
		while (linkLen < LinkID.length && LinkID[linkLen] != 0) linkLen++;
		submit.setLinkID(new String(LinkID, 0, linkLen));

		return (T) submit;

	    }

}
