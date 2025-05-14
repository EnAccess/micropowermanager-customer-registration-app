package com.inensus.android.base.view.password_input

import android.content.Context
import android.text.InputType
import android.util.AttributeSet
import androidx.core.content.res.ResourcesCompat
import com.inensus.android.R
import com.inensus.android.base.view.default_input.DefaultInputView

class PasswordInputView(
    context: Context,
    attrs: AttributeSet,
) : DefaultInputView(context, attrs) {
    private var passwordState: Boolean = true

    override fun setupView() {
        super.setupView()
        setInputType(InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD)
        setTypeface()
        setupListeners()
    }

    private fun setInputType(inputTypes: Int) {
        binding.editText.inputType = inputTypes
    }

    private fun setTypeface() {
        binding.editText.typeface = ResourcesCompat.getFont(context, R.font.semi_bold)
    }

    private fun setupListeners() {
        binding.icon.setOnClickListener {
            passwordState = !passwordState

            if (passwordState) {
                setInputType(InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD)
                binding.icon.isActivated = false
            } else {
                setInputType(InputType.TYPE_CLASS_TEXT)
                binding.icon.isActivated = true
            }

            setTypeface()
            setSelection()
        }
    }

    private fun setSelection() {
        binding.editText.setSelection(binding.editText.text?.length ?: 0)
    }
}
