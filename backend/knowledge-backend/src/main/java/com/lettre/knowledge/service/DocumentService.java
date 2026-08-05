package com.lettre.knowledge.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lettre.knowledge.dto.DocumentEmbedResponse;
import com.lettre.knowledge.dto.DocumentChunkResponse;
import com.lettre.knowledge.dto.DocumentDetailResponse;
import com.lettre.knowledge.dto.DocumentListItemResponse;
import com.lettre.knowledge.dto.DocumentParseResponse;
import com.lettre.knowledge.dto.DocumentUploadResponse;

import org.springframework.web.multipart.MultipartFile;



public interface DocumentService {


    DocumentUploadResponse upload(
            MultipartFile file,
            Long userId
    );


    Page<DocumentListItemResponse> page(
            Long userId,
            long page,
            long size
    );


    DocumentDetailResponse getById(
            Long id,
            Long userId
    );


    DocumentParseResponse parse(
            Long id,
            Long userId
    );


    DocumentChunkResponse chunk(
            Long id,
            Long userId
    );


    DocumentEmbedResponse embed(
            Long id,
            Long userId
    );


}
