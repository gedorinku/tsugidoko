package io.github.hunachi.tsugidoko.detailMap

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import io.github.hunachi.tsugidoko.model.FloorRooms

class DetailMapPagerAdapter(
        fm: FragmentManager,
        val rooms: List<FloorRooms>
) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment = FloorFragment.newInstance(rooms[position])

    override fun getPageTitle(position: Int) = rooms[position].floor.toString()

    override fun getCount() = rooms.size
}