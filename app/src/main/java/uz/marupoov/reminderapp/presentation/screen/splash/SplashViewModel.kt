package uz.marupoov.reminderapp.presentation.screen.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import uz.marupoov.reminderapp.domain.AppRepository
import uz.marupoov.reminderapp.navigation.AppNavigator
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val appRepository: AppRepository,
    private val appNavigator: AppNavigator
) : ViewModel() {

    fun openSplash() {
        viewModelScope.launch {
            appNavigator.navigateTo(SplashScreenDirections.actionSplashScreenToMainScreen())
        }
    }
}