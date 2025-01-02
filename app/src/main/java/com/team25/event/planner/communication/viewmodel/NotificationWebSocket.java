package com.team25.event.planner.communication.viewmodel;

import android.annotation.SuppressLint;
import android.util.Log;

import com.team25.event.planner.core.ConnectionParams;

import java.net.URI;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;
import ua.naiksoftware.stomp.dto.StompMessage;

public class NotificationWebSocket {
    private static final String SOCKET_URL = "ws://" + ConnectionParams.BASE_URL.replace("http://", "") + "/socket";
    private StompClient stompClient;

    public NotificationWebSocket() {
    }

    @SuppressLint("CheckResult")
    public void connect() {
        stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, SOCKET_URL);
        stompClient.connect();
        stompClient.lifecycle()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(lifecycleEvent -> {
                    switch (lifecycleEvent.getType()) {
                        case OPENED:
                            Log.d("WebSocket", "Stomp connected", lifecycleEvent.getException());
                            break;
                        case ERROR:
                            Log.e("WebSocket", "Stomp connection error", lifecycleEvent.getException());
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