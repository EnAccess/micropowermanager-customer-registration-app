package com.inensus.android.base.view.base_input

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class TitleView(
    context: Context,
    attributeSet: AttributeSet,
) : AppCompatTextView(context, attributeSet),
    ErrorState {
    private var errorState: Boolean = false

    override fun onCreateDrawableState(extraSpace: Int): IntArray =
        if (errorState) {
            val drawableState = super.onCreateDrawableState(extraSpace + 1)
            mergeDrawableStates(drawableState, ErrorState.STATE_ERROR)
            drawableState
        } else {
            super.onCreateDrawableState(extraSpace)
        }

    override fun setErrorState(isError: Boolean) {
        errorState = isError
        refreshDrawableState()
    }
}
