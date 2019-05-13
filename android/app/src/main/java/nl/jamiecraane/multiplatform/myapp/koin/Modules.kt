package nl.jamiecraane.multiplatform.myapp.koin

import nl.jamiecraane.multiplatform.myapp.main.viewmodel.MainViewModel
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val viewModels = module {
    viewModel { MainViewModel() }
}

val modules = listOf(viewModels)