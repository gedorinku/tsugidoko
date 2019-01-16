package io.github.hunachi.tsugidoko

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.Fragment
import gedorinku.tsugidoko_server.type.TagOuterClass
import io.github.hunachi.tsugidoko.login.LoginActivity
import io.github.hunachi.tsugidoko.map.MapFragment
import io.github.hunachi.tsugidoko.util.inTransaction
import io.github.hunachi.tsugidoko.util.session
import io.github.hunachi.tsugidoko.util.toast
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {

    private val preference: SharedPreferences by inject()
    private val mapFragment = MapFragment.newInstance()
    val selectedTags: MutableList<TagOuterClass.Tag> = mutableListOf()
    private var mMenu: Menu? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        if (preference.session()?.isNotBlank() != true) {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        supportFragmentManager.inTransaction {
            add(R.id.container, mapFragment)
        }
    }

    fun changeTags(tags: List<TagOuterClass.Tag>) {
        selectedTags.apply {
            clear()
            addAll(tags)
        }
        mMenu?.let { onCreateOptionsMenu(it) }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        mMenu = menu
        selectedTags.forEachIndexed { index, tag ->
            menu.add(0, tag.id, index, tag.name).apply { isCheckable = true }
        }
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    fun changeFragment(fragment: Fragment) {
        supportFragmentManager.inTransaction { replace(R.id.container, fragment) }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        toast(item?.title.toString())
        item?.isChecked = !(item?.isChecked ?: true)
        mapFragment.reloadMarker()
        return super.onOptionsItemSelected(item)
    }
}
