package com.inensus.android.base.view.base_input

import com.inensus.android.R

interface ErrorState {
    fun setErrorState(isError: Boolean)

    companion object {
        val STATE_ERROR = intArrayOf(R.attr.state_error)
    }
}
