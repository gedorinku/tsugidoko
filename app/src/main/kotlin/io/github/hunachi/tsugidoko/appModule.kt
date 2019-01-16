package io.github.hunachi.tsugidoko

import io.github.hunachi.tsugidoko.infra.ClassRoomServiceClient
import io.github.hunachi.tsugidoko.infra.SessionServiceClient
import io.github.hunachi.tsugidoko.infra.UserPositionServiceClient
import io.github.hunachi.tsugidoko.infra.UserServiceClient
import io.github.hunachi.tsugidoko.login.LoginViewModel
import io.github.hunachi.tsugidoko.map.MapViewModel
import io.github.hunachi.tsugidoko.util.setupSharedPreference
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val appModule = module {

    factory { UserPositionServiceClient() }

    factory { setupSharedPreference(get()) }

    factory { SessionServiceClient(get()) }

    factory { UserServiceClient() }

    factory { ClassRoomServiceClient() }

    viewModel { MapViewModel(get(), get(), get()) }

    viewModel { LoginViewModel(get(), get()) }

    viewModel { MainViewModel(get(), get()) }
}
