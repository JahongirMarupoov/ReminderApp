package uz.marupoov.reminderapp.domain.impl

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import uz.marupoov.reminderapp.data.TaskData
import uz.marupoov.reminderapp.data.dao.TaskDao
import uz.marupoov.reminderapp.domain.AppRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppRepositoryImpl @Inject constructor(
    private val taskDao: TaskDao
) : AppRepository {

    override fun getAllNotCompleteTask() = callbackFlow {
        trySend(Result.success(taskDao.getAllNotCompleteTask()))
        awaitClose()
    }.flowOn(Dispatchers.IO).catch { emit(Result.failure(it)) }

    override fun getAllCompleteTask() = callbackFlow {
        trySend(Result.success(taskDao.getAllCompleteTask()))
        awaitClose()
    }.flowOn(Dispatchers.IO).catch { emit(Result.failure(it)) }

    override fun deleteTask(taskData: TaskData) = callbackFlow {
        taskDao.deleteTask(taskData)
        trySend(Result.success(Unit))
        awaitClose()
    }.flowOn(Dispatchers.IO).catch { emit(Result.failure(it)) }

    override fun insertTask(taskData: TaskData) = callbackFlow {
        taskDao.insertTask(taskData)
        trySend(Result.success(Unit))
        awaitClose()
    }.flowOn(Dispatchers.IO).catch { emit(Result.failure(it)) }

    override fun upDateTask(taskData: TaskData) = callbackFlow {
        taskDao.updateTask(taskData)
        trySend(Result.success(Unit))
        awaitClose()
    }.flowOn(Dispatchers.IO).catch { emit(Result.failure(it)) }
}