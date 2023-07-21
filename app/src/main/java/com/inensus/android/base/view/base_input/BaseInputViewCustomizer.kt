package com.inensus.android.base.view.base_input

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import com.inensus.android.R

class BaseInputViewCustomizer : FocusableState {

    private var hintText: String? = null
    private var titleText: String? = null
    private var editTextText: String? = null

    private var iconResourceId: Int? = null

    private var titleColor: ColorStateList? = null
    private var editTextColor: ColorStateList? = null
    private var hintTextColor: ColorStateList? = null
    private var borderColor: ColorStateList? = null

    private lateinit var view: BaseInputView

    fun initializeAttrs(context: Context, attrs: AttributeSet) {
        context.obtainStyledAttributes(attrs, R.styleable.BaseInputView).apply {
            titleText = getString(R.styleable.BaseInputView_title)
            hintText = getString(R.styleable.BaseInputView_hint)
            editTextText = getString(R.styleable.BaseInputView_text)

            titleColor = getColorStateList(R.styleable.BaseInputView_input_view_title_color)
            editTextColor = getColorStateList(R.styleable.BaseInputView_input_view_text_color)
            hintTextColor = getColorStateList(R.styleable.BaseInputView_hint_color)
            borderColor = getColorStateList(R.styleable.BaseInputView_border_color)

            iconResourceId = getResourceId(R.styleable.BaseInputView_icon, -1)
            if (iconResourceId == -1) iconResourceId = null

            recycle()
        }
    }

    fun setupView(view: BaseInputView) {
        this.view = view
        setupColors()
        setupValues()
    }

    private fun setupValues() {
        with(view) {
            getMainTextView()?.hint = hintText
            getTitleView()?.text = titleText
            editTextText?.let { getMainTextView()?.setText(it) }
            iconResourceId?.let {
                getIcon()?.setImageDrawable(ContextCompat.getDrawable(context, it))
            }
        }
    }

    private fun setupColors() {
        with(view) {
            editTextColor?.let { getMainTextView()?.setTextColor(it) }
            titleColor?.let { getTitleView()?.setTextColor(it) }
            hintTextColor?.let { getMainTextView()?.setHintTextColor(it) }

            borderColor?.let { color ->
                getBorderView()?.let { borderView ->
                    ViewCompat.setBackgroundTintList(borderView, color)
                }
            }
        }
    }

    override fun onFocusChanged(isFocused: Boolean) {
        if (isFocused) showFocused()
        else showUnfocused()
    }

    private fun showUnfocused() {
        with(view) {
            getTitleView()?.isActivated = false
            getMainTextView()?.isActivated = false
            getBorderView()?.isActivated = false
        }
    }

    private fun showFocused() {
        with(view) {
            getTitleView()?.isActivated = true
            getMainTextView()?.isActivated = true
            getBorderView()?.isActivated = true

            setErrorState(false)
        }
    }
}