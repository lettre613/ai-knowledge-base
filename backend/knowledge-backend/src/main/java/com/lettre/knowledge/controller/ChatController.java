package com.lettre.knowledge.controller;


import com.lettre.knowledge.common.Result;
import com.lettre.knowledge.dto.ChatMessageRequest;
import com.lettre.knowledge.dto.ChatMessageResponse;
import com.lettre.knowledge.rag.RagService;
import com.lettre.knowledge.util.SecurityUtil;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/chat")
public class ChatController {


    private final RagService ragService;


    public ChatController(RagService ragService) {
        this.ragService = ragService;
    }


    @PostMapping("/message")
    public Result<ChatMessageResponse> message(
            @Valid
            @RequestBody ChatMessageRequest request
    ) {

        Long userId = SecurityUtil.getCurrentUserId();

        ChatMessageResponse response = ragService.chat(
                request.getQuestion(),
                userId
        );

        return Result.success(response);

    }


}
