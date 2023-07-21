package com.inensus.android.base.view.base_input

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.MotionEvent
import android.widget.PopupWindow
import com.inensus.android.R
import kotlinx.android.synthetic.main.error_popup_layout.view.*

class ErrorPopup(context: Context) : PopupWindow() {

    init {
        isOutsideTouchable = true
        contentView = LayoutInflater.from(context).inflate(R.layout.error_popup_layout, null)
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
        contentView.errorMessageTextView.text = message
    }
}