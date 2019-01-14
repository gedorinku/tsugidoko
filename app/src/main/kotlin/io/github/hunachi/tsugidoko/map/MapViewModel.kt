package io.github.hunachi.tsugidoko.map

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import gedorinku.tsugidoko_server.Users
import gedorinku.tsugidoko_server.type.TagOuterClass
import io.github.hunachi.tsugidoko.infra.ClassRoomServiceClient
import io.github.hunachi.tsugidoko.infra.UserServiceClient
import io.github.hunachi.tsugidoko.model.ClassRoom
import io.github.hunachi.tsugidoko.model.convertToClassRoom
import io.github.hunachi.tsugidoko.util.NetworkState
import io.github.hunachi.tsugidoko.util.session
import kotlinx.coroutines.*
import java.lang.Exception

class MapViewModel(
        private val classRoomsClient: ClassRoomServiceClient,
        private val userClient: UserServiceClient,
        private val preference: SharedPreferences
) : ViewModel() {

    private val _userState = MutableLiveData<NetworkState<Users.User>>()
    val userState: LiveData<NetworkState<Users.User>> = _userState

    private val _classRoomState = MutableLiveData<NetworkState<List<ClassRoom>>>()
    val classRoomState: LiveData<NetworkState<List<ClassRoom>>> = _classRoomState

    fun user() {
        viewModelScope.launch {
            try {
                preference.session()?.let { it ->

                    val user = async { userClient.user(it) }

                    _userState.postValue(NetworkState.Success(user.await()))
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _userState.postValue(NetworkState.Error(e))
            }
        }
    }

    fun classRoom(tags: List<TagOuterClass.Tag>) {
        viewModelScope.launch {
            try {
                preference.session()?.let { it ->

                    val classRoom = async { classRoomsClient.classRooms(it, tags) }

                    classRoom.await().map { it.convertToClassRoom() }.let {
                        _classRoomState.postValue(NetworkState.Success(it))
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _classRoomState.postValue(NetworkState.Error(e))
            }
        }
    }
}