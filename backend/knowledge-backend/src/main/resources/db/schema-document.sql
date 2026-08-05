-- ============================================================
-- AI Enterprise Knowledge Base - 文档处理链路
-- 阶段 1：数据库设计
-- 数据库：knowledge
-- ============================================================

USE knowledge;

-- ------------------------------------------------------------
-- 1. document  知识文档表（文件存储 + 处理状态）
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS document (
    id              BIGINT          NOT NULL AUTO_INCREMENT COMMENT '主键',
    user_id         BIGINT          NOT NULL                COMMENT '上传用户ID，关联 user.id',
    file_name       VARCHAR(255)    NOT NULL                COMMENT '原始文件名',
    file_type       VARCHAR(32)     NOT NULL                COMMENT '文件类型: pdf / docx / txt / md',
    file_size       BIGINT          NOT NULL                COMMENT '文件大小（字节）',
    file_hash       VARCHAR(64)                             COMMENT '文件 SHA-256 哈希，用于去重',
    storage_path    VARCHAR(512)    NOT NULL                COMMENT '文件磁盘存储路径',
    parsed_content  LONGTEXT                                COMMENT '解析后的全文（解析阶段写入）',
    status          VARCHAR(32)     NOT NULL DEFAULT 'UPLOADED' COMMENT '处理状态',
    error_message   VARCHAR(512)                            COMMENT '失败原因',
    chunk_count     INT             NOT NULL DEFAULT 0      COMMENT '切片数量',
    create_time     DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time     DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    INDEX idx_document_user_id (user_id),
    INDEX idx_document_status (status),
    INDEX idx_document_file_hash (file_hash)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='知识文档表';

-- ------------------------------------------------------------
-- 2. document_chunk  文档切片表（文本切分 + 向量关联）
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS document_chunk (
    id              BIGINT          NOT NULL AUTO_INCREMENT COMMENT '主键',
    document_id     BIGINT          NOT NULL                COMMENT '所属文档ID，关联 document.id',
    chunk_index     INT             NOT NULL                COMMENT '切片序号，从 0 开始',
    content         TEXT            NOT NULL                COMMENT '切片文本内容',
    token_count     INT                                     COMMENT 'Token 数量（Embedding 前统计）',
    vector_id       VARCHAR(64)                             COMMENT '向量库中的向量ID（Milvus/Qdrant 等）',
    create_time     DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (id),
    INDEX idx_chunk_document_id (document_id),
    UNIQUE KEY uk_document_chunk_index (document_id, chunk_index)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='文档切片表';

-- ------------------------------------------------------------
-- status 枚举说明（应用层维护，不入库枚举类型，便于扩展）
-- ------------------------------------------------------------
-- UPLOADED   : 文件已上传，等待处理
-- PARSING    : 正在读取/解析文件内容
-- PARSED     : 全文解析完成
-- CHUNKING   : 正在文本切分
-- CHUNKED    : 切分完成
-- EMBEDDING  : 正在向量化
-- COMPLETED  : 全流程完成（可参与 RAG 检索）
-- FAILED     : 处理失败（见 error_message）
