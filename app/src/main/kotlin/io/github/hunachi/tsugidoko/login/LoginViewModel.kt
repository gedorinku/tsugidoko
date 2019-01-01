package io.github.hunachi.tsugidoko.login

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.github.hunachi.tsugidoko.infra.SessionServiceClient
import io.github.hunachi.tsugidoko.util.NetworkState
import io.github.hunachi.tsugidoko.util.session
import kotlinx.coroutines.*

class LoginViewModel(
        private val client: SessionServiceClient,
        private val preference: SharedPreferences
) : ViewModel() {

    private val job = SupervisorJob()
    private val scope = Dispatchers.IO + job

    private val _submitStatus = MutableLiveData<NetworkState<String>>()
    val submitStatus: LiveData<NetworkState<String>> = _submitStatus

    fun submit(userName: String, password: String, isRegister: Boolean) {
        CoroutineScope(scope).launch {
            try {
                val session = async {
                    if (isRegister) {
                        client.firstCreateSession(userName, password)
                    } else {
                        client.createSession(userName, password)
                    }
                }
                val sessionId = session.await().sessionId
                preference.session(sessionId)
                _submitStatus.postValue(NetworkState.Success(sessionId))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}