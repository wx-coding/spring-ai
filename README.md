# Spring Ai Alibaba
紧跟技术潮流，Spring Ai 学习实践，随心记

项目结构
```text
├─agent-example     // 智能体案例
│  └─flight-booking     // 机票预定
│
├─spring-ai-chat        // ai 聊天
│  ├─dashscope-chat     // 通义前问
│  └─deepseek-chat      // deepseek
│
├─spring-ai-prompt      // ai 提示词
│
├─spring-ai-rag         // rag模块
│  ├─rag-milvus         // milvus
│  └─rag-pgvector       // pgvector
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

