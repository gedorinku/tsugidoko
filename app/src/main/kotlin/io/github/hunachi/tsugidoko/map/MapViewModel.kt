package io.github.hunachi.tsugidoko.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import gedorinku.tsugidoko_server.ClassRooms
import gedorinku.tsugidoko_server.Users
import gedorinku.tsugidoko_server.type.TagOuterClass
import io.github.hunachi.tsugidoko.infra.ClassRoomServiceClient
import io.github.hunachi.tsugidoko.infra.UserServiceClient
import io.github.hunachi.tsugidoko.util.NetworkState
import kotlinx.coroutines.*

class MapViewModel(
        private val classRoomsClient: ClassRoomServiceClient,
        private val userClient: UserServiceClient
) : ViewModel() {

    private val _userState = MutableLiveData<NetworkState<Users.User>>()
    val userState: LiveData<NetworkState<Users.User>> = _userState

    private val _classRoomState = MutableLiveData<NetworkState<List<ClassRooms.ClassRoom>>>()
    val classRoomState: LiveData<NetworkState<List<ClassRooms.ClassRoom>>> = _classRoomState

    fun user() {
        viewModelScope.launch {
            try {
                val user = async { userClient.user() }

                _userState.postValue(NetworkState.Success(user.await()))

            } catch (e: java.lang.Exception) {
                e.printStackTrace()
                _userState.postValue(NetworkState.Error(e))
            }
        }
    }

    fun classRoom(tags: List<TagOuterClass.Tag>) {
        viewModelScope.launch {
            try {
                val classRoom = async { classRoomsClient.classRooms(tags) }

                _classRoomState.postValue(NetworkState.Success(classRoom.await()))
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
                _classRoomState.postValue(NetworkState.Error(e))
            }
        }
    }
}