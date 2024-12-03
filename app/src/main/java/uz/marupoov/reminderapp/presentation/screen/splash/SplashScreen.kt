package uz.marupoov.reminderapp.presentation.screen.splash

import android.Manifest
import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.permissionx.guolindev.PermissionX
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import uz.marupoov.reminderapp.R

@AndroidEntryPoint
@SuppressLint("CustomSplashScreen")
class SplashScreen : Fragment(R.layout.screen_splash) {
    private var isResume = false
    private val viewModel by viewModels<SplashViewModel>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            delay(800)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                PermissionX
                    .init(this@SplashScreen)
                    .permissions(Manifest.permission.POST_NOTIFICATIONS)
                    .request { isPermission, _, _ ->
                        if (isPermission) {
                            viewModel.openSplash()
                        } else {
                            requireActivity().finish()
                        }
                    }
            } else {
                viewModel.openSplash()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (isResume) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                PermissionX
                    .init(this@SplashScreen)
                    .permissions(Manifest.permission.POST_NOTIFICATIONS)
                    .request { isPermission, _, _ ->
                        if (isPermission) {
                            viewModel.openSplash()
                        } else {
                            requireActivity().finish()
                        }
                    }
            } else {
                viewModel.openSplash()
            }
        }
    }

    override fun onStop() {
        super.onStop()
        isResume = true
    }
}