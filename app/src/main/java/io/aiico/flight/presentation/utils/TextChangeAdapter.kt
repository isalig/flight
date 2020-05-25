package io.aiico.flight.presentation.utils

import android.text.Editable
import android.text.TextWatcher

abstract class TextChangeAdapter: TextWatcher {

    override fun afterTextChanged(s: Editable?) {
        // do nothing
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        // do nothing
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        // do nothing
    }
}