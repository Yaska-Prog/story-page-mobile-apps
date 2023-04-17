package com.bangkit.mystoryapps.UI.CustomView

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.bangkit.mystoryapps.R

class PasswordEditText: AppCompatEditText, View.OnTouchListener {
    private lateinit var clearButtonImage: Drawable

    private fun init(){
        clearButtonImage = ContextCompat.getDrawable(context, R.drawable.ic_baseline_clear_24) as Drawable
        setOnTouchListener(this)
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Do nothing.
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.toString().isNotEmpty()) {
                    showClearButton()
                    if(!s.toString().isEmpty() && s.toString().length < 8){
                        error = "Password minimum is 8 characters"
                    }
                } else hideClearButton()
            }

            override fun afterTextChanged(s: Editable) {
                // Do nothing.
            }
        })
    }
    private fun showClearButton() {
        setButtonDrawables(endOfTheText = clearButtonImage)
    }
    private fun hideClearButton() {
        setButtonDrawables()
    }

    private fun setButtonDrawables(
        startOfTheText: Drawable = ContextCompat.getDrawable(context, R.drawable.ic_baseline_lock_24) as Drawable,
        topOfTheText:Drawable? = null,
        endOfTheText:Drawable? = null,
        bottomOfTheText: Drawable? = null
    ){
        setCompoundDrawablesWithIntrinsicBounds(
            startOfTheText,
            topOfTheText,
            endOfTheText,
            bottomOfTheText
        )
    }

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    override fun onTouch(p0: View?, event: MotionEvent): Boolean {
        if (compoundDrawables[2] != null) {
            val clearButtonStart: Float
            val clearButtonEnd: Float
            var isClearButtonClicked = false

            if (layoutDirection == View.LAYOUT_DIRECTION_RTL) {
                clearButtonEnd = (clearButtonImage.intrinsicWidth + paddingStart).toFloat()
                when {
                    event.x < clearButtonEnd -> isClearButtonClicked = true
                }
            } else {
                clearButtonStart = (width - paddingEnd - clearButtonImage.intrinsicWidth).toFloat()
                when {
                    event.x > clearButtonStart -> isClearButtonClicked = true
                }
            }
            if (isClearButtonClicked) {
                when (event.action) {
                    MotionEvent.ACTION_UP -> {
                        clearButtonImage = ContextCompat.getDrawable(context, R.drawable.ic_baseline_clear_24) as Drawable
                        when {
                            text != null -> text?.clear()
                        }
                        hideClearButton()
                        return true
                    }
                    MotionEvent.ACTION_DOWN -> {
                        clearButtonImage = ContextCompat.getDrawable(context, R.drawable.ic_baseline_clear_24) as Drawable
                        showClearButton()
                        return true
                    }
                    else -> return false
                }
            } else return false
        }
        return false
    }
}