package io.github.hunachi.tsugidoko.login.tag

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import io.github.hunachi.tsugidoko.MainActivity
import io.github.hunachi.tsugidoko.R
import io.github.hunachi.tsugidoko.login.LoginActivity
import io.github.hunachi.tsugidoko.model.Tag
import kotlinx.android.synthetic.main.fragment_tag_list.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class SelectTagFragment : Fragment() {

    private val listAdapter = TagListAdapter()
    private val tagListViewModel: TagListViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        listAdapter.submitList(listOf(Tag(0, "0"), Tag(1, "1")))
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
            when(activity){
                is LoginActivity -> (activity as LoginActivity).finishSetup()
                is MainActivity -> {}
            }
        }
    }

    companion object {

        const val ARG_COLUMN_COUNT = "column-count"

        fun newInstance() = SelectTagFragment()
    }
}
