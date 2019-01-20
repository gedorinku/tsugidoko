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
import io.github.hunachi.tsugidoko.util.nonNullObserve
import io.github.hunachi.tsugidoko.util.toast
import kotlinx.android.synthetic.main.fragment_tag_list.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class SelectTagFragment : Fragment() {

    private val listAdapter = TagListAdapter()
    private val tagListViewModel: TagListViewModel by viewModel()
    private lateinit var tags: List<Tag>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tagListViewModel.apply {
            tagListState.nonNullObserve(this@SelectTagFragment){
                listAdapter.submitList(it)
            }

            tagListErrorState.nonNullObserve(this@SelectTagFragment){
                activity?.toast("${it.message}")
            }

            addTagState.nonNullObserve(this@SelectTagFragment){
                activity?.toast("success !!")
                (activity as LoginActivity).finishSetup()
            }

            addTagsErrorState.nonNullObserve(this@SelectTagFragment){
                activity?.toast("${it.message}")
            }
        }.tagList()
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
        button.setOnClickListener {
            (activity as LoginActivity).finishSetup()
        }
    }

    companion object {
        fun newInstance() = SelectTagFragment()
    }
}
