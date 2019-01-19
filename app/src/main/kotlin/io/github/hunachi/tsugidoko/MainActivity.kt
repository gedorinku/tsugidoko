package io.github.hunachi.tsugidoko

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.wifi.SupplicantState
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.Fragment
import gedorinku.tsugidoko_server.type.TagOuterClass
import io.github.hunachi.tsugidoko.login.LoginActivity
import io.github.hunachi.tsugidoko.map.MapFragment
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import android.net.wifi.WifiManager
import io.github.hunachi.tsugidoko.util.*

class MainActivity : AppCompatActivity() {

    private val preference: SharedPreferences by inject()
    private val mainViewModel: MainViewModel by viewModel()
    private val mapFragment = MapFragment.newInstance()
    val selectedTags: MutableList<TagOuterClass.Tag> = mutableListOf()
    private var mMenu: Menu? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        if (preference.session()?.isNotBlank() != true) LoginActivity.start(this)


        supportFragmentManager.inTransaction {
            add(R.id.container, mapFragment)
        }

        mainViewModel.apply {
            sendState.nonNullObserve(this@MainActivity) {
                mapFragment.reloadMarker()
                (applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager)
                        .connectionInfo
                        .let { info ->
                            when (info.supplicantState) {
                                SupplicantState.ASSOCIATED -> sendState(info.bssid, true)
                                else -> sendState("TODO", false) //TODO
                            }
                        }
            }

            sentState.nonNullObserve(this@MainActivity) {
                when (it) {
                    is NetworkState.Success -> mapFragment.addMarker(it.result)
                    is NetworkState.Error -> {
                    }
                }
                preSendState()
            }
        }.preSendState()
    }

    fun changeTags(tags: List<TagOuterClass.Tag>) {
        selectedTags.apply {
            clear()
            addAll(tags)
        }
        mMenu?.let { onCreateOptionsMenu(it) }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        if (mMenu == null) mMenu = menu
        selectedTags.forEachIndexed { index, tag ->
            if (menu.findItem(tag.id) == null) {
                menu.add(0, tag.id, index, tag.name).apply { isCheckable = true }
            }
        }
        if (menu.findItem(MENU_ITEM_ADD_TAGS_ID) == null) {
            menu.add(0, MENU_ITEM_ADD_TAGS_ID, selectedTags.size, "参加するタグを増やす．")
        }
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    fun changeFragment(fragment: Fragment) {
        supportFragmentManager.inTransaction { replace(R.id.container, fragment) }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        toast(item?.title.toString())

        if (item?.itemId == -1) {
            LoginActivity.start(this, true)
            return super.onOptionsItemSelected(item)
        }

        item?.isChecked = !(item?.isChecked ?: true)
        mapFragment.reloadMarker()
        return super.onOptionsItemSelected(item)
    }

    companion object {
        private const val MENU_ITEM_ADD_TAGS_ID = -1

        fun start(context: Context) =
                context.startActivity(Intent(context, MainActivity::class.java))
    }
}
