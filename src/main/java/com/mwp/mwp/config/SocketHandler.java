package com.mwp.mwp.config;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mwp.mwp.payload.socket.AnonymousUser;
import com.mwp.mwp.payload.socket.Message;
import com.mwp.mwp.payload.socket.OnJoin;
import com.mwp.mwp.payload.socket.Room;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class SocketHandler {

    private final ConcurrentHashMap<String, AnonymousUser> anonUsers = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Room> rooms = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final SocketIOServer socketIOServer;

    public SocketHandler(SocketIOServer socketIOServer) {
        this.socketIOServer = socketIOServer;
        socketIOServer.addListeners(this);
    }

    private static void printLog(String header, SocketIOClient client, String room) {
        if (room == null) return;
        int size = 0;
        try {
            size = client.getNamespace().getRoomOperations(room).getClients().size();
        } catch (Exception e) {
            log.error("error ", e);
        }
        log.info("#ConncetedClients - {} => room: {}, count: {}", header, room, size);
    }

    @OnConnect
    public void onConnect(SocketIOClient client) {
        log.info("Client connected: {}", client.getSessionId());
        AnonymousUser newUser = new AnonymousUser();
        newUser.setSessionId(client.getSessionId().toString());
        anonUsers.put(client.getSessionId().toString(), newUser);
    }

    @OnDisconnect
    public void onDisconnect(SocketIOClient client) {
        String clientId = client.getSessionId().toString();
        log.info("Client disconnected: {}", clientId);
        Room room = null;
        Map<String, Object> parcel = null;
        if (anonUsers.get(clientId).getRoomId() != null) {
            room = rooms.get(anonUsers.get(clientId).getRoomId());
        }
        if (room != null) {
            room.removeUsers(anonUsers.get(clientId));
            if (room.size() == 0) rooms.remove(room.getRoomId());
            else {
                parcel = Map.of("user", anonUsers.get(clientId), "joined", false);
                log.info("{}", parcel);
                client.getNamespace().getRoomOperations(room.getRoomId()).sendEvent("roomCountChange", parcel);
            }
        }
        anonUsers.remove(clientId);
    }

    @OnEvent("joinRoom")
    public void onJoinRoom(SocketIOClient client, String o) throws JsonProcessingException {
        OnJoin onJoin = objectMapper.readValue(o, OnJoin.class);
        if (rooms.containsKey(onJoin.getRoomId())) {
            client.joinRoom(onJoin.getRoomId());
        } else {
            Room room = new Room();
            room.setRoomId(onJoin.getRoomId());
            rooms.put(onJoin.getRoomId(), room);
            client.joinRoom(onJoin.getRoomId());
        }
        AnonymousUser newUser = anonUsers.get(client.getSessionId().toString());
        newUser.setDisplayName(onJoin.getDisplayName());
        newUser.setRoomId(onJoin.getRoomId());
        rooms.get(onJoin.getRoomId()).setUsers(newUser);
        client.getNamespace().getRoomOperations(onJoin.getRoomId()).sendEvent("userJoined", objectMapper.writeValueAsString(newUser));
        client.sendEvent("joinRoom", o);
        Map<String, Object> parcel = Map.of("user", newUser, "joined", true);
        client.getNamespace().getRoomOperations(onJoin.getRoomId()).sendEvent("roomCountChange", parcel);
    }

    @OnEvent("userCall")
    public void onCall(SocketIOClient client, Map<String, Object> payload) {
        String clientId = (String) payload.get("to");
        String roomId = anonUsers.get(clientId).getRoomId();
        Map<String, Object> newPayload = Map.of("from", client.getSessionId().toString(), "offer", payload.get("offer"));
        socketIOServer.getClient(UUID.fromString(clientId)).sendEvent("incomingCall", newPayload);
    }

    @OnEvent("callAccepted")
    public void callAccepted(SocketIOClient client, Map<String, Object> payload) {
        String clientId = (String) payload.get("to");
        String roomId = anonUsers.get(clientId).getRoomId();
        Map<String, Object> newPayload = Map.of("from", client.getSessionId().toString(), "ans", payload.get("ans"));
        socketIOServer.getClient(UUID.fromString(clientId)).sendEvent("callAccepted", newPayload);
    }

    @OnEvent("peerNegotiationNeeded")
    public void negotiationNeeded(SocketIOClient client, Map<String, Object> payload) {
        String clientId = (String) payload.get("to");
        Map<String, Object> newPayload = Map.of("from", client.getSessionId().toString(), "offer", payload.get("offer"));
        socketIOServer.getClient(UUID.fromString(clientId)).sendEvent("peerNegotiationNeeded", newPayload);
    }

    @OnEvent("peerNegotiationDone")
    public void negotiationDone(SocketIOClient client, Map<String, Object> payload) {
        String clientId = (String) payload.get("to");
        Map<String, Object> newPayload = Map.of("from", client.getSessionId().toString(), "ans", payload.get("ans"));
        socketIOServer.getClient(UUID.fromString(clientId)).sendEvent("peerNegotiationFinal", newPayload);
    }

    @OnEvent("notifyPeer")
    public void negotiationCompleted(SocketIOClient client, Map<String, Object> payload) {
        String clientId = (String) payload.get("to");
        String name = anonUsers.get(clientId).getDisplayName();
        log.info("sent to: {}", name);
        Map<String, Object> newPayload = Map.of("message", "Send stream");
        socketIOServer.getClient(UUID.fromString(clientId)).sendEvent("peerNegotiationCompleted", newPayload);
    }

    @OnEvent("textMessage")
    public void messageSent(SocketIOClient client, String payload) {
        String name = anonUsers.get(client.getSessionId().toString()).getDisplayName();
        Message message = new Message(name, client.getSessionId().toString(), payload);
        String roomId = anonUsers.get(client.getSessionId().toString()).getRoomId();
        rooms.get(roomId).addMessage(message);
        client.getNamespace().getRoomOperations(roomId).sendEvent("messageReceived", message);
    }

    @OnEvent("getAllUsersInRoom")
    public void getAllUsers(SocketIOClient client) {
        String clientId = client.getSessionId().toString();
        String roomId = anonUsers.get(clientId).getRoomId();
        client.sendEvent("allUsers", rooms.get(roomId).getUsers());
    }

    //screen change calls

    @OnEvent("linkChanged")
    public void sendChangedLink(SocketIOClient client, String payload) {
        String clientId = client.getSessionId().toString();
        String room = anonUsers.get(clientId).getRoomId();
        client.getNamespace().getRoomOperations(room).sendEvent("linkChangeAccept", payload);
    }

    @OnEvent("play")
    public void sendPlay(SocketIOClient client) {
        String clientId = client.getSessionId().toString();
        String room = anonUsers.get(clientId).getRoomId();
        client.getNamespace().getRoomOperations(room).sendEvent("play");
    }

    @OnEvent("pause")
    public void sendPause(SocketIOClient client) {
        String clientId = client.getSessionId().toString();
        String room = anonUsers.get(clientId).getRoomId();
        client.getNamespace().getRoomOperations(room).sendEvent("pause");
    }
}
