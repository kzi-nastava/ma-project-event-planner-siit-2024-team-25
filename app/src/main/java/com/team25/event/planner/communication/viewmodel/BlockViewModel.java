package com.team25.event.planner.communication.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.team25.event.planner.communication.api.BlockApi;
import com.team25.event.planner.communication.model.BlockUserRequestDTO;
import com.team25.event.planner.core.ConnectionParams;
import com.team25.event.planner.core.api.ResponseCallback;

public class BlockViewModel extends ViewModel {

    public final MutableLiveData<Long> currentUser = new MutableLiveData<>();

    public final MutableLiveData<Long> blockedUser = new MutableLiveData<>();

    private final MutableLiveData<Boolean> _isBlocked = new MutableLiveData<>();
    public LiveData<Boolean> isBlocked = _isBlocked;

    private final BlockApi _blockApi = ConnectionParams.blockApi;

    private final MutableLiveData<String> _serverError = new MutableLiveData<>();
    public final LiveData<String> serverError = _serverError;

    public void blockUser(){
        BlockUserRequestDTO requestDTO = BlockUserRequestDTO.builder()
                .blockerUserId(currentUser.getValue())
                .blockedUserId(blockedUser.getValue()).build();

        this._blockApi.blockUser(requestDTO).enqueue(new ResponseCallback<>(
                _isBlocked::postValue,
                _serverError,
                "BlockViewModel"
        ));
    }

    public void isChatBlocked() {
        this._blockApi.isBlocked(blockedUser.getValue()).enqueue(new ResponseCallback<>(
                _isBlocked::postValue,
                _serverError,
                "BlockViewModel"
        ));
    }
}
