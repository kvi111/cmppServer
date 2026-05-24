/**
 * Project Name cmpp-gateway
 * File Name BaseService.java
 * Package Name com.kvi.cmpp.gateway.service
 * Create Time 2018年3月19日
 * Create by name：kvi
 * Copyright © 2015, 2017, kvi. All rights reserved.
 */
package com.kvi.cmpp.gateway.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kvi.cmpp.gateway.exception.GateWayException;
import com.kvi.cmpp.gateway.stack.ClientInfo;
import com.kvi.cmpp.gateway.stack.MsgConnect;
import com.kvi.cmpp.gateway.utils.ReadYamlUtils;

/** 
 * ClassName: BaseService.java <br>
 * Description: <br>
 * @author name：kvi <br>
 * Create Time: 2018年3月19日<br>
 */
public class BaseService {
    private static Logger logger = LoggerFactory.getLogger(BaseService.class);

    public static void checkConnectRequest(MsgConnect connectReq) {
        logger.debug("spIp={},AuthenticatorSource={}",connectReq.getSpIp(),connectReq.getAuthenticatorSource());
        List<ClientInfo> clientConfig = ReadYamlUtils.getClientConfig();
        boolean flag = clientConfig.get(0).getSpId().equals(connectReq.getSourceAddr());
        GateWayException.checkCondition(!flag, 0x0003, "认证失败");
        GateWayException.checkCondition(connectReq.getVersion() > ReadYamlUtils.getGatewayVersion(), 0x0004, "版本太高");
        

    }
}
