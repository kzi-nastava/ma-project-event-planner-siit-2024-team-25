package com.team25.event.planner.communication.websocket;

import android.annotation.SuppressLint;
import android.util.Log;

import com.team25.event.planner.core.ConnectionParams;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;
import ua.naiksoftware.stomp.dto.StompMessage;

public class ChatMessageWebSocket {
    private static final String SOCKET_URL = "ws://" + ConnectionParams.BASE_URL.replace("http://", "") + "socket";
    private StompClient stompClient;
    public ChatMessageWebSocket(){}

    @SuppressLint("CheckResult")
    public void connect(){
        stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, SOCKET_URL);
        stompClient.connect();
        stompClient.lifecycle()
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(lifecycleEvent -> {
                    switch (lifecycleEvent.getType()){
                        case OPENED:
                            Log.d("WebSocketChat", "Stomp connected successfully", lifecycleEvent.getException());
                            break;
                        case ERROR:
                            Log.e("WebSocketChat", "Stomp connection error", lifecycleEvent.getException());
                            break;
                        case CLOSED:
                            break;
                        case FAILED_SERVER_HEARTBEAT:
                            break;
                    }
                });
    }

    public Flowable<StompMessage> subscribeToTopic(String topic) {
        return stompClient.topic(topic);
    }
    public void disconnect(){
        this.stompClient.disconnect();
    }
}
