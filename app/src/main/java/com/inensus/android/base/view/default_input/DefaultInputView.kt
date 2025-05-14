package com.inensus.android.base.view.default_input

import android.content.Context
import android.text.Editable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.TextView
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatImageView
import com.inensus.android.base.view.base_input.BaseInputView
import com.inensus.android.base.view.base_input.BorderView
import com.inensus.android.base.view.base_input.ErrorState
import com.inensus.android.databinding.ViewDefaultInputBinding
import com.inensus.android.extensions.afterTextChanged

open class DefaultInputView(
    context: Context,
    attrs: AttributeSet,
) : BaseInputView(context, attrs) {
    protected lateinit var binding: ViewDefaultInputBinding

    var afterTextChanged: ((editable: Editable) -> Unit)? = null

    init {
        binding = ViewDefaultInputBinding.inflate(LayoutInflater.from(context), this)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        setupView()
    }

    override fun onCreateDrawableState(extraSpace: Int): IntArray =
        if (errorState) {
            val drawableState = super.onCreateDrawableState(extraSpace + 1)
            mergeDrawableStates(drawableState, ErrorState.STATE_ERROR)
            drawableState
        } else {
            super.onCreateDrawableState(extraSpace)
        }

    override fun getTitleView(): TextView = binding.titleText

    override fun getMainTextView(): AppCompatEditText = binding.editText

    override fun getIcon(): AppCompatImageView = binding.icon

    override fun getBorderView(): BorderView = binding.border

    fun setText(text: String) {
        binding.editText.setText(text)
    }

    fun getText() = binding.editText.text.toString()

    open fun setupView() {
        setOnClickListener {
            if (isEnabled) binding.editText.requestFocus()
        }

        binding.editText.setOnFocusChangeListener { _, focus -> onFocusChanged(focus) }

        binding.editText.afterTextChanged {
            afterTextChanged?.invoke(it)
            setErrorState(false)
        }
    }
}
