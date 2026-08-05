package com.lettre.knowledge.dto;


import java.util.List;



public class ChatMessageResponse {


    private String answer;

    private List<ChatSourceResponse> sources;


    public String getAnswer() {
        return answer;
    }


    public void setAnswer(String answer) {
        this.answer = answer;
    }


    public List<ChatSourceResponse> getSources() {
        return sources;
    }


    public void setSources(List<ChatSourceResponse> sources) {
        this.sources = sources;
    }


}
