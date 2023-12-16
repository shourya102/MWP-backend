package com.mwp.mwp.payload.socket;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;

public class OnJoin {

    @JsonProperty(value = "displayName")
    private String displayName;

    @JsonProperty(value = "roomId")
    private String roomId;

    @JsonGetter
    public String getDisplayName() {
        return displayName;
    }

    @JsonSetter
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @JsonGetter
    public String getRoomId() {
        return roomId;
    }

    @JsonSetter
    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }
}
