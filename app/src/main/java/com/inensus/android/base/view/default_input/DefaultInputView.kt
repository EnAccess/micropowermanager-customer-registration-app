package com.inensus.android.base.view.default_input

import android.content.Context
import android.text.Editable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.TextView
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatImageView
import com.inensus.android.R
import com.inensus.android.base.view.base_input.BaseInputView
import com.inensus.android.base.view.base_input.BorderView
import com.inensus.android.base.view.base_input.ErrorState
import com.inensus.android.extensions.afterTextChanged
import kotlinx.android.synthetic.main.view_default_input.view.*

open class DefaultInputView(context: Context, attrs: AttributeSet) : BaseInputView(context, attrs) {

    var afterTextChanged: ((editable: Editable) -> Unit)? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.view_default_input, this, true)
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

    override fun getTitleView(): TextView = titleText

    override fun getMainTextView(): AppCompatEditText = editText

    override fun getIcon(): AppCompatImageView = icon

    override fun getBorderView(): BorderView = border

    fun setText(text: String) {
        editText.setText(text)
    }

    fun getText() = editText.text.toString()

    open fun setupView() {
        setOnClickListener {
            if (isEnabled) editText.requestFocus()
        }

        editText.setOnFocusChangeListener { _, focus -> onFocusChanged(focus) }

        editText.afterTextChanged {
            afterTextChanged?.invoke(it)
            setErrorState(false)
        }
    }
}