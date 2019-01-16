package io.github.hunachi.tsugidoko

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import gedorinku.tsugidoko_server.ClassRooms
import io.github.hunachi.tsugidoko.infra.UserPositionServiceClient
import io.github.hunachi.tsugidoko.util.NetworkState
import io.github.hunachi.tsugidoko.util.session
import kotlinx.coroutines.*
import java.lang.Exception

class MainViewModel(
        private val userPositionClient: UserPositionServiceClient,
        private val preference: SharedPreferences
) : ViewModel() {

    private val _sendState = MutableLiveData<NetworkState<Boolean>>()
    val sendState: LiveData<NetworkState<Boolean>> = _sendState

    private val _sentState = MutableLiveData<NetworkState<ClassRooms.ClassRoom>>()
    val sentState: LiveData<NetworkState<ClassRooms.ClassRoom>> = _sentState


    fun preSendState() {
        viewModelScope.launch {
            try {
                delay(30000)
                _sendState.postValue(NetworkState.Success(true))
            } catch (e: Exception) {
                e.printStackTrace()
                _sendState.postValue(NetworkState.Error(e))
            }
        }
    }

    fun sendState(bssId: String, isStayingNow: Boolean) {
        viewModelScope.launch {
            try {
                preference.session()?.let { it ->
                    val userPosition = async {
                        userPositionClient.sendUserPosition(it, bssId, isStayingNow)
                    }

                    userPosition.await().let {
                        _sentState.postValue(NetworkState.Success(it.classRoom))
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _sentState.postValue(NetworkState.Error(e))
            }
        }
    }
}