package io.github.hunachi.tsugidoko.infra


import android.content.SharedPreferences
import androidx.annotation.CheckResult
import io.github.hunachi.tsugidoko.BuildConfig
import io.github.hunachi.tsugidoko.util.session
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import io.grpc.Metadata
import java.lang.IllegalStateException

abstract class ServiceClient(private val preferences: SharedPreferences) {

    protected val channel: ManagedChannel = ManagedChannelBuilder
            .forAddress(BuildConfig.SERVER_URL, BuildConfig.PORT.toInt())
            .useTransportSecurity()
            .build()

    @CheckResult
    protected fun setKeyMetadata() =
            Metadata().apply {
                preferences.session()?.also {
                    put(Metadata.Key.of(
                            "authorization",
                            Metadata.ASCII_STRING_MARSHALLER),
                            "session $it"
                    )
                } ?: throw IllegalStateException("sessionId is not found!!")
            }
}