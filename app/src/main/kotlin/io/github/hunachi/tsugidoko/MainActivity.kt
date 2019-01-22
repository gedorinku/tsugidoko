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
import android.view.View
import gedorinku.tsugidoko_server.Tags
import kotlinx.android.synthetic.main.loading_menu_item.view.*


class MainActivity : AppCompatActivity() {

    private val preference: SharedPreferences by inject()
    private val mainViewModel: MainViewModel by viewModel()
    private val mapFragment = MapFragment.newInstance()
    private val myTag: MutableList<Tags.Tag> = mutableListOf()
    private val selectedTag: MutableList<Tags.Tag> = mutableListOf()
    private var mMenu: Menu? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        if (preference.session()?.isNotBlank() != true) {
            LoginActivity.start(this)
            finish()
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
                (getSystemService(Context.WIFI_SERVICE) as WifiManager)
                        .connectionInfo
                        .let { info ->
                            if (isWifiConnected()) {
                                sendState(info.bssid, true)
                                toast(info.bssid)
                            } else sendState("TODO", false)
                        }
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
                //toast("error ${it.message}")
                preSendState()
            }

            userState.nonNullObserve(this@MainActivity) {
                changeTags(it.tagsList)
            }

            userErrorState.nonNullObserve(this@MainActivity) {
                toast("${it.message}")
            }

            loadingState.nonNullObserve(this@MainActivity) {

            }
        }.run {
            user()
            preSendState()
        }
    }

    private fun changeTags(tags: List<Tags.Tag>) {
        selectedTag.apply {
            tags.filter { contains(it) }.let {
                clear()
                addAll(it)
            }
        }
        myTag.apply {
            clear()
            addAll(tags)
        }
        reloadMarker()
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

        myTag.forEachIndexed { index, tag ->
            if (menu.findItem(tag.id) == null) {
                menu.add(MENU_GROUP_ID, tag.id, index, tag.name).apply {
                    isCheckable = true
                    isChecked = selectedTag.contains(tag)
                }
            }
        }
        if (menu.findItem(MENU_ITEM_ADD_TAGS_ID) == null) {
            menu.add(MENU_GROUP_ID, MENU_ITEM_ADD_TAGS_ID, myTag.size, "タグの設定")
        }

        if (menu.findItem(MENU_ITEM_RELOAD) == null) {
            menu.add(MENU_RELOAD_GROUP_ID, MENU_ITEM_RELOAD, myTag.size, "reload").apply {
                setActionView(R.layout.loading_menu_item)
                actionView.setOnClickListener { reloadMarker() }
                setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
            }
        }
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    fun loadingReloadMenuIcon(boolean: Boolean) {
        mMenu?.findItem(MENU_ITEM_RELOAD)?.let {
            it.actionView.run {
                spin_kit.visibility = if (boolean) View.VISIBLE else View.INVISIBLE
                renewIcon.visibility = if (boolean.not()) View.VISIBLE else View.INVISIBLE
            }
        }
    }

    fun changeFragment(fragment: Fragment) {
        supportFragmentManager.inTransaction { replace(R.id.container, fragment) }
    }

    fun reloadMarker(){
        mapFragment.reloadMarker(selectedTag)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        toast(item?.title.toString())


        when (item?.itemId) {
            MENU_ITEM_ADD_TAGS_ID -> LoginActivity.start(this, true)
            else -> {
                if (item?.isChecked == true) {
                    selectedTag.removeIf { it.id == item.itemId }
                } else {
                    myTag.find { it.id == item?.itemId }?.let { selectedTag.add(it) }
                }
                item?.isChecked = !(item?.isChecked ?: true)
                if (supportFragmentManager.fragments.find { it is MapFragment } != null) {
                    reloadMarker()
                } else {
                    // TODO DetailMap Renew
                }
            }
        }

        return super.onOptionsItemSelected(item)
    }

    companion object {
        private const val MENU_ITEM_ADD_TAGS_ID = -1
        private const val MENU_ITEM_RELOAD = -2
        private const val MENU_GROUP_ID = 0
        private const val MENU_RELOAD_GROUP_ID = 1

        fun start(context: Context) =
                context.startActivity(Intent(context, MainActivity::class.java))
    }
}
