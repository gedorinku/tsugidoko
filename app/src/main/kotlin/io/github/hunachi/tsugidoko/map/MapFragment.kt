package io.github.hunachi.tsugidoko.map

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import gedorinku.tsugidoko_server.Users
import io.github.hunachi.tsugidoko.R
import io.github.hunachi.tsugidoko.model.*
import io.github.hunachi.tsugidoko.util.NetworkState
import io.github.hunachi.tsugidoko.util.nonNullObserve
import org.koin.androidx.viewmodel.ext.android.viewModel


class MapFragment : Fragment(), OnMapReadyCallback {

    private var mMap: GoogleMap? = null
    private val mapViewModel: MapViewModel by viewModel()
    private var classRooms: List<ClassRoom> = listOf()
    private var user: Users.User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mapViewModel.apply {
            submitStatus.nonNullObserve(this@MapFragment) {
                when (it) {
                    is NetworkState.Success<Pair<List<ClassRoom>, Users.User>> -> {
                        classRooms = it.result.first
                        user = it.result.second
                        reloadMarker()
                    }
                    is NetworkState.Error<*> -> {
                        reloadMarker()
                    }
                }
            }
        }.submit()
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

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        reloadMarker()
        /*for (classRoom in dummyClassRooms) {
            //if (!isExistGroup(dummyUser, classRoom)) continue
            val classRoomPosition = LatLng(classRoom.latitude, classRoom.longitude)
            mMap?.addMarker(MarkerOptions().position(classRoomPosition).title("Marker in classRoom" + classRoom.id))
        }*/
    }

    /*private fun isExistGroup(user: User, classRoom: ClassRoom): Boolean {
        for (targetTag in user.tags) {
            for (tagCount in classRoom.tagCounts) {
                if (targetTag != tagCount.tag) continue
                if (tagCount.Count >= 5) return true
            }
        }
        return false
    }
*/
    fun reloadMarker() {
        if(user != null && mMap != null){
            classRooms.groupBy { it.buildingId }.flatMap { it.component2() }.filter {
                it.tagCounts.filter { tagCount ->
                    user?.tagsList?.map { tag -> tag.id }?.contains(tagCount.tag.id) ?: false
                }.size >= 5
            }.forEach {
                mMap?.addMarker(MarkerOptions().position(LatLng(it.latitude, it.longitude)).title("ここには5人以上の人がいます．"))
            }
        }
    }

    companion object {
        fun newInstance() =
                MapFragment().apply { arguments = bundleOf() }
    }
}
