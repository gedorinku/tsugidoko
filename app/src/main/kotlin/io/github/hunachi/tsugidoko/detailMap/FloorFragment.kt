package io.github.hunachi.tsugidoko.detailMap

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import io.github.hunachi.tsugidoko.R
import io.github.hunachi.tsugidoko.model.FloorRooms
import io.github.hunachi.tsugidoko.util.inflate
import kotlinx.android.synthetic.main.fragment_floor.view.*


class FloorFragment : Fragment() {

    private lateinit var floorRooms: FloorRooms

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            floorRooms = it.getSerializable(ARG_FLOOR) as FloorRooms
        } ?: IllegalAccessException()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
            container!!.inflate(R.layout.fragment_floor).apply {
                text.text = floorRooms.floor.toString()
            }

    companion object {
        const val ARG_FLOOR = "floor"

        fun newInstance(floorRooms: FloorRooms) = FloorFragment().apply {
            arguments = bundleOf(ARG_FLOOR to floorRooms)
        }
    }
}
