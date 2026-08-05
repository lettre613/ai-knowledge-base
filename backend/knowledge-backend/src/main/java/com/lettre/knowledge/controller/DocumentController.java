package com.lettre.knowledge.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lettre.knowledge.common.Result;
import com.lettre.knowledge.dto.DocumentEmbedResponse;
import com.lettre.knowledge.dto.DocumentChunkResponse;
import com.lettre.knowledge.dto.DocumentDetailResponse;
import com.lettre.knowledge.dto.DocumentListItemResponse;
import com.lettre.knowledge.dto.DocumentParseResponse;
import com.lettre.knowledge.dto.DocumentUploadResponse;
import com.lettre.knowledge.service.DocumentService;
import com.lettre.knowledge.util.SecurityUtil;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;



@RestController
@RequestMapping("/document")
public class DocumentController {


    private final DocumentService documentService;


    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }


    @PostMapping("/upload")
    public Result<DocumentUploadResponse> upload(
            @RequestParam("file") MultipartFile file
    ) {

        Long userId = SecurityUtil.getCurrentUserId();

        DocumentUploadResponse response = documentService.upload(file, userId);

        return Result.success(response);

    }


    @GetMapping("/page")
    public Result<Page<DocumentListItemResponse>> page(
            @RequestParam(defaultValue = "1") long page,
            @RequestParam(defaultValue = "10") long size
    ) {

        Long userId = SecurityUtil.getCurrentUserId();

        Page<DocumentListItemResponse> result = documentService.page(userId, page, size);

        return Result.success(result);

    }


    @GetMapping("/{id}")
    public Result<DocumentDetailResponse> getById(
            @PathVariable Long id
    ) {

        Long userId = SecurityUtil.getCurrentUserId();

        DocumentDetailResponse response = documentService.getById(id, userId);

        return Result.success(response);

    }


    @PostMapping("/{id}/parse")
    public Result<DocumentParseResponse> parse(
            @PathVariable Long id
    ) {

        Long userId = SecurityUtil.getCurrentUserId();

        DocumentParseResponse response = documentService.parse(id, userId);

        return Result.success(response);

    }


    @PostMapping("/{id}/chunk")
    public Result<DocumentChunkResponse> chunk(
            @PathVariable Long id
    ) {

        Long userId = SecurityUtil.getCurrentUserId();

        DocumentChunkResponse response = documentService.chunk(id, userId);

        return Result.success(response);

    }


    @PostMapping("/{id}/embed")
    public Result<DocumentEmbedResponse> embed(
            @PathVariable Long id
    ) {

        Long userId = SecurityUtil.getCurrentUserId();

        DocumentEmbedResponse response = documentService.embed(id, userId);

        return Result.success(response);

    }


}
