package com.example.almaware.di

import com.example.almaware.data.repository.BadgeRepository
import com.example.almaware.data.repository.KeywordRepository
import com.example.almaware.data.repository.ProjectRepository
import com.example.almaware.data.repository.SDGRepository
import com.example.almaware.data.repository.StudentRepository
import com.example.almaware.ui.screens.sdg.SdgViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    // Repositories
    single { SDGRepository() }
    single { ProjectRepository() }
    single { KeywordRepository() }
    single { StudentRepository() }
    single { BadgeRepository() }

    // ViewModels (da creare)
    viewModel { SdgViewModel(get()) }
//    viewModel { ProjectViewModel(get()) }
//    viewModel { StudentViewModel(get()) }
}
