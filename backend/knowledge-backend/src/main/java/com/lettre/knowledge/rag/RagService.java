package com.lettre.knowledge.rag;


import com.lettre.knowledge.dto.ChatMessageResponse;



public interface RagService {


    ChatMessageResponse chat(
            String question,
            Long userId
    );


}
