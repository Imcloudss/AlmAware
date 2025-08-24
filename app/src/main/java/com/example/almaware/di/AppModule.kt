package com.example.almaware.di

import com.example.almaware.data.repository.AuthRepository
import com.example.almaware.data.repository.BadgeRepository
import com.example.almaware.data.repository.KeywordRepository
import com.example.almaware.data.repository.ProjectRepository
import com.example.almaware.data.repository.SDGRepository
import com.example.almaware.data.repository.StudentRepository
import com.example.almaware.data.repository.UserRepository
import com.example.almaware.ui.screens.auth.AuthViewModel
import com.example.almaware.ui.screens.flower.FlowerViewModel
import com.example.almaware.ui.screens.sdg.SdgViewModel
import com.example.almaware.ui.screens.sdg.student.StudentViewModel
import com.example.almaware.ui.screens.sdg.unibo.UniboViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    // Firebase
    single { com.google.firebase.auth.FirebaseAuth.getInstance() }
    single { com.google.firebase.database.FirebaseDatabase.getInstance().reference }

    // Repositories
    single { SDGRepository() }
    single { ProjectRepository() }
    single { KeywordRepository() }
    single { StudentRepository() }
    single { BadgeRepository() }
    single { AuthRepository(get(), get()) }
    single { UserRepository(get()) }

    // ViewModels
    viewModel { SdgViewModel(get()) }
    viewModel { UniboViewModel(get()) }
    viewModel { StudentViewModel(get(), get(), get()) }
    viewModel { AuthViewModel(get()) }
    viewModel { FlowerViewModel(get(), get()) }
}
