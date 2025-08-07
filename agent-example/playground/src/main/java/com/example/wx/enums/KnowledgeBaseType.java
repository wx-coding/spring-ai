package com.example.wx.enums;

/**
 * @author wangxiang
 * @description KnowledgeBaseType
 * @create 2025/8/7 21:16
 */
public enum KnowledgeBaseType {
    BAILIAN("bailian", "百炼知识库"),
    PGVECTOR("pgvector", "PGVector向量库"),
    SIMPLE("simple", "内存向量库");

    private final String code;
    private final String description;

    KnowledgeBaseType(String code, String description) {
        this.code = code;
        this.description = description;
    }
    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}

