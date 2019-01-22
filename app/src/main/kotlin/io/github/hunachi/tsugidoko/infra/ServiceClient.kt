package io.github.hunachi.tsugidoko.infra

import androidx.annotation.CheckResult
import io.github.hunachi.tsugidoko.BuildConfig
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import io.grpc.Metadata

abstract class ServiceClient {

    protected val channel: ManagedChannel = ManagedChannelBuilder
            .forAddress(BuildConfig.SERVER_URL, BuildConfig.PORT.toInt())
            .useTransportSecurity()
            .build()

    abstract fun setUp(sessionId: String)

    @CheckResult
    protected fun setKeyMetadata(sessionId: String) =
            Metadata().apply {
                put(Metadata.Key.of(
                        "authorization",
                        Metadata.ASCII_STRING_MARSHALLER),
                        "session $sessionId"
                )
            }
}