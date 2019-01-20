package io.github.hunachi.tsugidoko.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import gedorinku.tsugidoko_server.ClassRooms
import gedorinku.tsugidoko_server.Tags
import gedorinku.tsugidoko_server.Users
import io.github.hunachi.tsugidoko.infra.ClassRoomServiceClient
import io.github.hunachi.tsugidoko.infra.UserServiceClient
import io.github.hunachi.tsugidoko.util.NetworkState
import kotlinx.coroutines.*

class MapViewModel(
        private val classRoomsClient: ClassRoomServiceClient
) : ViewModel() {

    private val _classRoomState = MutableLiveData<List<ClassRooms.ClassRoom>>()
    val classRoomState: LiveData<List<ClassRooms.ClassRoom>> = _classRoomState

    private val _classRoomErrorState = MutableLiveData<Exception>()
    val classRoomErrorState: LiveData<Exception> = _classRoomErrorState

    fun classRoom(tags: List<Tags.Tag>) {
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