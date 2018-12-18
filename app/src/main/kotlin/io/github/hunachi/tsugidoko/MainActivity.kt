package io.github.hunachi.tsugidoko

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import io.github.hunachi.tsugidoko.map.MapFragment
import io.github.hunachi.tsugidoko.R
import io.github.hunachi.tsugidoko.util.inTransaction

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        supportFragmentManager.inTransaction {
            add(R.id.container, MapFragment.newInstance())
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        menu.add(0, 0/*itemIdを使ってuniqueにする*/, 0, "hoge")
        val item = menu.findItem(0)
        //item.actionView = View.inflate(this, R.layout.fragment_tag, )
        return super.onPrepareOptionsMenu(menu)
    }
}
