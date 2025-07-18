package com.team25.event.planner.communication.viewmodel;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.team25.event.planner.communication.api.ChatApi;
import com.team25.event.planner.communication.model.ChatMessage;
import com.team25.event.planner.communication.model.ChatMessageRequestDTO;
import com.team25.event.planner.communication.websocket.ChatMessageWebSocket;
import com.team25.event.planner.core.ConnectionParams;
import com.team25.event.planner.core.ErrorParse;
import com.team25.event.planner.core.Page;
import com.team25.event.planner.user.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyChatMessageViewModel extends ViewModel {
    private ChatApi chatApi = ConnectionParams.chatApi;
    private ChatMessageWebSocket chatMessageWebSocket;
    private final MutableLiveData<ChatMessage> newMessageLiveData = new MutableLiveData<>();
    public LiveData<ChatMessage> getNewMessageLiveData() {
        return newMessageLiveData;
    }

    @SuppressLint("CheckResult")
    public void connectToSocket(User user){
        chatMessageWebSocket = new ChatMessageWebSocket();
        chatMessageWebSocket.connect();
        chatMessageWebSocket.subscribeToTopic("/topic/" + chatId)
                .subscribe(stompMessage -> {
                    ChatMessage chatMessage = new Gson().fromJson(stompMessage.getPayload(), ChatMessage.class);

                            List<ChatMessage> currentList = _chatMessages.getValue();

                            if (currentList == null) {
                                currentList = new ArrayList<>();
                            }

                            boolean alreadyExists = true;
                            if(!currentList.isEmpty()){
                                alreadyExists = currentList.stream()
                                        .anyMatch(msg -> msg.getId().equals(chatMessage.getId()));
                            }


                            if (!alreadyExists) {
                                currentList.add(0, chatMessage);

                                while (currentList.size() > 7) {
                                    currentList.remove(currentList.size() - 1);
                                    _totalPages += 1;
                                }

                                _chatMessages.postValue(currentList);
                            }

                    Log.d(chatMessage.getContent(), "CHAT");
                },
                        throwable -> {
                            Log.e("WebSocket", "Error receiving message", throwable);
                        });

    }

    @SuppressLint("CheckResult")
    public void sendMessage() {
        if (!Objects.equals(message.getValue(), "") && chatMessageWebSocket.stompClient != null && chatMessageWebSocket.stompClient.isConnected()) {
            String destination = "/app/chat";
            String payload = new Gson().toJson(new ChatMessageRequestDTO(senderId.getValue(), receiverId.getValue(), message.getValue(), chatId));

            chatMessageWebSocket.stompClient.send(destination, payload)
                    .subscribe(() -> {
                        sendMessage.postValue(true);
                    }, throwable -> {
                        sendMessage.postValue(false);
                    });
        } else {
            sendMessage.postValue(false);
        }
    }

    public final MutableLiveData<String> message = new MutableLiveData<>();
    public final MutableLiveData<Boolean> sendMessage = new MutableLiveData<>();
    private int _currentPage = 0;
    private int _totalPages = 0;
    public String chatId = "";
    public final MutableLiveData<Long> senderId = new MutableLiveData<>();
    public final MutableLiveData<Long> receiverId = new MutableLiveData<>();
    public final MutableLiveData<String> receiverName = new MutableLiveData<>();

    private final MutableLiveData<Boolean> _isLoading = new MutableLiveData<>(false);
    public final LiveData<Boolean> isLoading = _isLoading;

    private final MutableLiveData<String> _serverError = new MutableLiveData<>();
    public final LiveData<String> serverError = _serverError;

    private final  MutableLiveData<List<ChatMessage>> _chatMessages= new MutableLiveData<>();
    public final LiveData<List<ChatMessage>> chatMessages = _chatMessages;

    public boolean isLoading() {
        return isLoading.getValue() == null || isLoading.getValue();
    }

    public void setCurrentPage(int n){
        _currentPage = n;
    }
    private final MutableLiveData<Boolean> _canOpenConnection = new MutableLiveData<>();
    public final LiveData<Boolean> canOpenConnection = _canOpenConnection;

    public void getChat(Long senderId, Long receiverId){
        if(isLoading())return;
        _isLoading.postValue(true);
        chatApi.getChatMessages(senderId, receiverId, _currentPage, 7,"timestamp", "desc").enqueue(new Callback<Page<ChatMessage>>() {
            @Override
            public void onResponse(Call<Page<ChatMessage>> call, Response<Page<ChatMessage>> response) {
                if(response.isSuccessful() && response.body()!=null){
                    _chatMessages.postValue(response.body().getContent());//fill front
                    chatId = response.body().getContent().get(0).getChatId();
                    _canOpenConnection.setValue(true);
                    _isLoading.postValue(false);
                    _totalPages = response.body().getTotalPages();
                }else{
                    _serverError.postValue(ErrorParse.catchError(response));
                }
            }

            @Override
            public void onFailure(Call<Page<ChatMessage>> call, Throwable t) {
                _serverError.postValue("Network problem: chat");
            }
        });
    }

    public void scrollDown(){
        if(_currentPage > 0){
            _currentPage--;
            getChat(senderId.getValue(), receiverId.getValue());
        }
    }
    public void scrollUp(){
        if(_currentPage < _totalPages-1){
            _currentPage++;
            getChat(senderId.getValue(),receiverId.getValue());
        }
    }
}
