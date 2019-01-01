package io.github.hunachi.tsugidoko.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import io.github.hunachi.tsugidoko.MainActivity
import io.github.hunachi.tsugidoko.R
import io.github.hunachi.tsugidoko.util.NetworkState
import io.github.hunachi.tsugidoko.util.nonNullObserve
import io.github.hunachi.tsugidoko.util.toast
import kotlinx.android.synthetic.main.activity_login.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : AppCompatActivity() {

    private var isRegister = false
    private val loginViewModel: LoginViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        with(submitButton) {
            setOnClickListener {
                val userName = identicalText.text.toString()
                val password = passwordText.text.toString()
                if (userName.isNotBlank() && password.isNotBlank()) {
                    loginViewModel.submit(userName, password, isRegister)
                }
            }
            text = "ログイン"
        }
        with(changeLoginStateText) {
            text = "新規登録をしていない方はこちら！"
            setOnClickListener {
                isRegister = !isRegister
                submitButton.text = if (isRegister) "登録" else "ログイン"
                text = if (isRegister) "登録済みの方はこちら！" else "新規登録をしていない方はこちら！"
                identicalText
            }
        }
        loginViewModel.submitStatus.nonNullObserve(this) {
            when (it) {
                is NetworkState.Success -> finish()
                is NetworkState.Error -> toast(it.e.message ?: "hogehoge")
            }
        }
    }
}
