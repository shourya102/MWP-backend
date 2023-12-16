package com.mwp.mwp.payload.socket;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Message {

    @JsonProperty
    private final String date = DateTimeFormatter.ofPattern("HH:mm").format(LocalDateTime.now());
    @JsonProperty
    private String name;
    @JsonProperty
    private String id;
    @JsonProperty
    private String message;

    public Message(String name, String id, String message) {
        this.name = name;
        this.id = id;
        this.message = message;
    }

    @JsonGetter
    public String getName() {
        return name;
    }

    @JsonSetter
    public void setName(String name) {
        this.name = name;
    }

    @JsonGetter
    public String getId() {
        return id;
    }

    @JsonSetter
    public void setId(String id) {
        this.id = id;
    }

    @JsonGetter
    public String getMessage() {
        return message;
    }

    @JsonSetter
    public void setMessage(String message) {
        this.message = message;
    }

    @JsonGetter
    public String getDate() {
        return date;
    }
}
