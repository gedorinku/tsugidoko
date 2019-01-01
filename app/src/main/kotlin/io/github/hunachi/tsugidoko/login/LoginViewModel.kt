package io.github.hunachi.tsugidoko.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import gedorinku.tsugidoko_server.Users
import io.github.hunachi.tsugidoko.infra.SessionServiceClient
import io.github.hunachi.tsugidoko.infra.UserServiceClient
import io.github.hunachi.tsugidoko.util.NetworkState
import kotlinx.coroutines.*

class LoginViewModel(private val client: SessionServiceClient) : ViewModel() {

    private val job = SupervisorJob()
    private val scope = Dispatchers.IO + job

    private val _submitStatus = MutableLiveData<NetworkState<String>>()
    val submitStatus: LiveData<NetworkState<String>> = _submitStatus

    fun submit(userName: String, password: String) {
        CoroutineScope(scope).launch {
            try {
                val hoge = async { client.firstCreateSession(userName, password) }
                _submitStatus.postValue(NetworkState.Success(hoge.await().sessionId))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}