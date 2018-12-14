package io.github.hunachi.tsugidoko

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import io.github.hunachi.tsugidoko.databinding.ActivityMapsBinding
import io.github.hunachi.tsugidoko.map.MapFragment

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMapsBinding by lazy {
        DataBindingUtil.setContentView<ActivityMapsBinding>(this, R.layout.activity_maps)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.apply {
            supportFragmentManager.inTransaction{
                add(R.id.container, MapFragment.newInstance())
            }
        }
    }
}
