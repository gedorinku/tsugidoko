package io.github.hunachi.tsugidoko.map

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import io.github.hunachi.tsugidoko.R
import org.koin.androidx.viewmodel.ext.android.viewModel

class MapFragment : Fragment(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private val mapViewModel: MapViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        childFragmentManager
            .beginTransaction()
            .add(R.id.container, SupportMapFragment().apply {
                getMapAsync(this@MapFragment)
            })
            .commit()
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // TODO ここにmarkerの処をかく（別にmethodを新しく作るのはおk．）
        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }

    companion object {
        fun newInstance() =
            MapFragment().apply { arguments = bundleOf() }
    }
}
