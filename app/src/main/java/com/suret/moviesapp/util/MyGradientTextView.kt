package com.suret.moviesapp.util

import android.content.Context
import android.graphics.LinearGradient
import android.graphics.Shader
import android.util.AttributeSet
import androidx.core.content.ContextCompat

class MyGradientTextView : androidx.appcompat.widget.AppCompatTextView {

    var firstColor: Int = 0
    var secondColor: Int = 0


    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    fun setColor(firstColor: Int, secondColor: Int) {
        this.firstColor = firstColor
        this.secondColor = secondColor
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

        if (changed) {
            paint.shader = LinearGradient(
                0f, 0f, width.toFloat(), height.toFloat(),
                ContextCompat.getColor(context, firstColor),
                ContextCompat.getColor(context, secondColor),
                Shader.TileMode.CLAMP
            )

        }
    }

}