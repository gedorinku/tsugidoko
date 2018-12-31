package io.github.hunachi.tsugidoko

import io.github.hunachi.tsugidoko.infra.SessionServiceClient
import io.github.hunachi.tsugidoko.infra.UserServiceClient
import io.github.hunachi.tsugidoko.login.LoginViewModel
import io.github.hunachi.tsugidoko.map.MapViewModel
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val appModule = module {
    factory { SessionServiceClient() }

    factory { UserServiceClient(get()) }

    viewModel { MapViewModel() }

    viewModel { LoginViewModel(get()) }
}
