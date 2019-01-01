package io.github.hunachi.tsugidoko

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import io.github.hunachi.tsugidoko.login.LoginActivity
import io.github.hunachi.tsugidoko.map.MapFragment
import io.github.hunachi.tsugidoko.util.inTransaction
import io.github.hunachi.tsugidoko.util.session
import io.github.hunachi.tsugidoko.util.toast
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {

    private val preference: SharedPreferences by inject()
    private val mapFragment =  MapFragment.newInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        if(preference.session()?.isNotBlank() != true){
            startActivity(Intent(this, LoginActivity::class.java))
        }

        supportFragmentManager.inTransaction {
            add(R.id.container, mapFragment)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        (0..3).forEach {
            menu.add(0, it, it, "hoge").apply {
                isCheckable = true
            }
        }
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        toast(item?.title.toString())
        item?.isChecked = !(item?.isChecked ?: true)
        return super.onOptionsItemSelected(item)
    }
}
