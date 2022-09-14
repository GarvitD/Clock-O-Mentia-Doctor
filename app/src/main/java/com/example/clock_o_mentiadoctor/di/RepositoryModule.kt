package com.fantasyclash.zeus.di


import com.example.clock_o_mentiadoctor.Repositories.RepoImplemenation.AppointmentRepoImplementation
import com.example.clock_o_mentiadoctor.Repositories.RepoImplemenation.AuthRepoImplementation
import com.example.clock_o_mentiadoctor.Repositories.RepoImplemenation.ProfileSetupImplementation
import com.example.clock_o_mentiadoctor.Repositories.RepoInterfaces.AppointmentInterface
import com.example.clock_o_mentiadoctor.Repositories.RepoInterfaces.AuthRepoInterface
import com.example.clock_o_mentiadoctor.Repositories.RepoInterfaces.ProfileSetupInterface
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
internal abstract class RepositoryModule {

    @Binds
    abstract fun authRepo(authRepoImplementation: AuthRepoImplementation): AuthRepoInterface

    @Binds
    abstract fun profileSetup(profileSetupImplementation: ProfileSetupImplementation): ProfileSetupInterface

    @Binds
    abstract fun getAppointment(appointmentRepoImplementation: AppointmentRepoImplementation): AppointmentInterface

}