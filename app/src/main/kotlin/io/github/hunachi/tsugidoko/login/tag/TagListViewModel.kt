package io.github.hunachi.tsugidoko.login.tag

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import gedorinku.tsugidoko_server.Users
import io.github.hunachi.tsugidoko.infra.TagServiceClient
import io.github.hunachi.tsugidoko.infra.UserServiceClient
import io.github.hunachi.tsugidoko.model.Tag
import io.github.hunachi.tsugidoko.model.convertTag
import io.github.hunachi.tsugidoko.util.LoadingLiveData
import io.github.hunachi.tsugidoko.util.LoadingMutableLiveData
import io.github.hunachi.tsugidoko.util.NetworkState
import io.github.hunachi.tsugidoko.util.session
import kotlinx.coroutines.*
import java.lang.Exception

class TagListViewModel(
        private val tagServiceClient: TagServiceClient,
        private val userClient: UserServiceClient,
        preferences: SharedPreferences
) : ViewModel() {

    private val _tagListState = MutableLiveData<List<Tag>>()
    val tagListState: LiveData<List<Tag>> = _tagListState

    private val _updateTagsState = MutableLiveData<List<Tag>>()
    val updateTagState: LiveData<List<Tag>> = _updateTagsState

    private val _tagListErrorState = MutableLiveData<Exception>()
    val tagListErrorState: LiveData<Exception> = _tagListErrorState

    private val _updateTagsErrorState = MutableLiveData<Exception>()
    val updateTagsErrorState: LiveData<Exception> = _updateTagsErrorState

    private val _userState = MutableLiveData<Users.User>()
    val userState: LiveData<Users.User> = _userState

    private val _userErrorState = MutableLiveData<Exception>()
    val userErrorState: LiveData<Exception> = _userErrorState

    private val _loadingState = LoadingMutableLiveData()
    val loadingState: LoadingLiveData = _loadingState

    init {
        preferences.session()?.let {
            tagServiceClient.setUp(it)
            userClient.setUp(it)
        }
    }

    fun tagList() {
        viewModelScope.launch {
            _loadingState.loading()
            try {
                val tagList = async { tagServiceClient.tags() }

                tagList.await().let {
                    when (it) {
                        is NetworkState.Success -> {
                            _tagListState.postValue(it.result.tagsList.map { i -> i.convertTag() })
                        }
                        is NetworkState.Error -> {
                            _tagListErrorState.postValue(it.e)
                        }
                    }
                    _loadingState.stop()
                }
            } catch (e: Exception) {
                _tagListErrorState.postValue(e)
                e.printStackTrace()
            }
        }
    }

    fun updateTags(tags: List<Tag>) {
        viewModelScope.launch {
            try {
                _loadingState.loading()
                val result = async { tagServiceClient.updateTags(tags) }

                result.await().let {
                    when (it) {
                        is NetworkState.Success -> {
                            _updateTagsState.postValue(it.result.tagsList.map { i -> i.convertTag() })
                        }
                        is NetworkState.Error -> {
                            it.e.printStackTrace()
                            _updateTagsErrorState.postValue(it.e)
                        }
                    }
                    _loadingState.stop()
                }
            } catch (e: Exception) {
                _updateTagsErrorState.postValue(e)
                e.printStackTrace()
                _loadingState.stop()
            }
        }
    }

    fun user() {
        viewModelScope.launch {
            try {
                _loadingState.loading()
                val user = async { userClient.user() }

                user.await().let {
                    when (it) {
                        is NetworkState.Success -> _userState.postValue(it.result)
                        is NetworkState.Error -> {
                            it.e.printStackTrace()
                            _userErrorState.postValue(it.e)
                        }
                    }
                    _loadingState.stop()
                }
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
                _userErrorState.postValue(e)
                _loadingState.stop()
            }
        }
    }
}