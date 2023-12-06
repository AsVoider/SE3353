package com.books.bkb.utils;

import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@ServerEndpoint("/public/WS/{UserId}")
public class WebSocket {
    public static AtomicInteger onlineCount = new AtomicInteger();
    private static final Map<String, Session> map = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session, @PathParam("UserId") String UserId) {
        if(map.get(UserId) != null) {
            return;
        }
        map.put(UserId, session);
        onlineCount.incrementAndGet();
        System.out.println("Online Now " + onlineCount);
    }

    @OnMessage
    public void onMessage(String message) {
        System.out.println("Received Message: " + message);
    }

    @OnClose
    public void onClose(@PathParam("UserId") String UserId) {
        map.remove(UserId);
        onlineCount.decrementAndGet();
        System.out.println("Online Now by Decrement: " + onlineCount);
    }

    @OnError
    public void onError(Session session, Throwable throwable) throws Exception {
        throwable.fillInStackTrace();
    }

    public void sendOneMessage(String UserId, String message) {
        var session = map.get(UserId);
        if(session == null) {
            System.out.println("Not Online");
        } else {
            try {
                session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
