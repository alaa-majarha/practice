package com.spring.soap.platform.mcp;

import org.springaicommunity.mcp.annotation.McpTool;
import org.springaicommunity.mcp.annotation.McpToolParam;
import org.springframework.stereotype.Service;

@Service
public class MyMcpTools {

    @McpTool(name = "sayHello", description = "Returns a greeting for the given name")
    public String sayHello(@McpToolParam(description = "Name to greet", required = true) String name) {
        return "Hello, " + name + "!";
    }
}
