package io.github.hunachi.tsugidoko.detailMap

import android.content.Context
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
import io.github.hunachi.tsugidoko.model.DetailMap


class DetailMapFragment : Fragment(), ViewPager.OnPageChangeListener {

    private val detailMap: DetailMap by lazy {
        arguments?.getSerializable(ARG_BUILDING_ID) as DetailMap
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return container!!.inflate(R.layout.fragment_detail_map).apply {
            viewpager.adapter = DetailMapPagerAdapter(childFragmentManager, detailMap.rooms)
            viewpagertab.setViewPager(viewpager)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onPageScrollStateChanged(state: Int) {}

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

    override fun onPageSelected(position: Int) {}

    companion object {
        val ARG_BUILDING_ID = "building-id"
        fun newInstance(detailMap: DetailMap) =
                DetailMapFragment().apply {
                    arguments = bundleOf(ARG_BUILDING_ID to detailMap)
                }
    }
}
