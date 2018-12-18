package io.github.hunachi.tsugidoko.map

import androidx.lifecycle.ViewModel
import io.github.hunachi.tsugidoko.model.ClassRoom

class MapViewModel: ViewModel() {


    fun fetchClassRooms(): List<ClassRoom> {
        /*TODO*/
        return listOf()
    }

    override fun onCleared() {
        super.onCleared()
    }
}