package io.github.hunachi.tsugidoko.login.tag

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import gedorinku.tsugidoko_server.Users
import io.github.hunachi.tsugidoko.infra.TagServiceClient
import io.github.hunachi.tsugidoko.infra.UserServiceClient
import io.github.hunachi.tsugidoko.model.Tag
import io.github.hunachi.tsugidoko.model.convertTag
import io.github.hunachi.tsugidoko.util.NetworkState
import kotlinx.coroutines.*
import java.lang.Exception

class TagListViewModel(
        private val tagServiceClient: TagServiceClient,
        private val userClient: UserServiceClient
) : ViewModel() {

    private val _tagListState = MutableLiveData<List<Tag>>()
    val tagListState: LiveData<List<Tag>> = _tagListState

    private val _addTagsState =  MutableLiveData<List<Tag>>()
    val addTagState: LiveData<List<Tag>> = _addTagsState

    private val _tagListErrorState = MutableLiveData<Exception>()
    val tagListErrorState: LiveData<Exception> = _tagListErrorState

    private val _addTagsErrorState = MutableLiveData<Exception>()
    val addTagsErrorState: LiveData<Exception> = _addTagsErrorState

    private val _userState = MutableLiveData<Users.User>()
    val userState: LiveData<Users.User> = _userState

    private val _userErrorState = MutableLiveData<Exception>()
    val userErrorState: LiveData<Exception> = _userErrorState

    fun tagList() {
        viewModelScope.launch {
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
                }
            } catch (e: Exception) {
                _tagListErrorState.postValue(e)
                e.printStackTrace()
            }
        }
    }

    fun addTags(tags: List<Tag>) {
        viewModelScope.launch {
            try {
                val tags = async { tagServiceClient.attachTags(tags) }

                tags.await().let {
                    when (it) {
                        is NetworkState.Success -> {
                            _addTagsState.postValue(it.result.tagsList.map { i -> i.convertTag() })
                        }
                        is NetworkState.Error -> {
                            it.e.printStackTrace()
                            _addTagsErrorState.postValue(it.e)
                        }
                    }
                }
            } catch (e: Exception) {
                _addTagsErrorState.postValue(e)
                e.printStackTrace()
            }
        }
    }

    fun user() {
        viewModelScope.launch {
            try {
                val user = async { userClient.user() }

                user.await().let {
                    when (it) {
                        is NetworkState.Success -> _userState.postValue(it.result)
                        is NetworkState.Error -> {
                            it.e.printStackTrace()
                            _userErrorState.postValue(it.e)
                        }
                    }
                }
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
                _userErrorState.postValue(e)
            }
        }
    }
}