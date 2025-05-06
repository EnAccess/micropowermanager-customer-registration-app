package com.inensus.android.base.view.base_input

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.children
import com.inensus.android.extensions.requestFocusWithKeyboard
import com.inensus.android.extensions.setEnabledWithChildren
import com.inensus.android.util.Constants.ERROR_POPUP_DELAY_IN_MILLIS

abstract class BaseInputView(
    context: Context,
    attrs: AttributeSet
) : ConstraintLayout(context, attrs), ErrorState, DisableState, FocusableState {

    var errorState: Boolean = false
        private set

    private var errorPopup: ErrorPopup? = null

    private var viewCustomizer: BaseInputViewCustomizer = BaseInputViewCustomizer()

    init {
        viewCustomizer.initializeAttrs(context, attrs)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        setupView()
        viewCustomizer.setupView(this)
    }

    private fun setupView() {
        setOnClickListener {
            getMainTextView()?.let {
                if (it is EditText) {
                    if (!hasCurrentFocus()) {
                        it.setSelection(it.text?.length ?: 0)
                        it.requestFocusWithKeyboard()
                    }
                }
            }
        }
    }

    fun showErrorMessage(message: String) {
        parent.requestChildFocus(this, this)
        if (errorPopup == null) {
            errorPopup = ErrorPopup(context)
        }
        errorPopup?.setErrorMessage(message)

        setErrorState(true)
        showErrorIfNecessary()
        invalidate()
    }

    override fun setErrorState(isError: Boolean) {
        errorState = isError

        children
            .filter { it is ErrorState }
            .forEach {
                (it as ErrorState).setErrorState(isError)
            }

        refreshDrawableState()
    }

    override fun setEnabledState(isEnabled: Boolean) {
        setEnabledWithChildren(isEnabled)
        if (isFocused) onFocusChanged(isFocused)
    }

    override fun onFocusChanged(isFocused: Boolean) {
        viewCustomizer.onFocusChanged(isFocused)
    }

    private fun hasCurrentFocus() = (context as? Activity)?.currentFocus === getMainTextView()

    private fun showErrorIfNecessary() {
        if (errorState) {
            errorPopup?.let {
                it.width = measuredWidth
                it.height = ViewGroup.LayoutParams.WRAP_CONTENT
                postDelayed(
                    { it.showAsDropDown(this, 0, 0, Gravity.BOTTOM) },
                    ERROR_POPUP_DELAY_IN_MILLIS
                )
            }
        }
    }

    abstract fun getTitleView(): TextView?
    abstract fun getMainTextView(): TextView?
    abstract fun getIcon(): AppCompatImageView?
    abstract fun getBorderView(): View?
}
