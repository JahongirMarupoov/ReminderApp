package uz.marupoov.reminderapp.navigation

import androidx.navigation.NavDirections
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppNavigationDispatcher @Inject constructor() : AppNavigator, AppNavigationHandler {
    override val buffer =
        MutableSharedFlow<AppNavigation>(replay = 1, onBufferOverflow = BufferOverflow.DROP_LATEST)

    private suspend fun navigate(appNavigation: AppNavigation) {
        buffer.emit(appNavigation)
    }

    override suspend fun navigateTo(directions: NavDirections) = navigate {
        this.navigate(directions)
    }

    override suspend fun navigateUp() = navigate {
        this.navigateUp()
    }

    override suspend fun popBackStack() = navigate {
        this.popBackStack()
    }
}