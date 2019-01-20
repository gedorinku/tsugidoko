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
import kotlinx.android.synthetic.main.fragment_create_tag.*
import kotlinx.android.synthetic.main.fragment_tag_list.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class CreateTagFragment : Fragment() {

    private val tagViewModel: CreateTagViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tagViewModel.apply {
            createTagState.nonNullObserve(this@CreateTagFragment) {
                activity?.toast("タグの作成に成功しました．")
                (activity as? LoginActivity)?.changeFragment(SelectTagFragment.newInstance())
            }

            createTagErrorState.nonNullObserve(this@CreateTagFragment) {
                activity?.toast("${it.message}")
            }
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ) = inflater.inflate(R.layout.fragment_create_tag, container, false)!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        createButton.setOnClickListener {
            tagNameText.text.toString().let { name ->
                if (name.isNotBlank()) tagViewModel.createTag(Tag(name = name))
                else activity?.toast("何か入力してください！")
            }
        }
    }

    companion object {
        fun newInstance() = CreateTagFragment()
    }
}
