package io.github.hunachi.tsugidoko

import io.github.hunachi.tsugidoko.map.MapViewModel
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val appModule = module {

    viewModel { MapViewModel() }
}