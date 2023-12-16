package com.mwp.mwp.payload.socket;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class AnonymousUser {

    @JsonProperty
    private String displayName;

    @JsonProperty
    private String sessionId;

    @JsonProperty
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
    public String getSessionId() {
        return sessionId;
    }

    @JsonSetter
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
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
