package uz.marupoov.reminderapp.presentation.screen.main

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.WorkManager
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import uz.marupoov.reminderapp.R
import uz.marupoov.reminderapp.data.TaskData
import uz.marupoov.reminderapp.databinding.ScreenMainBinding
import uz.marupoov.reminderapp.presentation.adapter.MainTaskAdapter
import uz.marupoov.reminderapp.utils.myLogger
import uz.marupoov.reminderapp.utils.timeChangeFlow

@AndroidEntryPoint
class MainScreen : Fragment(R.layout.screen_main) {
    private val binding by viewBinding(ScreenMainBinding::bind)
    private val viewModel by viewModels<MainViewModel>()
    private val adapter = MainTaskAdapter()
    private var time = 0L
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initFlow()
    }

    private fun initView() = binding.apply {
        rvList.layoutManager = LinearLayoutManager(requireContext())
        rvList.adapter = adapter
        viewModel.requestAllNotCompleteTask()
        btnAdd.setOnClickListener {
            if (System.currentTimeMillis() - time >= 500) {
                time = System.currentTimeMillis()
                viewModel.onClickBtnAdd()
            }
        }
        allComplete.setOnClickListener {
            if (System.currentTimeMillis() - time >= 500) {
                time = System.currentTimeMillis()
                viewModel.onClickAllCompleteBtn()
            }
        }
        adapter.setOnCompleteChange {
            if (System.currentTimeMillis() - time >= 500) {
                time = System.currentTimeMillis()
                viewModel.onCompleteChangeTask(it)
                cancelWork(it)
            }
        }
    }

    private fun initFlow() = binding.apply {
        viewModel.allNotCompleteTask
            .onEach {
                adapter.submitList(it)
                emptyState.isVisible = it.isEmpty()
            }.flowWithLifecycle(lifecycle)
            .launchIn(lifecycleScope)

        timeChangeFlow
            .onEach {
                viewModel.requestAllNotCompleteTask()
            }
            .flowWithLifecycle(lifecycle)
            .launchIn(lifecycleScope)
    }

    private fun cancelWork(taskData: TaskData) {
        myLogger("Cancel worker ${taskData.task}", "WORK")
        WorkManager.getInstance(requireContext()).cancelAllWorkByTag(taskData.uuid)
    }
}