package uz.marupoov.reminderapp.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import uz.marupoov.reminderapp.data.TaskDatabase
import uz.marupoov.reminderapp.data.dao.TaskDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class LocalSourceModule {

    @[Provides Singleton]
    fun provideTaskDatabase(@ApplicationContext context: Context): TaskDatabase {
        return Room
            .databaseBuilder(context, TaskDatabase::class.java, "ReminderBahaDew.db")
            .build()
    }

    @[Provides Singleton]
    fun provideTaskDao(taskDatabase: TaskDatabase): TaskDao {
        return taskDatabase.getTaskDao()
    }
}