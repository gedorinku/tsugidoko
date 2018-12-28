package io.github.hunachi.tsugidoko

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import io.github.hunachi.tsugidoko.detailMap.DetailMapFragment
import io.github.hunachi.tsugidoko.model.dammyDetailMap
import io.github.hunachi.tsugidoko.util.inTransaction
import io.github.hunachi.tsugidoko.util.toast

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        supportFragmentManager.inTransaction {
            add(R.id.container, DetailMapFragment.newInstance(dammyDetailMap))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        (0..3).forEach {
            menu.add(0, it, it, "hoge").apply {
                setCheckable(true)
            }
        }
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        toast(item?.title.toString())
        item?.setChecked(!(item.isChecked))
        return super.onOptionsItemSelected(item)
    }
}
