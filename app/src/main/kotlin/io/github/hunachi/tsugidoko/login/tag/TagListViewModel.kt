package io.github.hunachi.tsugidoko.login.tag

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import gedorinku.tsugidoko_server.type.TagOuterClass
import io.github.hunachi.tsugidoko.infra.TagServiceClient
import io.github.hunachi.tsugidoko.model.Tag
import io.github.hunachi.tsugidoko.util.NetworkState
import io.github.hunachi.tsugidoko.util.session
import kotlinx.coroutines.*
import java.lang.Exception

class TagListViewModel(
        private val tagServiceClient: TagServiceClient,
        private val preference: SharedPreferences
) : ViewModel() {

    private val _tagListStatus = MutableLiveData<NetworkState<List<Tag>>>()
    val tagListStatus: LiveData<NetworkState<List<Tag>>> = _tagListStatus

    private val _addTagsState = MutableLiveData<NetworkState<Boolean>>()
    val addTagState: LiveData<NetworkState<Boolean>> = _addTagsState

    fun tagList() {
        viewModelScope.launch {
            try {
                val tagList = async { }
                // todo
            } catch (e: Exception) {
                _tagListStatus.postValue(NetworkState.Error(e))
                e.printStackTrace()
            }
        }
    }

    fun addTags(tags: List<Tag>) {
        viewModelScope.launch {
            try {
                tags.map { it.convert() }
                // todo
            } catch (e: Exception) {
                _addTagsState.postValue(NetworkState.Error(e))
                e.printStackTrace()
            }
        }
    }
}