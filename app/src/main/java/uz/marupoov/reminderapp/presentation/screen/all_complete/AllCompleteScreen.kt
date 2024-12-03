package uz.marupoov.reminderapp.presentation.screen.all_complete

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import uz.marupoov.reminderapp.R
import uz.marupoov.reminderapp.databinding.ScreenAllCompleteBinding
import uz.marupoov.reminderapp.presentation.adapter.MainTaskAdapter
import uz.marupoov.reminderapp.presentation.dialog.DeleteDialog

@AndroidEntryPoint
class AllCompleteScreen : Fragment(R.layout.screen_all_complete) {

    private val binding by viewBinding(ScreenAllCompleteBinding::bind)
    private val viewModel by viewModels<AllCompleteViewModel>()
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
        adapter.setOnCompleteChange {
            if (System.currentTimeMillis() - time >= 500) {
                time = System.currentTimeMillis()
                viewModel.onCompleteChangeTask(it)
            }
        }
        val dialog = DeleteDialog()
        adapter.setOnClickDelete {
            if (System.currentTimeMillis() - time >= 500) {
                time = System.currentTimeMillis()
                dialog.setOnClickYes {
                    viewModel.onClickDelete(it)
                    dialog.dismiss()
                }
                dialog.show(parentFragmentManager, dialog.toString())
            }
        }
        viewModel.requestAllCompleteTask()
        btnBack.setOnClickListener {
            if (System.currentTimeMillis() - time >= 500) {
                time = System.currentTimeMillis()
                viewModel.onClickBack()
            }
        }
    }

    private fun initFlow() = binding.apply {
        viewModel.allCompleteTask
            .onEach {
                adapter.submitList(it)
                emptyState.isVisible = it.isEmpty()
            }.flowWithLifecycle(lifecycle)
            .launchIn(lifecycleScope)
    }
}