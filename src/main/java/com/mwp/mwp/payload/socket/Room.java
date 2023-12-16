package com.mwp.mwp.payload.socket;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;

import java.util.ArrayList;

public class Room {
    @JsonProperty
    private String roomId;

    @JsonProperty
    private ArrayList<Message> messages = new ArrayList<>();

    @JsonProperty
    private ArrayList<AnonymousUser> users = new ArrayList<>();

    @JsonGetter
    public String getRoomId() {
        return roomId;
    }

    @JsonSetter
    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    @JsonGetter
    public ArrayList<AnonymousUser> getUsers() {
        return users;
    }

    @JsonSetter
    public void setUsers(AnonymousUser user) {
        users.add(user);
    }

    public void removeUsers(AnonymousUser user) {
        users.remove(user);
    }

    @JsonSetter
    public void addMessage(Message message) {
        messages.add(message);
    }

    public void removeMessage(Message message) {
        messages.remove(message);
    }

    @JsonGetter
    public int size() {
        return users.size();
    }
}
