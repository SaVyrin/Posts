package ru.surf.gallery.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager


fun String.formatPhone(): String {
    var phoneMask = "# # (###) ### ## ##"
    this.toCharArray()
        .forEach {
            phoneMask = phoneMask.replaceFirst('#', it)
        }
    return phoneMask
}

fun String.withQuotation(): String {
    return "\"${this}\""
}

fun View.showKeyboard() {
    val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.showSoftInput(this, InputMethodManager.SHOW_FORCED)
}

fun View.hideKeyboard() {
    val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(this.windowToken, 0)
}