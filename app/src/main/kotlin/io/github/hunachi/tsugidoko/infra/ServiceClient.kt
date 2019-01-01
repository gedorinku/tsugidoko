package io.github.hunachi.tsugidoko.infra


import io.github.hunachi.tsugidoko.BuildConfig
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import io.grpc.Metadata

abstract class ServiceClient {

    protected val channel: ManagedChannel = ManagedChannelBuilder
            .forAddress(BuildConfig.SERVER_URL, BuildConfig.PORT.toInt())
            .useTransportSecurity()
          //  .usePlaintext()
            .build()

    protected val header = Metadata()

    protected abstract fun setKey(sessionId: String)
}