package uz.marupoov.reminderapp.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import uz.marupoov.reminderapp.domain.AppRepository
import uz.marupoov.reminderapp.domain.impl.AppRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface AppRepositoryModule {

    @[Binds Singleton]
    fun bindAppRepository(impl : AppRepositoryImpl) : AppRepository
}