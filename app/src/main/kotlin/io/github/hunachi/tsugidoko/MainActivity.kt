package io.github.hunachi.tsugidoko

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.Fragment
import io.github.hunachi.tsugidoko.login.LoginActivity
import io.github.hunachi.tsugidoko.map.MapFragment
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import android.net.wifi.WifiManager
import io.github.hunachi.tsugidoko.util.*
import android.net.ConnectivityManager
import gedorinku.tsugidoko_server.Tags


class MainActivity : AppCompatActivity() {

    private val preference: SharedPreferences by inject()
    private val mainViewModel: MainViewModel by viewModel()
    private val mapFragment = MapFragment.newInstance()
    private val selectedTags: MutableList<Tags.Tag> = mutableListOf()
    private var mMenu: Menu? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        if (preference.session()?.isNotBlank() != true) {
            LoginActivity.start(this)
        } else {
            setUpMap()
        }
    }

    override fun onRestart() {
        super.onRestart()
        mainViewModel.user()
    }

    private fun isWifiConnected() =
            with(getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager) {
                activeNetworkInfo != null && activeNetworkInfo.isConnected
                        && activeNetworkInfo.type == ConnectivityManager.TYPE_WIFI
            }

    private fun setUpMap() {
        supportFragmentManager.inTransaction {
            add(R.id.container, mapFragment)
        }

        mainViewModel.apply {
            sendState.observe(this@MainActivity) {
                (applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager)
                        .connectionInfo
                        .let { info ->
                            if (isWifiConnected()) {
                                sendState(info.bssid, true)
                                toast(info.bssid)
                            } else sendState("TODO", false)
                        }
                mapFragment.reloadMarker(selectedTags)
            }

            sendErrorState.nonNullObserve(this@MainActivity) {
                toast("error ${it.message}")
                preSendState()
            }

            sentState.nonNullObserve(this@MainActivity) {
                mapFragment.addMarker(it)
                preSendState()
            }

            sentErrorState.nonNullObserve(this@MainActivity) {
                toast("error ${it.message}")
                preSendState()
            }

            userState.nonNullObserve(this@MainActivity) {
                changeTags(it.tagsList)
                mapFragment.reloadMarker(it.tagsList)
            }

            userErrorState.nonNullObserve(this@MainActivity) {
                toast("${it.message}")
            }
        }.run {
            user()
            preSendState()
        }
    }

    private fun changeTags(tags: List<Tags.Tag>) {
        selectedTags.apply {
            clear()
            addAll(tags)
        }
        mMenu?.let { onCreateOptionsMenu(it) }
    }

    override fun onBackPressed() {
        if (supportFragmentManager.fragments.find { it is MapFragment } == null) {
            changeFragment(mapFragment)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        if (mMenu == null) mMenu = menu
        menu.removeGroup(MENU_GROUP_ID)

        selectedTags.forEachIndexed { index, tag ->
            if (menu.findItem(tag.id) == null) {
                menu.add(MENU_GROUP_ID, tag.id, index, tag.name).apply { isCheckable = true }
            }
        }
        if (menu.findItem(MENU_ITEM_ADD_TAGS_ID) == null) {
            menu.add(MENU_GROUP_ID, MENU_ITEM_ADD_TAGS_ID, selectedTags.size, "タグの設定")
        }
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    fun changeFragment(fragment: Fragment) {
        supportFragmentManager.inTransaction { replace(R.id.container, fragment) }
        if (fragment is MapFragment) mainViewModel.user()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        toast(item?.title.toString())

        if (item?.itemId == -1) {
            LoginActivity.start(this, true)
            return super.onOptionsItemSelected(item)
        }

        item?.isChecked = !(item?.isChecked ?: true)
        mapFragment.reloadMarker(selectedTags)
        return super.onOptionsItemSelected(item)
    }

    companion object {
        private const val MENU_ITEM_ADD_TAGS_ID = -1
        private const val MENU_GROUP_ID = 0

        fun start(context: Context) =
                context.startActivity(Intent(context, MainActivity::class.java))
    }
}
