package uz.marupoov.reminderapp.presentation.screen.add_task

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import uz.marupoov.reminderapp.data.TaskData
import uz.marupoov.reminderapp.domain.AppRepository
import uz.marupoov.reminderapp.navigation.AppNavigator
import javax.inject.Inject

@HiltViewModel
class AddTaskViewModel @Inject constructor(
    private val appRepository: AppRepository,
    private val appNavigator: AppNavigator
) : ViewModel() {
    fun onClickBack() {
        viewModelScope.launch {
            appNavigator.popBackStack()
        }
    }

    fun onClickAdd(task: String, date: Long, time: Long, uuid: String) {
        val taskData = TaskData(
            id = 0,
            task = task,
            date = date,
            time = time,
            uuid = uuid,
            completeState = 0
        )
        appRepository.insertTask(taskData)
            .onEach {
                appNavigator.popBackStack()
            }.launchIn(viewModelScope)
    }
}