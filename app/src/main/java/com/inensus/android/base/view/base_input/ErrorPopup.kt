package com.inensus.android.base.view.base_input

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.MotionEvent
import android.widget.PopupWindow
import com.inensus.android.databinding.ErrorPopupLayoutBinding

class ErrorPopup(context: Context) : PopupWindow() {
    private lateinit var binding: ErrorPopupLayoutBinding

    init {
        isOutsideTouchable = true
        binding = ErrorPopupLayoutBinding.inflate(LayoutInflater.from(context))
        contentView = binding.root
        setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setTouchDismissListener()
    }

    private fun setTouchDismissListener() {
        setTouchInterceptor { view, event ->
            if (event != null && event.action == MotionEvent.ACTION_UP) {
                dismiss()
                view.performClick()
            }
            false
        }
    }

    fun setErrorMessage(message: String) {
        binding.errorMessageTextView.text = message
    }
}
