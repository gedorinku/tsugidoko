package io.github.hunachi.tsugidoko.util

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import io.github.hunachi.tsugidoko.model.Tag


private const val NAME_STORE = "Store"
private const val NAME_SESSION = "Session"
private const val NAME_OWNER_TAGS = "tags"

fun setupSharedPreference(application: Application): SharedPreferences =
        application.getSharedPreferences(NAME_STORE, Context.MODE_PRIVATE)

fun SharedPreferences.session(): String? = getString(NAME_SESSION, null)

fun SharedPreferences.session(token: String) = edit { putString(NAME_SESSION, token) }

/*

fun SharedPreferences.selectedTagsOnMap() = getString(NAME_OWNER_TAGS, null)

fun SharedPreferences.selectedTagsOnMap(tags: List<Tag>) = edit {
    tags.map { it.id }
}

*/