package uz.marupoov.reminderapp.presentation.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import by.kirich1409.viewbindingdelegate.viewBinding
import uz.marupoov.reminderapp.R
import uz.marupoov.reminderapp.databinding.DialogDeleteBinding

class DeleteDialog : DialogFragment(R.layout.dialog_delete) {
    private val binding by viewBinding(DialogDeleteBinding::bind)
    private var onClickYes: (() -> Unit)? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnYes.setOnClickListener {
            onClickYes?.invoke()
        }
        binding.btnCancel.setOnClickListener {
            dismiss()
        }
    }

    fun setOnClickYes(onClickYes: (() -> Unit)) {
        this.onClickYes = onClickYes
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }
}