package uz.marupoov.reminderapp.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import uz.marupoov.reminderapp.navigation.AppNavigationDispatcher
import uz.marupoov.reminderapp.navigation.AppNavigationHandler
import uz.marupoov.reminderapp.navigation.AppNavigator
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface AppNavigationModule {

    @[Binds Singleton]
    fun bindAppNavigator(impl: AppNavigationDispatcher): AppNavigator

    @[Binds Singleton]
    fun bindAppNavigationHandler(impl: AppNavigationDispatcher): AppNavigationHandler

}