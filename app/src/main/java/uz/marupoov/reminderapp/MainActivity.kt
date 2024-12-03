package uz.marupoov.reminderapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import uz.marupoov.reminderapp.databinding.ActivityMainBinding
import uz.marupoov.reminderapp.navigation.AppNavigationHandler
import uz.marupoov.reminderapp.utils.isVisibleActivity
import uz.marupoov.reminderapp.utils.makeToast
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(R.layout.activity_main) {
    private val binding by viewBinding(ActivityMainBinding::bind)

    @Inject
    lateinit var appNavigationHandler: AppNavigationHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val navHostFragment =
            supportFragmentManager.findFragmentById(binding.main.id) as NavHostFragment
        val navController = navHostFragment.navController

        appNavigationHandler.buffer
            .onEach {
                it.invoke(navController)
            }.launchIn(lifecycleScope)
        isVisibleActivity = true
        makeToast.onEach {
            Snackbar.make(this, binding.root, it, 3000).show()
        }.launchIn(lifecycleScope)
    }

    override fun onResume() {
        super.onResume()
        isVisibleActivity = true
    }

    override fun onStop() {
        super.onStop()
        isVisibleActivity = false
    }
}
//tugadi