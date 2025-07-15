# Spring Ai Alibaba
紧跟技术潮流，Spring Ai 学习实践，随心记

项目结构
```text
├─agent-example     // 智能体案例
│   └─flight-booking     // 机票预定
│
├─spring-ai-chat        // ai 聊天
│   ├─dashscope-chat     // 通义千问
│   ├─dashscope-chat-memory     // 聊天记录保存 内存、Mysql、Postgres   
│   ├─deepseek-chat      // deepseek
│   └─multiple-chat
│
├─spring-ai-mcp         // Spring MCP
│   ├─mcp-client        // MCP Client
│   └─mcp-server        // MCP Server stdio/sse
|
├─spring-ai-nacos           // 集成 nacos
│   ├─nacos-mcp-client      // 从 nacos 发现 mcp server **存在bug**
│   ├─nacos-mcp-server      // mcp server 注册到 nacos **存在bug**
│   └─nacos-prompt          // nacos 存储提示词
|
├─spring-ai-observability       // 可观测性最佳实践 待补全
|
├─spring-ai-prompt      // ai 提示词
│
├─spring-ai-rag         // rag模块
|   ├─rag-bailian-knowledge     // ali bailian 知识库 **存在bug**
│   ├─rag-etl       // ETL 文档解析
│   ├─rag-milvus         // milvus
│   └─rag-pgvector       // pgvector
│
├─spring-ai-structured  // 结构化输出
│
└─spring-ai-tool-calling    // 工具调用
```
需要在根目录下创建`.env`文件

```text
DASH_SCOPE_API_KEY=sk-**
DEEPSEEK_API_KEY=sk-***
DEEPSEEK_BASE_URL=https://api.deepseek.com
```

