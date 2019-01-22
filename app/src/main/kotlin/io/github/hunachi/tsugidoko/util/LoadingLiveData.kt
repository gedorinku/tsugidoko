package io.github.hunachi.tsugidoko.util

import androidx.lifecycle.LiveData

class LoadingMutableLiveData : LoadingLiveData() {

    fun loading(){
        value = true
    }

    fun stop(){
        postValue(false)
    }
}

open class LoadingLiveData: LiveData<Boolean>()