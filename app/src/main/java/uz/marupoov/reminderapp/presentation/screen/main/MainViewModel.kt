package uz.marupoov.reminderapp.presentation.screen.main

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
class MainViewModel @Inject constructor(
    private val appRepository: AppRepository,
    private val appNavigator: AppNavigator
) : ViewModel() {
    val allNotCompleteTask =
        MutableSharedFlow<List<TaskData>>(replay = 1, onBufferOverflow = BufferOverflow.DROP_LATEST)

    fun requestAllNotCompleteTask() {
        appRepository.getAllNotCompleteTask()
            .onEach {
                it.onSuccess { list ->
                    allNotCompleteTask.tryEmit(list)
                }
                it.onFailure { thr ->
                    myLogger(thr.message ?: "Nomalum xatoli!")
                }
            }.launchIn(viewModelScope)
    }

    fun onClickBtnAdd() {
        viewModelScope.launch {
            appNavigator.navigateTo(MainScreenDirections.actionMainScreenToAddTaskScreen())
        }
    }

    fun onClickAllCompleteBtn() {
        viewModelScope.launch {
            appNavigator.navigateTo(MainScreenDirections.actionMainScreenToAllCompleteScreen())
        }
    }

    fun onCompleteChangeTask(task: TaskData) {
        appRepository.upDateTask(task)
            .onEach {
                requestAllNotCompleteTask()
            }.launchIn(viewModelScope)
    }
}