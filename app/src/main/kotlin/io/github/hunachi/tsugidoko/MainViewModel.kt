package io.github.hunachi.tsugidoko

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import gedorinku.tsugidoko_server.ClassRooms
import gedorinku.tsugidoko_server.Users
import io.github.hunachi.tsugidoko.infra.UserPositionServiceClient
import io.github.hunachi.tsugidoko.infra.UserServiceClient
import io.github.hunachi.tsugidoko.util.NetworkState
import kotlinx.coroutines.*

class MainViewModel(
        private val userPositionClient: UserPositionServiceClient,
        private val userClient: UserServiceClient
) : ViewModel() {

    private val _sendState = MutableLiveData<Nothing>()
    val sendState: LiveData<Nothing> = _sendState

    private val _sendErrorState = MutableLiveData<Exception>()
    val sendErrorState: LiveData<Exception> = _sendErrorState

    private val _sentState = MutableLiveData<ClassRooms.ClassRoom>()
    val sentState: LiveData<ClassRooms.ClassRoom> = _sentState

    private val _sentErrorState = MutableLiveData<Exception>()
    val sentErrorState: LiveData<Exception> = _sentErrorState

    private val _userState = MutableLiveData<Users.User>()
    val userState: LiveData<Users.User> = _userState

    private val _userErrorState = MutableLiveData<Exception>()
    val userErrorState: LiveData<Exception> = _userErrorState

    fun preSendState() {
        viewModelScope.launch {
            try {
                delay(20000)
                _sendState.postValue(null)
            } catch (e: Exception) {
                e.printStackTrace()
                _sendErrorState.postValue(e)
            }
        }
    }

    fun sendState(bssId: String, isStayingNow: Boolean) {
        viewModelScope.launch {
            try {
                val userPosition = async {
                    userPositionClient.sendUserPosition(bssId, isStayingNow)
                }

                userPosition.await().let {
                    when (it) {
                        is NetworkState.Success -> _sentState.postValue(it.result.classRoom)
                        is NetworkState.Error -> _sentErrorState.postValue(it.e)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _sentErrorState.postValue(e)
            }
        }
    }

    fun user() {
        viewModelScope.launch {
            try {
                val user = async { userClient.user() }

                user.await().let {
                    when (it) {
                        is NetworkState.Success -> _userState.postValue(it.result)
                        is NetworkState.Error -> _userErrorState.postValue(it.e)
                    }
                }
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
                _userErrorState.postValue(e)
            }
        }
    }
}