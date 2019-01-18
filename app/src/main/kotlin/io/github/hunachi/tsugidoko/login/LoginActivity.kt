package io.github.hunachi.tsugidoko.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import io.github.hunachi.tsugidoko.R
import io.github.hunachi.tsugidoko.util.inTransaction

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        supportFragmentManager.inTransaction { add(R.id.container, RegisterFragment.newInstance()) }
    }

    fun changeFragment(fragment: Fragment) {
        supportFragmentManager.inTransaction { replace(R.id.container, fragment) }
    }
}
