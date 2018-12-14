package io.github.hunachi.tsugidoko

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import io.github.hunachi.tsugidoko.databinding.ActivitySelectTagBinding

class SelectTagActivity : AppCompatActivity() {

    private val binding by lazy {
        DataBindingUtil.setContentView<ActivitySelectTagBinding>(this, R.layout.activity_select_tag)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.apply {  }
    }
}
