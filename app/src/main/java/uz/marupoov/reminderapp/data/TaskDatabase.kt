package uz.marupoov.reminderapp.data

import androidx.room.Database
import androidx.room.RoomDatabase
import uz.marupoov.reminderapp.data.dao.TaskDao

@Database(entities = [TaskData::class], version = 1)
abstract class TaskDatabase : RoomDatabase() {
    abstract fun getTaskDao() : TaskDao
}