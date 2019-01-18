package io.github.hunachi.tsugidoko.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.github.hunachi.tsugidoko.R
import io.github.hunachi.tsugidoko.login.tag.SelectTagFragment
import io.github.hunachi.tsugidoko.util.NetworkState
import io.github.hunachi.tsugidoko.util.nonNullObserve
import kotlinx.android.synthetic.main.fragment_register.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class RegisterFragment : Fragment() {

    private val registerViewModel: RegisterViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        submitButton.setOnClickListener {
            val userName = identicalText.text.toString()
            val password = passwordText.text.toString()
            if (userName.isNotBlank() && password.isNotBlank()) {
                registerViewModel.submit(userName, password)
            }
        }

        changeLoginStateText.setOnClickListener {
            changeFragment(LoginFragment.newInstance(identicalText.text.toString()))
        }

        submitButton.text = "登録"
        changeLoginStateText.text = "登録済みの方はこちら！"
        identicalText.setText(arguments?.getString(ARG_USERNAME) ?: "")

        registerViewModel.submitStatus.nonNullObserve(this) {
            when (it) {
                is NetworkState.Success -> {
                    changeFragment(SelectTagFragment.newInstance())
                }
                is NetworkState.Error -> {
                }
            }
        }
    }

    private fun changeFragment(fragment: Fragment) {
        activity?.let {
            if (it is LoginActivity) it.changeFragment(fragment)
        }
    }

    companion object {
        private const val ARG_USERNAME = "userName"

        fun newInstance(userName: String? = null) =
                RegisterFragment().apply {
                    arguments = Bundle().apply { ARG_USERNAME to userName }
                }
    }
}
