package io.github.hunachi.tsugidoko

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
}
