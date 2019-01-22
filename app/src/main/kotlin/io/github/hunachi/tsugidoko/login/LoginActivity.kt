package io.github.hunachi.tsugidoko.login

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import io.github.hunachi.tsugidoko.MainActivity
import io.github.hunachi.tsugidoko.R
import io.github.hunachi.tsugidoko.login.tag.SelectTagFragment
import io.github.hunachi.tsugidoko.util.inTransaction


class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        requestPermission()

        /*MainActivityとかからTagを追加したいって時かどうか調べる．*/
        if (intent.getBooleanExtra(EXT_TO_SELECT_TAG, false)) {
            changeFragment(SelectTagFragment.newInstance())
        } else changeFragment(RegisterFragment.newInstance())
    }

    fun changeFragment(fragment: Fragment) {
        supportFragmentManager.inTransaction { replace(R.id.container, fragment) }
    }

    fun finishSetup() {
        if (intent.getBooleanExtra(EXT_TO_SELECT_TAG, false)) {
            finish()
        } else {
            MainActivity.start(this)
        }
    }

    private val reqPermissions: MutableList<String> = mutableListOf()

    private fun requestPermission() {
        listOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
                .forEachIndexed { i, s ->
                    if (ContextCompat.checkSelfPermission(this, s) != PackageManager.PERMISSION_GRANTED) {
                        reqPermissions.add(s)
                    }
                }
        if (reqPermissions.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, reqPermissions.toTypedArray(), 0)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == 0) {
            if (permissions.size != reqPermissions.size ||
                    grantResults.any { it != PackageManager.PERMISSION_GRANTED }) requestPermission()
        }
    }

    companion object {
        private const val EXT_TO_SELECT_TAG = "to_select_tags"

        fun start(context: Context, toSelectTag: Boolean = false) =
                context.startActivity(Intent(context, LoginActivity::class.java).apply {
                    putExtra(EXT_TO_SELECT_TAG, toSelectTag)
                })
    }
}
