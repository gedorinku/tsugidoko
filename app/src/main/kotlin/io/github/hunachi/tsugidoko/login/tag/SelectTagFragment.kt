package io.github.hunachi.tsugidoko.login.tag

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import io.github.hunachi.tsugidoko.R
import io.github.hunachi.tsugidoko.login.LoginActivity
import io.github.hunachi.tsugidoko.model.Tag
import io.github.hunachi.tsugidoko.model.convertTag
import io.github.hunachi.tsugidoko.util.nonNullObserve
import io.github.hunachi.tsugidoko.util.toast
import kotlinx.android.synthetic.main.fragment_tag_list.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class SelectTagFragment : Fragment() {

    private val tagListViewModel: TagListViewModel by viewModel()
    private val myTags: MutableList<Tag> = mutableListOf()
    private val checkedListener: (Tag) -> Unit = { tag: Tag ->
        if(tag.isSelected) myTags.add(tag)
        else myTags.removeIf { it.id == tag.id }
    }
    private val listAdapter = TagListAdapter(checkedListener)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tagListViewModel.apply {
            userState.nonNullObserve(this@SelectTagFragment) {
                myTags.run {
                    clear()
                    addAll(it.tagsList.map { tags -> tags.convertTag(true) })
                }
                tagList()
            }

            userErrorState.nonNullObserve(this@SelectTagFragment) {
                activity?.toast("${it.message}")
            }

            tagListState.nonNullObserve(this@SelectTagFragment) {
                it.toMutableList().apply {
                    replaceAll { tag -> myTags.find { myTag -> myTag.id == tag.id } ?: tag }
                }.let { tags ->
                    listAdapter.submitList(tags.toList())
                }
            }

            tagListErrorState.nonNullObserve(this@SelectTagFragment) {
                activity?.toast("${it.message}")
            }

            updateTagState.nonNullObserve(this@SelectTagFragment) {
                activity?.toast("success !!")
                (activity as LoginActivity).finishSetup()
            }

            updateTagsErrorState.nonNullObserve(this@SelectTagFragment) {
                activity?.toast("${it.message}")
            }
        }.user()
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ) = inflater.inflate(R.layout.fragment_tag_list, container, false)!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        list.apply {
            adapter = listAdapter
            layoutManager = LinearLayoutManager(context)
        }
        submit_button.setOnClickListener {
            tagListViewModel.updateTags(myTags)
        }
        create_tag_button.setOnClickListener {
            (activity as? LoginActivity)?.changeFragment(CreateTagFragment.newInstance())
        }
    }

    companion object {
        fun newInstance() = SelectTagFragment()
    }
}
