package io.github.hunachi.tsugidoko.login

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.hunachi.tsugidoko.infra.SessionServiceClient
import io.github.hunachi.tsugidoko.util.LoadingLiveData
import io.github.hunachi.tsugidoko.util.LoadingMutableLiveData
import io.github.hunachi.tsugidoko.util.NetworkState
import io.github.hunachi.tsugidoko.util.session
import kotlinx.coroutines.*
import java.lang.Exception

class LoginViewModel(
        private val client: SessionServiceClient,
        private val preference: SharedPreferences
) : ViewModel() {

    private val _sessionIdState = MutableLiveData<String>()
    val sessionIdState: LiveData<String> = _sessionIdState

    private val _sessionIdErrorState = MutableLiveData<Exception>()
    val sessionIdErrorState: LiveData<Exception> = _sessionIdErrorState

    private val _loadingState = LoadingMutableLiveData()
    val loadingState: LoadingLiveData = _loadingState

    fun submit(userName: String, password: String) {
        viewModelScope.launch {
            try {
                _loadingState.loading()
                val session = async { client.createSession(userName, password) }
                session.await().let {
                    when (it) {
                        is NetworkState.Success -> {
                            it.result.sessionId.let { sessionId ->
                                preference.session(sessionId)
                                _sessionIdState.postValue(sessionId)
                            }
                        }
                        is NetworkState.Error -> _sessionIdErrorState.postValue(it.e)
                    }
                    _loadingState.stop()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _sessionIdErrorState.postValue(e)
                _loadingState.stop()
            }
        }
    }
}