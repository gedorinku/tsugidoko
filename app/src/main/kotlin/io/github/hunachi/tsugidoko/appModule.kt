package io.github.hunachi.tsugidoko

import io.github.hunachi.tsugidoko.infra.*
import io.github.hunachi.tsugidoko.login.LoginViewModel
import io.github.hunachi.tsugidoko.login.RegisterViewModel
import io.github.hunachi.tsugidoko.login.tag.TagListViewModel
import io.github.hunachi.tsugidoko.map.MapViewModel
import io.github.hunachi.tsugidoko.util.setupSharedPreference
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val appModule = module {

    factory { UserPositionServiceClient(get()) }

    factory { setupSharedPreference(get()) }

    factory { SessionServiceClient(get(), get()) }

    factory { UserServiceClient(get()) }

    factory { ClassRoomServiceClient(get()) }

    factory { TagServiceClient(get()) }

    viewModel { MapViewModel(get()) }

    viewModel { LoginViewModel(get(), get()) }

    viewModel { MainViewModel(get(), get()) }

    viewModel { RegisterViewModel(get(), get()) }

    viewModel { TagListViewModel(get(), get()) }
}
