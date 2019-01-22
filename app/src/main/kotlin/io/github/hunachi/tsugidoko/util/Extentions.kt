package io.github.hunachi.tsugidoko.util

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import io.github.hunachi.tsugidoko.R
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.*
import android.net.ConnectivityManager
import android.widget.Toast

// lazy of none safety but fast thread mode
fun <T> lazyFast(operation: () -> T) = lazy(LazyThreadSafetyMode.NONE) {
    operation()
}

fun ViewGroup.inflate(
        @LayoutRes layout: Int, attachToRoot: Boolean = false
): View {
    return LayoutInflater.from(context).inflate(layout, this, attachToRoot)
}

inline fun FragmentManager.inTransaction(
        func: FragmentTransaction.() -> FragmentTransaction
) {
    beginTransaction().setCustomAnimations(R.animator.slide_up, R.animator.slide_down).func().commit()
}

fun AppCompatActivity.startActivity(next: AppCompatActivity) {
    startActivity(Intent(this, next.javaClass))
}

val <T> T.checkAllMatched: T
    get() = this

fun <T> LiveData<T>.nonNullObserve(owner: LifecycleOwner, observer: (T) -> Unit) {
    this.observe(owner, Observer { this.value?.let(observer) })
}

fun <T> LiveData<T>.observe(owner: LifecycleOwner, observer: (T?) -> Unit) {
    this.observe(owner, Observer(observer))
}

fun Context.netWorkCheck(): Boolean {
    val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val info = cm.activeNetworkInfo
    return info?.isConnected ?: false
}

fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Context.toastNetworkError(error: NetWorkError) {
    toast(when {
        !netWorkCheck() -> "ネット環境の確認をお願いにゃ！"
        error == NetWorkError.FIN -> "読み込めるものがもうないにゃ！"
        else -> "えらーにゃーん"
    })
}


enum class NetWorkError {
    TOKEN, NORMAL, FIN, POST
}