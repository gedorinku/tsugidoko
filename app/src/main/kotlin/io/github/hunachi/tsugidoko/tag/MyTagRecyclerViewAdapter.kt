package io.github.hunachi.tsugidoko.tag

import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import io.github.hunachi.tsugidoko.R
import io.github.hunachi.tsugidoko.model.Tag
import io.github.hunachi.tsugidoko.util.inflate

/**
 * [RecyclerView.Adapter] that can display a [DummyItem] and makes a call to the
 * specified [OnListFragmentInteractionListener].
 */

class MyTagRecyclerViewAdapter() : ListAdapter<Tag, MyTagRecyclerViewAdapter.ViewHolder>(DIFF_UTIL) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(parent.inflate(R.layout.fragment_tag, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.itemView.apply {

        }
    }

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    companion object {
        private val DIFF_UTIL = object : DiffUtil.ItemCallback<Tag>() {
            override fun areItemsTheSame(oldItem: Tag, newItem: Tag) = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: Tag, newItem: Tag) = oldItem == newItem
        }
    }
}
