package com.inensus.android.base.view

import android.content.Context
import android.text.InputFilter
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.inputmethod.EditorInfo
import android.widget.FrameLayout
import com.inensus.android.R
import com.inensus.android.databinding.ViewTextInputBinding

class TextInputView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : FrameLayout(context, attrs, defStyle) {

    private lateinit var binding: ViewTextInputBinding

    var text: String?
        get() = binding.editText.text.toString()
        set(value) = binding.editText.setText(value)

    private fun addInputFilter(inputFilter: InputFilter) {
        binding.editText.filters = binding.editText.filters.plus(inputFilter)
    }

    init {
        binding = ViewTextInputBinding.inflate(LayoutInflater.from(context), this, true)
        extractAttributes(context, attrs)
    }

    private fun extractAttributes(context: Context, attrs: AttributeSet?) {
        context.theme.obtainStyledAttributes(attrs, R.styleable.TextInputView, 0, 0).apply {
            try {
                binding.inputLayout.hint = getString(R.styleable.TextInputView_android_hint)
                binding.editText.inputType =
                    getInt(R.styleable.TextInputView_android_inputType, EditorInfo.TYPE_CLASS_TEXT)
                binding.editText.imeOptions = getInt(
                    R.styleable.TextInputView_android_imeOptions,
                    EditorInfo.IME_ACTION_UNSPECIFIED
                )
                val maxLength = getInt(R.styleable.TextInputView_android_maxLength, -1)
                if (maxLength != -1) {
                    addInputFilter(InputFilter.LengthFilter(maxLength))
                }
            } finally {
                recycle()
            }
        }
    }
}
