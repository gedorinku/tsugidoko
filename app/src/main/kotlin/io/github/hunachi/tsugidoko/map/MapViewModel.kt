package io.github.hunachi.tsugidoko.map

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import gedorinku.tsugidoko_server.Users
import io.github.hunachi.tsugidoko.infra.ClassRoomServiceClient
import io.github.hunachi.tsugidoko.infra.UserServiceClient
import io.github.hunachi.tsugidoko.model.ClassRoom
import io.github.hunachi.tsugidoko.model.convertToClassRoom
import io.github.hunachi.tsugidoko.util.NetworkState
import io.github.hunachi.tsugidoko.util.session
import kotlinx.coroutines.*

class MapViewModel(
        private val classRoomsClient: ClassRoomServiceClient,
        private val userClient: UserServiceClient,
        private val preference: SharedPreferences
) : ViewModel() {

    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    private val _submitStatus = MutableLiveData<NetworkState<Pair<List<ClassRoom>, Users.User>>>()
    val submitStatus: LiveData<NetworkState<Pair<List<ClassRoom>, Users.User>>> = _submitStatus

    fun submit() {
        try {
            scope.launch {
                try {
                    preference.session()?.let { it ->

                        val classRoom = async { classRoomsClient.classRooms(it) }
                        val user = async { userClient.user(it) }

                        (classRoom.await().map { it.convertToClassRoom() } to user.await()).let {
                            _submitStatus.postValue(NetworkState.Success(it))
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    _submitStatus.postValue(NetworkState.Error(e))
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onCleared() {
        scope.cancel()
        super.onCleared()
    }
}