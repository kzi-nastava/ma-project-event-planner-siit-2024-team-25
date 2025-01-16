package com.team25.event.planner.communication.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.team25.event.planner.communication.api.ChatRoomApi;
import com.team25.event.planner.communication.model.ChatMessage;
import com.team25.event.planner.communication.model.ChatRoom;
import com.team25.event.planner.core.ConnectionParams;
import com.team25.event.planner.core.ErrorParse;
import com.team25.event.planner.core.Page;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatRoomViewModel extends ViewModel {
    private final ChatRoomApi chatRoomApi = ConnectionParams.chatRoomApi;

    public final MutableLiveData<Long> senderId = new MutableLiveData<>();
    private int currentPage = 0;
    private int totalPages = 0;
    private final MutableLiveData<List<ChatRoom>> _chats = new MutableLiveData<>();
    public final LiveData<List<ChatRoom>> chats = _chats;
    private final MutableLiveData<Boolean> _isLoading = new MutableLiveData<>(false);
    public final LiveData<Boolean> isLoading = _isLoading;

    private final MutableLiveData<String> _serverError = new MutableLiveData<>();
    public final LiveData<String> serverError = _serverError;

    public void getChats(Long senderId){
        chatRoomApi.getChats(senderId,currentPage,10, "id", "desc").enqueue(new Callback<Page<ChatRoom>>() {
            @Override
            public void onResponse(Call<Page<ChatRoom>> call, Response<Page<ChatRoom>> response) {
                if(response.isSuccessful() && response.body()!= null){
                    _chats.postValue(response.body().getContent());
                    totalPages = response.body().getTotalPages();
                }else{
                  _serverError.postValue(ErrorParse.catchError(response));
                }
            }

            @Override
            public void onFailure(Call<Page<ChatRoom>> call, Throwable t) {
                _serverError.postValue("Network problem");
            }
        });
    }
    public void scrollDown(){
        if(currentPage > 0){
            currentPage--;
            getChats(senderId.getValue());
        }
    }
    public void scrollUp(){
        if(currentPage < totalPages-1){
            currentPage++;
            getChats(senderId.getValue());
        }
    }
}
