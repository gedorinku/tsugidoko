package io.github.hunachi.tsugidoko.map

import android.content.Context
import android.os.Bundle
import android.util.Log
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
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import gedorinku.tsugidoko_server.ClassRooms
import gedorinku.tsugidoko_server.Users
import gedorinku.tsugidoko_server.type.BuildingOuterClass
import io.github.hunachi.tsugidoko.MainActivity
import io.github.hunachi.tsugidoko.R
import io.github.hunachi.tsugidoko.detailMap.DetailMapFragment
import io.github.hunachi.tsugidoko.model.Building
import io.github.hunachi.tsugidoko.model.FloorRooms
import io.github.hunachi.tsugidoko.util.NetworkState
import io.github.hunachi.tsugidoko.util.nonNullObserve
import org.koin.androidx.viewmodel.ext.android.viewModel


class MapFragment : Fragment(), OnMapReadyCallback {

    private var mMap: GoogleMap? = null
    private val mapViewModel: MapViewModel by viewModel()
    private var user: Users.User? = null
    private lateinit var classRooms: List<ClassRooms.ClassRoom>

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
                    is NetworkState.Success ->
                        it.result.groupBy { result -> result.building }.forEach { result ->
                            this@MapFragment.classRooms = result.value
                            showMapsMarkers(result.key, result.value)
                        }
                    is NetworkState.Error ->
                        Toast.makeText(activity, it.e.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun showMapsMarkers(
            building: BuildingOuterClass.Building,
            classRooms: List<ClassRooms.ClassRoom>
    ) {
        if (mMap != null) {
            /*以下のif文を消すと0人の時にも表示される*/
            val peopleCountMessage = when (classRooms.sumBy { it.tagCountsCount }) {
                in 0..4 -> "数人"
                in 5..10 -> "5人以上"
                in 10..20 -> "10人以上"
                in 20..50 -> "20人以上"
                else -> "50人以上"
            }

            mMap?.addMarker(MarkerOptions()
                    .position(LatLng(building.latitude, building.longitude))
                    .title("${building.name}(${peopleCountMessage}人以上)"))
                    ?.apply { tag = Pair(building, classRooms) }

            mMap?.setOnMarkerClickListener { it ->
                if (checkCouldCast(it)) {
                    Building(
                            building.id,
                            building.name,
                            classRooms.convertFloors()
                    ).let {
                        (activity as MainActivity).changeFragment(DetailMapFragment.newInstance(it))
                    }
                }
                false
            }
        }
    }

    private fun checkCouldCast(marker: Marker): Boolean {
        if (marker.tag !is Pair<*, *>) return false
        (marker.tag as Pair<*, *>).let { pair ->
            if (pair.first !is BuildingOuterClass.Building) return false
            if (pair.second !is List<*>) return false
            val classRooms = pair.second as List<*>
            if (classRooms.isEmpty() and (classRooms[0] !is ClassRooms.ClassRoom)) return false
        }
        return true
    }

    fun List<ClassRooms.ClassRoom>.convertFloors(): List<FloorRooms> =
            groupBy { it.floor }.map { FloorRooms(it.key, it.value[0].imageUrl(), it.value) }

    fun ClassRooms.ClassRoom.imageUrl() = "https://gedorinku.github.io/tsugidoko-pic/${building.id}/$floor.png"

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mapViewModel.user()
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
        mMap?.clear()
        mapViewModel.classRoom((activity as MainActivity).selectedTags)
    }

    fun addMarker(classRoom: ClassRooms.ClassRoom) {
        mMap?.addMarker(MarkerOptions()
                .position(LatLng(classRoom.building.latitude + 0.0000000000005, classRoom.building.longitude + 0.0000000000005))
                .title("${classRoom.building.name}(現在地)"))
                ?.apply { tag = Pair(classRoom.building, classRooms) }
    }

    companion object {
        fun newInstance() =
                MapFragment().apply { arguments = bundleOf() }
    }
}
