package io.github.hunachi.tsugidoko.detailMap

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.viewpager.widget.ViewPager
import io.github.hunachi.tsugidoko.R
import io.github.hunachi.tsugidoko.util.inflate
import kotlinx.android.synthetic.main.fragment_detail_map.view.*
import androidx.fragment.app.Fragment
import io.github.hunachi.tsugidoko.model.Building


class DetailMapFragment : Fragment(), ViewPager.OnPageChangeListener {

    private val detailMap: Building by lazy {
        arguments?.getSerializable(ARG_BUILDING_ID) as Building
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return container!!.inflate(R.layout.fragment_detail_map).apply {
            viewpager.adapter = DetailMapPagerAdapter(childFragmentManager, detailMap.rooms.sortedBy { it.floor })
            viewpagertab.setViewPager(viewpager)
        }
    }

    override fun onPageScrollStateChanged(state: Int) {}

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

    override fun onPageSelected(position: Int) {}

    companion object {
        val ARG_BUILDING_ID = "building-id"
        fun newInstance(building: Building) =
                DetailMapFragment().apply {
                    arguments = bundleOf(ARG_BUILDING_ID to building)
                }
    }
}
