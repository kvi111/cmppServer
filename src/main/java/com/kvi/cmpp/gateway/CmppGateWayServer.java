/**
 * Project Name cmpp-gateway
 * File Name MCPPServer.java
 * Package Name com.kvi.cmpp.gateway
 * Create Time 2018年3月19日
 * Create by name：kvi
 * Copyright © 2015, 2017, kvi. All rights reserved.
 */
package com.kvi.cmpp.gateway;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import com.kvi.cmpp.gateway.acceptor.CmccAcceptor;
import com.kvi.cmpp.gateway.stack.ClientInfo;
import com.kvi.cmpp.gateway.utils.EhCache;
import com.kvi.cmpp.gateway.utils.ReadYamlUtils;

/** 
 * ClassName: MCPPServer.java <br>
 * Description: <br>
 * @author name：kvi <br>
 * Create Time: 2018年3月19日<br>
 */
@ComponentScan(basePackages ="com.kvi.cmpp.gateway")
@SpringBootApplication
public class CmppGateWayServer {
    private static final Logger logger = LoggerFactory.getLogger(CmppGateWayServer.class);
    
    public static void main(String[] args) {
        SpringApplication.run(CmppGateWayServer.class, args);
        logger.info("<start server ...>");
        List<ClientInfo> clientConfig = ReadYamlUtils.getClientConfig();
        for (ClientInfo clientInfo : clientConfig) {
        	EhCache.put(EhCache.CACHE_NAME, clientInfo.getSpIp(), clientInfo);
		}
        CmccAcceptor acceptor = new CmccAcceptor();
        acceptor.start();
    }
    
}
