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

    private var isLogin = true
    private val loginViewModel: LoginViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        with(submitButton) {
            setOnClickListener {
                val userName = identicalText.text.toString()
                val password = passwordText.text.toString()
                if (userName.isNotBlank() && password.isNotBlank()) {
                    loginViewModel.submit(userName, password)
                }
            }
            text = "ログイン"
        }
        with(changeLoginStateText) {
            text = "新規登録をしていない方はこちら！"
            setOnClickListener {
                isLogin = !isLogin
                submitButton.text = if (isLogin) "ログイン" else "登録"
                text = if (isLogin) "新規登録をしていない方はこちら！" else "登録済みの方はこちら！"
                identicalText.hint = if (isLogin) "login ID" else "mail address"
            }
        }
        loginViewModel.submitStatus.nonNullObserve(this) {
            when (it) {
                is NetworkState.Success -> {
                    toast(it.result.name)
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                }
                is NetworkState.Error -> toast(it.e.message ?: "hogehoge")
            }
        }
    }

    private fun successSubmit() {

    }

    private fun errorSubmit() {

    }
}
