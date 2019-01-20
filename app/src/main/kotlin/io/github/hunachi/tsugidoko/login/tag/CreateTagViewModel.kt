package io.github.hunachi.tsugidoko.login.tag

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.hunachi.tsugidoko.infra.TagServiceClient
import io.github.hunachi.tsugidoko.model.Tag
import io.github.hunachi.tsugidoko.model.convertTag
import io.github.hunachi.tsugidoko.util.NetworkState
import io.github.hunachi.tsugidoko.util.session
import kotlinx.coroutines.*
import java.lang.Exception

class CreateTagViewModel(
        private val tagServiceClient: TagServiceClient,
        preferences: SharedPreferences
) : ViewModel() {

    private val _createTagsState = MutableLiveData<Tag>()
    val createTagState: LiveData<Tag> = _createTagsState

    private val _createTagErrorState = MutableLiveData<Exception>()
    val createTagErrorState: LiveData<Exception> = _createTagErrorState

    init {
        preferences.session()?.let {
            tagServiceClient.setUp(it)
        }
    }

    fun createTag(tag: Tag) {
        viewModelScope.launch {
            try {
                val result = async { tagServiceClient.createTag(tag) }

                result.await().let {
                    when (it) {
                        is NetworkState.Success -> {
                            _createTagsState.postValue(it.result.convertTag())
                        }
                        is NetworkState.Error -> {
                            it.e.printStackTrace()
                            _createTagErrorState.postValue(it.e)
                        }
                    }
                }
            } catch (e: Exception) {
                _createTagErrorState.postValue(e)
                e.printStackTrace()
            }
        }
    }
}