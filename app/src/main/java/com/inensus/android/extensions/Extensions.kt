package com.inensus.android.extensions

import android.app.Activity
import android.graphics.Typeface
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.view.children
import com.amulyakhare.textdrawable.TextDrawable
import com.google.gson.Gson
import com.inensus.android.base.model.Error
import com.inensus.android.base.model.ServiceError
import com.inensus.android.util.KeyboardUtils
import retrofit2.HttpException
import java.net.SocketTimeoutException

fun EditText.requestFocusWithKeyboard() {
    requestFocus()
    context.let {
        if (it is Activity) {
            KeyboardUtils.showKeyboard(it)
        }
    }
}

fun EditText.afterTextChanged(afterTextChanged: (Editable) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun afterTextChanged(editable: Editable?) {
            editable?.run {
                afterTextChanged.invoke(this)
            }
        }
    })
}

fun ViewGroup.setEnabledWithChildren(isEnabled: Boolean) {
    this.isEnabled = isEnabled
    children.forEach {
        it.isEnabled = isEnabled
        if (it is ViewGroup) {
            it.setEnabledWithChildren(isEnabled)
        }
    }
}

fun View.show() {
    alpha = 1f
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.INVISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun Throwable.toServiceError(): ServiceError {
    return when (this) {
        is HttpException -> parse(response()?.errorBody()?.string())
        is SocketTimeoutException -> ServiceError(Error("Request has been timed out. Please check your connection and try again"))
        else -> ServiceError(Error("An error occurred. Please try again later"))
    }
}

private fun parse(errorBody: String?): ServiceError =
        try {
            Gson().fromJson(errorBody, ServiceError::class.java)
        } catch (e: Throwable) {
            ServiceError(Error("An error occurred. Please try again later"))
        }

fun createInitialsDrawable(name: String, textColor: Int, backgroundColor: Int): TextDrawable = TextDrawable.builder()
        .beginConfig()
        .fontSize(14.toPx())
        .useFont(Typeface.DEFAULT)
        .textColor(textColor)
        .endConfig()
        .buildRound(extractUserInitials(name), backgroundColor)

private fun extractUserInitials(text: String): String {
    val sequence = text.split(" ").take(2).filterNot { it.isEmpty() }.map { it.first().toString() }
    return when {
        sequence.isEmpty() -> ""
        sequence.size == 1 -> sequence[0]
        else -> listOf(sequence[0], sequence.last()).joinToString("")
    }
}