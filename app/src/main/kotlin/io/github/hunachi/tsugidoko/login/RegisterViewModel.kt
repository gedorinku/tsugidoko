package io.github.hunachi.tsugidoko.login

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.hunachi.tsugidoko.infra.SessionServiceClient
import io.github.hunachi.tsugidoko.util.NetworkState
import io.github.hunachi.tsugidoko.util.session
import kotlinx.coroutines.*

class RegisterViewModel(
        private val client: SessionServiceClient,
        private val preference: SharedPreferences
) : ViewModel() {

    private val _sessionIdState = MutableLiveData<String>()
    val sessionIdState: LiveData<String> = _sessionIdState

    private val _sessionIdErrorState = MutableLiveData<Exception>()
    val sessionIdErrorState: LiveData<Exception> = _sessionIdErrorState

    fun submit(userName: String, password: String) {
        viewModelScope.launch {
            try {
                val session = async { client.firstCreateSession(userName, password) }
                session.await().let {
                    when (it) {
                        is NetworkState.Success -> {
                            it.result.sessionId.let { sessionId ->
                                _sessionIdState.postValue(sessionId)
                                preference.session(sessionId)
                            }
                        }
                        is NetworkState.Error -> _sessionIdErrorState.postValue(it.e)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _sessionIdErrorState.postValue(e)
            }
        }
    }
}