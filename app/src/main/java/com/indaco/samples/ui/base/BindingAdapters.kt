package com.indaco.samples.ui.base

import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputLayout

object BindingAdapters {

    @JvmStatic
    @BindingAdapter("android:onEditorAction")
    fun bindEditorAction(view: TextView, func: () -> Unit) {
        view.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_NEXT)
                func.invoke()
            false
        }
    }

    @JvmStatic
    @BindingAdapter("app:errorText")
    fun setErrorText(view: TextInputLayout, errorMessage: String) {
        if (errorMessage.isEmpty())
            view.error = null
        else
            view.error = errorMessage;
    }
}