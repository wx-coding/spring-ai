package com.example.wx.config;

import com.example.wx.tools.DemoTool;
import io.modelcontextprotocol.server.McpServer;
import io.modelcontextprotocol.server.McpSyncServer;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpServerTransportProvider;
import org.springframework.ai.mcp.McpToolUtils;
import org.springframework.ai.support.ToolCallbacks;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author wangxiang
 * @description
 * @create 2025/6/27 21:09
 */
@Configuration
public class McpServerConfig {
    @Bean
    public DemoTool demoTool() {
        return new DemoTool();
    }

    @Bean
    public ToolCallbackProvider serverTools(DemoTool demoTool) {
        return ToolCallbackProvider.from(ToolCallbacks.from(demoTool));
        // return MethodToolCallbackProvider.builder().toolObjects(demoTool).build();
    }
}
