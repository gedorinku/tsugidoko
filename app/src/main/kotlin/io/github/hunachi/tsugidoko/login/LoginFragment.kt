package io.github.hunachi.tsugidoko.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.github.hunachi.tsugidoko.R
import io.github.hunachi.tsugidoko.login.tag.SelectTagFragment
import io.github.hunachi.tsugidoko.util.nonNullObserve
import io.github.hunachi.tsugidoko.util.toast
import kotlinx.android.synthetic.main.fragment_login.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment : Fragment() {

    private val loginViewModel: LoginViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        submitButton.setOnClickListener {
            val userName = identicalText.text.toString()
            val password = passwordText.text.toString()
            if (userName.isNotBlank() && password.isNotBlank()) {
                loginViewModel.submit(userName, password)
            }
        }
        changeLoginStateText.setOnClickListener {
            changeFragment(RegisterFragment.newInstance(identicalText.text.toString()))
        }

        submitButton.text = "ログイン"
        changeLoginStateText.text = "新規登録をしていない方はこちら！"
        identicalText.setText(arguments?.getString(ARG_USERNAME) ?: "")

        loginViewModel.apply {
            sessionIdState.nonNullObserve(this@LoginFragment) {
                changeFragment(SelectTagFragment.newInstance())
            }

            sessionIdErrorState.nonNullObserve(this@LoginFragment) {
                activity?.toast("${it.message}")
            }

            loadingState.nonNullObserve(this@LoginFragment) {}
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
                LoginFragment().apply {
                    arguments = Bundle().apply { ARG_USERNAME to userName }
                }
    }
}