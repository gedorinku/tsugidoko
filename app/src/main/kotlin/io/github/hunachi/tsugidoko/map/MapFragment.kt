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
import com.google.android.gms.maps.model.*
import gedorinku.tsugidoko_server.ClassRooms
import gedorinku.tsugidoko_server.Tags
import gedorinku.tsugidoko_server.type.BuildingOuterClass
import io.github.hunachi.tsugidoko.MainActivity
import io.github.hunachi.tsugidoko.R
import io.github.hunachi.tsugidoko.detailMap.DetailMapFragment
import io.github.hunachi.tsugidoko.model.Building
import io.github.hunachi.tsugidoko.model.FloorRooms
import io.github.hunachi.tsugidoko.model.TagCountLevel
import io.github.hunachi.tsugidoko.model.tagCountLevel
import io.github.hunachi.tsugidoko.util.nonNullObserve
import io.github.hunachi.tsugidoko.util.toast
import org.koin.android.ext.android.inject


class MapFragment : Fragment(), OnMapReadyCallback {

    private var mMap: GoogleMap? = null
    private val mapViewModel: MapViewModel by inject()
    private lateinit var classRooms: List<ClassRooms.ClassRoom>
    private val markers: MutableList<Pair<Marker, TagCountLevel>> = mutableListOf()
    private var myPositionMarker: Marker? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        with(mapViewModel) {
            classRoomState.nonNullObserve(this@MapFragment) {
                val list = mutableListOf<String>()
                it.groupBy { result -> result.building }.forEach { result ->
                    this@MapFragment.classRooms = result.value
                    showMapsMarkers(result.key, result.value)
                    list.add(result.key.name)
                }
                markers.forEach { marker ->
                    if (!list.contains(marker.first.title)) deleteMarker(marker.first.title)
                }
            }

            classRoomErrorState.nonNullObserve(this@MapFragment) {
                activity?.toast("${it.message}")
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(mapViewModel) {
            classRoomState.nonNullObserve(this@MapFragment) {
                it.groupBy { result -> result.building }.forEach { result ->
                    this@MapFragment.classRooms = result.value
                    showMapsMarkers(result.key, result.value)
                }
            }

            classRoomErrorState.nonNullObserve(this@MapFragment) {
                activity?.toast("${it.message}")
            }
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        with(mapViewModel) {
            classRoomState.nonNullObserve(this@MapFragment) {
                it.groupBy { result -> result.building }.forEach { result ->
                    this@MapFragment.classRooms = result.value
                    showMapsMarkers(result.key, result.value)
                }
            }

            classRoomErrorState.nonNullObserve(this@MapFragment) {
                activity?.toast("${it.message}")
            }
        }
    }

    private fun showMapsMarkers(
            building: BuildingOuterClass.Building,
            classRooms: List<ClassRooms.ClassRoom>
    ) {
        if (mMap != null) {
            /*以下のif文を消すと0人の時にも表示される*/
            val tagCountLevel = classRooms.sumBy { it.tagCountsCount }.tagCountLevel()

            markers.find { it.first.title == building.name }?.let {
                if (it.second != tagCountLevel) addMarker(building, classRooms, tagCountLevel)
            } ?: addMarker(building, classRooms, tagCountLevel)
        }
    }

    private fun addMarker(
            building: BuildingOuterClass.Building,
            classRooms: List<ClassRooms.ClassRoom>,
            tagCountLevel: TagCountLevel
    ) {
        mMap?.addMarker(MarkerOptions()
                .position(LatLng(building.latitude, building.longitude))
                .icon(BitmapDescriptorFactory.fromResource(tagCountLevel.markerIcon))
                .title(building.name))
                ?.apply { tag = Pair(building, classRooms) }?.let {
                    val list: MutableList<Pair<Marker, TagCountLevel>> = markers.apply {
                        removeIf { m -> it.title == m.first.title }
                    }

                    markers.clear()
                    markers.addAll(list.apply { add(Pair(it, tagCountLevel)) }.distinct())
                }

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

    private fun deleteMarker(markerTitle: String) {
        markers.find { it.first.title == markerTitle }?.let {
            markers.remove(it)
            it.first.remove()
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
                it.target(LatLng(NITKC_LATITUDE, NITKC_LONGITUDE))
                // いい感じになる値（適当）
                it.zoom((mMap?.maxZoomLevel ?: 10f) - 4f)
            }.build()))
            mapType = GoogleMap.MAP_TYPE_HYBRID
        }
    }

    fun reloadMarker(tags: List<Tags.Tag>) {
        mapViewModel.classRoom(tags)
    }

    private fun addMyMarker(classRoom: ClassRooms.ClassRoom) {
        val title = "${classRoom.building.name}(現在地)"
        myPositionMarker = mMap?.addMarker(MarkerOptions()
                .position(LatLng(classRoom.building.latitude + 0.00005, classRoom.building.longitude + 0.00005))
                .title(title))
                ?.apply { tag = Pair(classRoom.building, classRooms) }
    }

    fun addMyPositionMarker(classRoom: ClassRooms.ClassRoom) {
        val title = "${classRoom.building.name}(現在地)"
        myPositionMarker?.let {
            if (it.title != title) {
                it.remove()
                addMyMarker(classRoom)
            }
        }?: addMyMarker(classRoom)
    }

    companion object {
        private const val NITKC_LATITUDE = 33.33521728594884
        private const val NITKC_LONGITUDE = 130.5097379631427

        fun newInstance() =
                MapFragment().apply { arguments = bundleOf() }
    }
}
