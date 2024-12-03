package uz.marupoov.reminderapp.presentation.screen.add_task

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import dagger.hilt.android.AndroidEntryPoint
import uz.marupoov.reminderapp.R
import uz.marupoov.reminderapp.databinding.ScreenAddTaskBinding
import uz.marupoov.reminderapp.presentation.dialog.ExitDialog
import uz.marupoov.reminderapp.presentation.worker.MyWorker
import uz.marupoov.reminderapp.utils.myLogger
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.UUID
import java.util.concurrent.TimeUnit


@AndroidEntryPoint
class AddTaskScreen : Fragment(R.layout.screen_add_task) {
    private val binding by viewBinding(ScreenAddTaskBinding::bind)
    private val viewModel by viewModels<AddTaskViewModel>()
    private var time = 0L
    private var dateTask = 0L
    private var timeTask = 0L
    private val formatterDate = SimpleDateFormat("dd.MM.yyyy", Locale.ROOT)
    private val formatterTime = SimpleDateFormat("HH:mm", Locale.ROOT)
    private val allFormatter = SimpleDateFormat("dd.MM.yyyy/HH:mm", Locale.ROOT)
    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            exitDialogShow()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initFlow()
        requireActivity().onBackPressedDispatcher.addCallback(onBackPressedCallback)
    }

    private fun initView() = binding.apply {
        btnBack.setOnClickListener {
            if (System.currentTimeMillis() - time >= 500) {
                time = System.currentTimeMillis()
                exitDialogShow()
            }
        }
        btnDate.setOnClickListener {
            if (System.currentTimeMillis() - time >= 500) {
                time = System.currentTimeMillis()
                hideInputMode()
                showDataPicker()
            }
        }
        btnTime.setOnClickListener {
            if (System.currentTimeMillis() - time >= 500) {
                time = System.currentTimeMillis()
                hideInputMode()
                showTimePicker()
            }
        }
        btnAdd.setOnClickListener {
            if (System.currentTimeMillis() - time >= 500) {
                time = System.currentTimeMillis()
                addTaskWorker()
            }
        }
        btnAdd.isEnabled = false
        etTask.addTextChangedListener {
            btnAdd.isEnabled =
                !(etTask.text.isNullOrEmpty() || btnDate.text == "Date" || btnTime.text == "Time")
        }
    }

    private fun initFlow() = binding.apply {

    }


    @SuppressLint("SetTextI18n")
    private fun showDataPicker() {
        val constraintsBuilder = CalendarConstraints.Builder()
        constraintsBuilder.setValidator(DateValidatorPointForward.now())
        val builder = MaterialDatePicker.Builder.datePicker()
        builder.setCalendarConstraints(constraintsBuilder.build())
        val datePicker = builder.build()
        datePicker.show(childFragmentManager, datePicker.toString())
        datePicker.addOnPositiveButtonClickListener {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = it
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            binding.apply {
                btnDate.text = formatterDate.format(calendar.time)
                dateTask = calendar.time.time
                btnAdd.isEnabled =
                    !(etTask.text.isNullOrEmpty() || btnDate.text == "Date" || btnTime.text == "Time")
                if (timeTask != 0L) {
                    val nowTime =
                        allFormatter.parse(allFormatter.format(System.currentTimeMillis()))?.time
                            ?: 0L
                    val chooseTime =
                        allFormatter.parse("${btnDate.text}/${btnTime.text}")?.time ?: 0L
                    if (chooseTime <= nowTime) {
                        btnTime.text = "Time"
                        btnAdd.isEnabled = false
                        Toast.makeText(requireContext(), "The time was wrong !", Toast.LENGTH_SHORT)
                            .show()
                        showDataPicker()
                    }
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun showTimePicker() {
        val cal = Calendar.getInstance()
        val timePicker = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_24H)
            .setHour(cal.get(Calendar.HOUR_OF_DAY))
            .setMinute(cal.get(Calendar.MINUTE))
            .setInputMode(MaterialTimePicker.INPUT_MODE_CLOCK)
            .build()
        timePicker.show(parentFragmentManager, timePicker.toString())
        timePicker.addOnPositiveButtonClickListener {
            cal.time = Date(System.currentTimeMillis())
            cal.set(Calendar.HOUR_OF_DAY, timePicker.hour)
            cal.set(Calendar.MINUTE, timePicker.minute)
            cal.set(Calendar.SECOND, 5)
            binding.apply {
                btnTime.text = formatterTime.format(cal.time)
                timeTask = cal.time.time
                btnAdd.isEnabled =
                    !(etTask.text.isNullOrEmpty() || btnDate.text == "Date" || btnTime.text == "Time")
                if (dateTask != 0L) {
                    val nowTime =
                        allFormatter.parse(allFormatter.format(System.currentTimeMillis()))?.time
                            ?: 0L
                    val chooseTime =
                        allFormatter.parse("${btnDate.text}/${btnTime.text}")?.time ?: 0L
                    if (chooseTime <= nowTime) {
                        btnTime.text = "Time"
                        btnAdd.isEnabled = false
                        Toast.makeText(requireContext(), "The time was wrong !", Toast.LENGTH_SHORT)
                            .show()
                        showTimePicker()
                    }
                }
            }
        }
    }

    private fun hideInputMode() {
        val inputMethodManager =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    private fun exitDialogShow() {
        val exitDialog = ExitDialog()
        exitDialog.setOnClickYes {
            onBackPressedCallback.remove()
            viewModel.onClickBack()
            exitDialog.dismiss()
        }
        exitDialog.show(parentFragmentManager, exitDialog.toString())
    }

    private fun addTaskWorker() {
        val task = binding.etTask.text.toString()
        val uuid = UUID.randomUUID().toString()
        viewModel.onClickAdd(task, dateTask, timeTask, uuid)
        val data = Data.Builder()
            .putString("TASK", task)
            .build()
        val dateStr = formatterDate.format(dateTask)
        val timeStr = formatterTime.format(timeTask)
        val taskTime = allFormatter.parse("$dateStr/$timeStr")
        val nowTime = System.currentTimeMillis()
        myLogger("Task add time = ${allFormatter.format(timeTask)}", "WORK")
        myLogger("dateStr  = $dateStr timeStr = $timeStr", "WORK")
        myLogger("Now add time = ${allFormatter.format(System.currentTimeMillis())}", "WORK")
        myLogger("delay = ${(taskTime?.time ?: nowTime) - nowTime}", "WORK")
        val notificationWork = OneTimeWorkRequestBuilder<MyWorker>()
            .addTag(uuid)
            .setInitialDelay(
                (taskTime?.time ?: nowTime) - nowTime,
                TimeUnit.MILLISECONDS
            )
            .setInputData(data)
            .build()

        WorkManager.getInstance(requireContext()).enqueue(notificationWork)
    }
}