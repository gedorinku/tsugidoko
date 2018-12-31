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
import io.github.hunachi.tsugidoko.model.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class MapFragment : Fragment(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private val mapViewModel: MapViewModel by viewModel()


    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private val dummyTags = listOf<Tag>(Tag(0,"dummy0"),Tag(1,"dummy1"),Tag(2,"dummy2"))
    private val dummyUser = User(0,"dummy", dummyTags)
    private val dummyClassRoom0 = ClassRoom(0,"dummyClassRoom0", listOf(),-34.0 + Random().nextInt(10) % 10,151.0 + Random().nextInt(10) % 10,0,0,
        listOf<TagCount>(TagCount(Tag(0,"dummy0"),Random().nextInt(10)),TagCount(Tag(1,"dummy1"),Random().nextInt(10))))
    private val dummyClassRoom1 = ClassRoom(1,"dummyClassRoom1", listOf(),-34.0 + Random().nextInt(10) % 10,151.0 + Random().nextInt(10) % 10,0,0,
        listOf<TagCount>(TagCount(Tag(1,"dummy1"),Random().nextInt(10)),TagCount(Tag(2,"dummy2"),Random().nextInt(10))))
    private val dummyClassRoom2 = ClassRoom(2,"dummyClassRoom2", listOf(),-34.0 + Random().nextInt(10) % 10,151.0 + Random().nextInt(10) % 10,0,0,
        listOf<TagCount>(TagCount(Tag(2,"dummy2"),Random().nextInt(10)),TagCount(Tag(0,"dummy0"),Random().nextInt(10))))
    private val dummyClassRooms = listOf<ClassRoom>(dummyClassRoom0,dummyClassRoom1,dummyClassRoom2)
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

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
        for(classRoom in dummyClassRooms){
            if(!isExistGroup(dummyUser,classRoom))continue;
            val classRoomPosition = LatLng(classRoom.latitude,classRoom.longitude)
            mMap.addMarker(MarkerOptions().position(classRoomPosition).title("Marker in classRoom"+classRoom.id))
        }
        val sydney = LatLng(-34.0, 151.0)
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }

    private fun isExistGroup(user: User, classRoom: ClassRoom): Boolean {
        for(targetTag in user.tags) {
            for (tagCount in classRoom.tagCounts){
                if(targetTag!=tagCount.tag)continue;
                if(tagCount.Count>=5)return true;
            }
        }
        return false;
    }

    companion object {
        fun newInstance() =
            MapFragment().apply { arguments = bundleOf() }
    }
}
