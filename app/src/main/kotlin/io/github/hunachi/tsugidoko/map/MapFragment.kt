package io.github.hunachi.tsugidoko.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import gedorinku.tsugidoko_server.Users
import gedorinku.tsugidoko_server.type.TagOuterClass
import io.github.hunachi.tsugidoko.MainActivity
import io.github.hunachi.tsugidoko.R
import io.github.hunachi.tsugidoko.util.NetworkState
import io.github.hunachi.tsugidoko.util.nonNullObserve
import org.koin.androidx.viewmodel.ext.android.viewModel


class MapFragment : Fragment(), OnMapReadyCallback {

    private var mMap: GoogleMap? = null
    private val mapViewModel: MapViewModel by viewModel()
    private var user: Users.User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        with(mapViewModel) {
            userState.nonNullObserve(this@MapFragment) {
                when (it) {
                    is NetworkState.Success -> {
                        user = it.result
                        (activity as MainActivity).changeTags(it.result.tagsList)
                        reloadMarker()
                    }
                    is NetworkState.Error -> {
                        Toast.makeText(activity, "user", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            classRoomState.nonNullObserve(this@MapFragment) {
                when (it) {
                    is NetworkState.Success -> it.result.forEach { classRoom ->
                        if (mMap != null) {
                            /*以下のif文を消すと0人の時にも表示される*/
                            //if (classRoom.tagCounts.isNotEmpty()) {
                                CameraPosition.Builder(mMap?.cameraPosition).apply {
                                    mMap?.addMarker(MarkerOptions()
                                            .position(LatLng(classRoom.latitude, classRoom.longitude))
                                            .title("${classRoom.name}(${classRoom.tagCounts.size}人以上)"))
                                }
                            }
                       // }
                    }
                    is NetworkState.Error -> {

                    }
                }
            }
            // if(preference.tags == null) user() else preference.tags
            user()
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        childFragmentManager.findFragmentById(R.id.map).apply {
            this as SupportMapFragment
            getMapAsync(this@MapFragment)
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap?.apply {
            moveCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.Builder(mMap?.cameraPosition).also {
                it.target(LatLng(33.33521728594884, 130.5097379631427))
                it.zoom((mMap?.maxZoomLevel ?: 10f) - 4f)
            }.build()))
            mapType = GoogleMap.MAP_TYPE_HYBRID
        }
    }

    fun reloadMarker() {
        mapViewModel.classRoom((activity as MainActivity).selectedTags)
    }

    companion object {
        fun newInstance() =
                MapFragment().apply { arguments = bundleOf() }
    }
}
