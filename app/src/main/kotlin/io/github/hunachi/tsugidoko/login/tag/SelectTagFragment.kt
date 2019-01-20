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
        with(myTags) {
            add(tag)
            myTags.distinctBy { it.id }
        }
    }
    private val listAdapter = TagListAdapter(checkedListener)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tagListViewModel.apply {
            userState.nonNullObserve(this@SelectTagFragment) {
                myTags.addAll(
                        it.tagsList.map { tag -> tag.convertTag(true) }
                )
                myTags.distinctBy { tag -> tag.id }
                tagList()
            }

            userErrorState.nonNullObserve(this@SelectTagFragment) {
                activity?.toast("${it.message}")
            }

            tagListState.nonNullObserve(this@SelectTagFragment) {
                listAdapter.submitList(it.toMutableList().apply {
                    addAll(myTags)
                    distinctBy { tag -> tag.id }
                })
            }

            tagListErrorState.nonNullObserve(this@SelectTagFragment) {
                activity?.toast("${it.message}")
            }

            addTagState.nonNullObserve(this@SelectTagFragment) {
                activity?.toast("success !!")
                (activity as LoginActivity).finishSetup()
            }

            addTagsErrorState.nonNullObserve(this@SelectTagFragment) {
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
            (activity as? LoginActivity)?.finishSetup()
        }
        create_tag_button.setOnClickListener {
            (activity as? LoginActivity)?.changeFragment(CreateTagFragment.newInstance())
        }
    }

    companion object {
        fun newInstance() = SelectTagFragment()
    }
}
