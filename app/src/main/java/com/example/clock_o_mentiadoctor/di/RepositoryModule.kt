package com.fantasyclash.zeus.di


import com.example.clock_o_mentiadoctor.Repositories.RepoImplemenation.AuthRepoImplementation
import com.example.clock_o_mentiadoctor.Repositories.RepoInterfaces.AuthRepoInterface
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
internal abstract class RepositoryModule {

    @Binds
    abstract fun authRepo(authRepoImplementation: AuthRepoImplementation): AuthRepoInterface

}