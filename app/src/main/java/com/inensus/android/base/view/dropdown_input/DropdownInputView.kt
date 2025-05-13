package com.inensus.android.base.view.dropdown_input

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MenuItem
import androidx.appcompat.widget.PopupMenu
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.use
import androidx.core.view.children
import com.inensus.android.R
import com.inensus.android.base.view.base_input.ErrorState
import com.inensus.android.databinding.ViewDropdownInputBinding
import com.inensus.android.extensions.hide

class DropdownInputView(context: Context, attributeSet: AttributeSet) :
    ConstraintLayout(context, attributeSet), ErrorState {
    private lateinit var binding: ViewDropdownInputBinding
    var onValueChanged: ((String) -> Unit)? = null
    private var errorState: Boolean = false
    private lateinit var popupMenu: PopupMenu

    var value: String
        get() = binding.valueText.text.toString()
        set(value) {
            binding.valueText.text = value
            binding.placeholderText.hide()
            setErrorState(false)

            onValueChanged?.invoke(value)
        }

    init {
        binding = ViewDropdownInputBinding.inflate(LayoutInflater.from(context), this)
        initializeAttributes(context, attributeSet)
    }

    override fun onCreateDrawableState(extraSpace: Int): IntArray =
        if (errorState) {
            val drawableState = super.onCreateDrawableState(extraSpace + 1)
            mergeDrawableStates(drawableState, ErrorState.STATE_ERROR)
            drawableState
        } else {
            super.onCreateDrawableState(extraSpace)
        }

    private fun initializeAttributes(context: Context, attrs: AttributeSet) {
        context.obtainStyledAttributes(attrs, R.styleable.DropdownInputView).use {
            val title = it.getString(R.styleable.DropdownInputView_dropdown_input_title)

            setupView(title)
        }
    }

    fun bindData(data: List<String>, firstAsDefault: Boolean = true) {
        popupMenu.menu.clear()

        data.forEach {
            popupMenu.menu.add(it)
        }

        if (data.isNotEmpty()) {
            if (value.isEmpty() && firstAsDefault) value = data.first()
        } else {
            value = ""
        }
    }

    private fun setupView(title: String?) {
        binding.titleText.text = title

        popupMenu = PopupMenu(context, this).apply {
            setOnMenuItemClickListener { item: MenuItem? ->
                value = item?.title.toString()

                true
            }
        }

        setOnClickListener {
            popupMenu.dismiss()
            popupMenu.show()
        }
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
}
