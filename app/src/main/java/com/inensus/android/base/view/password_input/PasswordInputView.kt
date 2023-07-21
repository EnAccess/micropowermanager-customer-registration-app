package com.inensus.android.base.view.password_input

import android.content.Context
import android.text.InputType
import android.util.AttributeSet
import androidx.core.content.res.ResourcesCompat
import com.inensus.android.R
import com.inensus.android.base.view.default_input.DefaultInputView
import kotlinx.android.synthetic.main.view_default_input.view.*

class PasswordInputView(context: Context, attrs: AttributeSet) : DefaultInputView(context, attrs) {
    private var passwordState: Boolean = true

    override fun setupView() {
        super.setupView()
        setInputType(InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD)
        setTypeface()
        setupListeners()
    }

    private fun setInputType(inputTypes: Int) {
        editText.inputType = inputTypes
    }

    private fun setTypeface() {
        editText.typeface = ResourcesCompat.getFont(context, R.font.semi_bold)
    }

    private fun setupListeners() {
        icon.setOnClickListener {
            passwordState = !passwordState

            if (passwordState) {
                setInputType(InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD)
                icon.isActivated = false
            } else {
                setInputType(InputType.TYPE_CLASS_TEXT)
                icon.isActivated = true
            }

            setTypeface()
            setSelection()
        }
    }

    private fun setSelection() {
        editText.setSelection(editText.text?.length ?: 0)
    }
}