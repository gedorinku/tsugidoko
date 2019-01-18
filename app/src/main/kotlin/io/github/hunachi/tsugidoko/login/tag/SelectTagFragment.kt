package io.github.hunachi.tsugidoko.login.tag

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.github.hunachi.tsugidoko.R

/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * [SelectTagFragment.OnListFragmentInteractionListener] interface.
 */
class SelectTagFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_tag_list, container, false)

        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onDetach() {
        super.onDetach()
    }

    companion object {

        const val ARG_COLUMN_COUNT = "column-count"

        fun newInstance() =
            SelectTagFragment().apply {}
    }
}
