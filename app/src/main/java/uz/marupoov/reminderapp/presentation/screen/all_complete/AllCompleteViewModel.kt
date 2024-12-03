package uz.marupoov.reminderapp.presentation.screen.all_complete

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import uz.marupoov.reminderapp.data.TaskData
import uz.marupoov.reminderapp.domain.AppRepository
import uz.marupoov.reminderapp.navigation.AppNavigator
import uz.marupoov.reminderapp.utils.myLogger
import javax.inject.Inject

@HiltViewModel
class AllCompleteViewModel @Inject constructor(
    private val appNavigator: AppNavigator,
    private val appRepository: AppRepository
) : ViewModel() {
    val allCompleteTask =
        MutableSharedFlow<List<TaskData>>(replay = 1, onBufferOverflow = BufferOverflow.DROP_LATEST)

    fun requestAllCompleteTask() {
        appRepository.getAllCompleteTask()
            .onEach {
                it.onSuccess { list ->
                    allCompleteTask.tryEmit(list)
                }
                it.onFailure { thr ->
                    myLogger(thr.message ?: "Nomalum xatoli!")
                }
            }.launchIn(viewModelScope)
    }

    fun onClickBack() {
        viewModelScope.launch {
            appNavigator.popBackStack()
        }
    }

    fun onCompleteChangeTask(it: TaskData) {
        appRepository.upDateTask(it)
            .onEach {
                requestAllCompleteTask()
            }.launchIn(viewModelScope)
    }

    fun onClickDelete(task: TaskData) {
        appRepository.deleteTask(task)
            .onEach {
                requestAllCompleteTask()
            }.launchIn(viewModelScope)
    }
}