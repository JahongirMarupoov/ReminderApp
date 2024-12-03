package uz.marupoov.reminderapp.domain

import kotlinx.coroutines.flow.Flow
import uz.marupoov.reminderapp.data.TaskData

interface AppRepository {

    fun getAllNotCompleteTask(): Flow<Result<List<TaskData>>>

    fun getAllCompleteTask(): Flow<Result<List<TaskData>>>

    fun deleteTask(taskData: TaskData) : Flow<Result<Unit>>

    fun insertTask(taskData: TaskData) : Flow<Result<Unit>>

    fun upDateTask(taskData: TaskData) : Flow<Result<Unit>>
}