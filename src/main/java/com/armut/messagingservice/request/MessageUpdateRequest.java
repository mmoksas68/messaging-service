package com.armut.messagingservice.request;

import com.armut.messagingservice.entity.Message;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class MessageUpdateRequest {
    @NotNull
    @Positive
    private Long id;

    @NotBlank
    private String body;

    public Message toMessage() {
        Message message = new Message();
        message.setId(id);
        message.setBody(body);
        return message;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "MessageUpdateRequest{" +
                "id=" + id +
                ", body='" + body + '\'' +
                '}';
    }
}
