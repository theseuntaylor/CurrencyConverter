package com.theseuntaylor.currencyconverter.utils

import android.content.Context
import android.util.AttributeSet

class CircularButtonProgress(
    context: Context?,
    attrs: AttributeSet?,
    defStyleAttr: Int,
) : androidx.appcompat.widget.AppCompatButton(context!!, attrs, defStyleAttr) {

    enum class State {
        IN_PROGRESS, DONE
    }

    class LoadingButton(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
    ) : androidx.appcompat.widget.AppCompatButton(context!!, attrs, defStyleAttr)
}