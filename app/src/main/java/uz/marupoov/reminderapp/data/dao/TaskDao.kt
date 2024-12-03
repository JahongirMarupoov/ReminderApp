package uz.marupoov.reminderapp.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import uz.marupoov.reminderapp.data.TaskData

@Dao
abstract class TaskDao {

    @Update
    abstract fun updateTask(task: TaskData)

    @Insert
    abstract fun insertTask(task: TaskData)

    @Delete
    abstract fun deleteTask(taskData: TaskData)

    @Query("SELECT * FROM TaskData WHERE completeState = 1")
    abstract fun getAllCompleteTask(): List<TaskData>

    @Query("SELECT * FROM TaskData WHERE completeState = 0")
    abstract fun getAllNotCompleteTask(): List<TaskData>
}