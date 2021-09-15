package com.suret.moviesapp.util

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.content.res.ResourcesCompat
import com.suret.moviesapp.R


class CurvedText : View {
    private var radius = 320
    private var centerAngle = -90
    private var textSize = resources.displayMetrics.density * 16
    private var text = ""
    private var textColor = Color.WHITE
    private var fontFamily: Typeface? = null

    private val pathArc = Path()
    private val paintText = Paint(Paint.ANTI_ALIAS_FLAG)
    private var offset = 0f

    constructor(context: Context) : super(context)

    constructor (context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        val typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.CurvedTextStyle)
        radius = typedArray.getDimensionPixelSize(R.styleable.CurvedTextStyle_radius, radius)
        centerAngle = typedArray.getInteger(R.styleable.CurvedTextStyle_center_angle, centerAngle)
        textSize = typedArray.getDimensionPixelSize(R.styleable.CurvedTextStyle_text_size, textSize.toInt())
            .toFloat()
        val text = typedArray.getString(R.styleable.CurvedTextStyle_text)
        if (text != null) this.text = text
        val colorRes = typedArray.getColor(R.styleable.CurvedTextStyle_text_color, Color.WHITE)
        if (colorRes != -1) textColor = colorRes
        val fontRes = typedArray.getResourceId(R.styleable.CurvedTextStyle_font_family, -1)
        if (fontRes != -1) fontFamily = ResourcesCompat.getFont(context!!, fontRes)
        typedArray.recycle()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        offset = textSize * 0.75f
        val lp = layoutParams
        lp.width = if (radius > 0) (radius * 2 + offset * 2).toInt() else 0
        lp.height = if (radius > 0) (radius * 2 + offset * 2).toInt() else 0
        requestLayout()

        //Text color
        paintText.color = textColor
        paintText.typeface = fontFamily
        paintText.textSize = textSize
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        val textWidth = paintText.measureText(text)
        val circumference = (2 * Math.PI * radius).toInt()
        val textAngle = (textWidth * 360 / circumference).toInt()
        val startAngle = centerAngle - textAngle / 2
        val oval = RectF(offset, offset, radius * 2 + offset, radius * 2 + offset)
        pathArc.addArc(oval, startAngle.toFloat(), 350f)
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawTextOnPath(text, pathArc, 0f, 0f, paintText)
    }

    fun setText(text: String?) {
        this.text = text!!
        invalidate()
    }

    fun setRadius(radius: Int) {
        this.radius = radius
        invalidate()
    }

    fun setCenterAngle(centerAngle: Int) {
        this.centerAngle = centerAngle
        invalidate()
    }

    fun setTextColor(textColor: Int) {
        this.textColor = textColor
        invalidate()
    }

    fun setTextSize(textSize: Int) {
        this.textSize = textSize.toFloat()
        invalidate()
    }

    fun setFontFamily(fontFamily: Typeface?) {
        this.fontFamily = fontFamily
        invalidate()
    }

}