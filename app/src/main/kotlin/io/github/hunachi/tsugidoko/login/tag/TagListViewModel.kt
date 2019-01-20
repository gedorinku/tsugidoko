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
import kotlinx.coroutines.*
import java.lang.Exception

class TagListViewModel(
        private val tagServiceClient: TagServiceClient,
        private val preference: SharedPreferences
) : ViewModel() {

    private val _tagListState = MutableLiveData<List<Tag>>()
    val tagListState: LiveData<List<Tag>> = _tagListState

    private val _addTagsState =  MutableLiveData<List<Tag>>()
    val addTagState: LiveData<List<Tag>> = _addTagsState

    private val _tagListErrorState = MutableLiveData<Exception>()
    val tagListErrorState: LiveData<Exception> = _tagListErrorState

    private val _addTagsErrorState = MutableLiveData<Exception>()
    val addTagsErrorState: LiveData<Exception> = _addTagsErrorState

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
}