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

    private val _userState = MutableLiveData<Users.User>()
    val userState: LiveData<Users.User> = _userState

    private val _userErrorState = MutableLiveData<Exception>()
    val userErrorState: LiveData<Exception> = _userErrorState

    private val _classRoomState = MutableLiveData<List<ClassRooms.ClassRoom>>()
    val classRoomState: LiveData<List<ClassRooms.ClassRoom>> = _classRoomState

    private val _classRoomErrorState = MutableLiveData<Exception>()
    val classRoomErrorState: LiveData<Exception> = _classRoomErrorState

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

    fun classRoom(tags: List<TagOuterClass.Tag>) {
        viewModelScope.launch {
            try {
                val classRoom = async { classRoomsClient.classRooms(tags) }

                classRoom.await().let {
                    when (it) {
                        is NetworkState.Success -> _classRoomState.postValue(it.result)
                        is NetworkState.Error -> _classRoomErrorState.postValue(it.e)
                    }
                }
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
                _classRoomErrorState.postValue(e)
            }
        }
    }
}