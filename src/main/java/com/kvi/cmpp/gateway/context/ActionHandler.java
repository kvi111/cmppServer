/**
 * Project Name pushServer
 * File Name ActionHandler.java
 * Package Name com.kvi.pushServer.context
 * Create Time 2018年3月15日
 * Create by name：kvi
 * Copyright © 2015, 2017, kvi. All rights reserved.
 */
package com.kvi.cmpp.gateway.context;

import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kvi.cmpp.gateway.action.ActionService;
import com.kvi.cmpp.gateway.stack.BaseMessage;


/** 
 * ClassName: ActionHandler.java <br>
 * Description: 消息分发器<br>
 * @author name：kvi <br>
 * @date: 2018年3月15日<br>
 */
public class ActionHandler {

    private static final Logger log = LoggerFactory.getLogger(ActionHandler.class);

    /**
     * Description：消息处理-消息分发
     * @param session
     * @param command
     * @throws Exception
     * @return void
     * @author name：kvi
     **/
    public static void process(IoSession session, BaseMessage command) throws Exception {

        ActionService service = MessageFactory.createService(command.getMsgCommand());
        if (service == null) {
            log.warn("<未找到命令 {} 对应的处理器, sessionId={}>", command.getMsgCommand(), session.getId());
            return;
        }
        log.debug("<分发消息 commandId={}, sequenceId={}, sessionId={}>",
                command.getMsgCommand(), command.getSequenceId(), session.getId());
        service.doProcess(session, command);

    }
}
