package uz.marupoov.reminderapp.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import uz.marupoov.reminderapp.R
import uz.marupoov.reminderapp.data.TaskData
import uz.marupoov.reminderapp.databinding.ItemTaskBinding
import java.text.SimpleDateFormat
import java.util.Locale

class MainTaskAdapter : ListAdapter<TaskData, MainTaskAdapter.MainTaskHolder>(MainTaskDiffUtil) {

    private var onCompleteChange: ((TaskData) -> Unit)? = null
    private var onClickDelete: ((TaskData) -> Unit)? = null
    private val formatterDate = SimpleDateFormat("dd.MM.yyyy", Locale.ROOT)
    private val formatterTime = SimpleDateFormat("HH:mm", Locale.ROOT)
    private val allFormatter = SimpleDateFormat("dd.MM.yyyy, HH:mm", Locale.ROOT)

    inner class MainTaskHolder(private val binding: ItemTaskBinding) : ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {

            }
            binding.btnDelete.setOnClickListener {
                onClickDelete?.invoke(getItem(absoluteAdapterPosition))
            }
        }

        @SuppressLint("SetTextI18n")
        fun bind() {
            getItem(absoluteAdapterPosition).let {
                binding.task.text = it.task
                binding.date.text =
                    "${formatterDate.format(it.date)}, ${formatterTime.format(it.time)}"
                binding.isComplete.isChecked = it.completeState == 1
                binding.btnDelete.isGone = onClickDelete == null
                if (it.completeState == 0) {
                    val nowTime =
                        allFormatter.parse(allFormatter.format(System.currentTimeMillis()))?.time
                            ?: 0L
                    val taskTime = allFormatter.parse(binding.date.text.toString())?.time ?: 0L
                    if (taskTime <= nowTime) {
                        binding.root.setBackgroundResource(R.drawable.bg_item_task_no_completed)
                    } else {
                        binding.root.setBackgroundResource(R.drawable.bg_item_task)
                    }
                } else {
                    binding.root.setBackgroundResource(R.drawable.bg_item_task_completed)
                }
                binding.isComplete.setOnCheckedChangeListener { _, isChecked ->
                    onCompleteChange?.invoke(getItem(absoluteAdapterPosition).copy(completeState = if (isChecked) 1 else 0))
                }
            }
        }
    }

    object MainTaskDiffUtil : DiffUtil.ItemCallback<TaskData>() {
        override fun areItemsTheSame(oldItem: TaskData, newItem: TaskData): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: TaskData, newItem: TaskData): Boolean {
            return false
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainTaskHolder {
        return MainTaskHolder(
            ItemTaskBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MainTaskHolder, position: Int) {
        holder.bind()
    }

    fun setOnCompleteChange(onCompleteChange: ((TaskData) -> Unit)) {
        this.onCompleteChange = onCompleteChange
    }

    fun setOnClickDelete(onClickDelete: ((TaskData) -> Unit)) {
        this.onClickDelete = onClickDelete
    }
}