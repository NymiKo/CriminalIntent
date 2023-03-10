package com.easyprog.android.criminalintent.utils

import android.text.Editable
import android.text.TextWatcher

class SimpleTextWatcher(val block: (s: CharSequence?) -> Unit): TextWatcher {
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        block(s)
    }

    override fun afterTextChanged(s: Editable?) {

    }
}