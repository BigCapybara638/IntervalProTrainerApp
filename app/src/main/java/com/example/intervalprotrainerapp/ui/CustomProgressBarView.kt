package com.example.intervalprotrainerapp.ui

import com.example.intervalprotrainerapp.R
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat

enum class CustomProgressBarColors {
    RED, PURPLE, BLUE, SKY, ORANGE, GREEN, YELLOW, LITE_GREEN
}

class CustomProgressBarView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : View(context, attrs, defStyleAttr) {

    private var strWidth = 40f
    private var countShares = 60
    private var count = 1

    private val backgroundColor = ContextCompat.getColor(context, R.color.progress0)
    private var foregroundColor = ContextCompat.getColor(context, R.color.progress1)

    private val paintArg = Paint().apply {
        color = Color.RED
        style = Paint.Style.STROKE
        strokeWidth = strWidth
        strokeCap = Paint.Cap.ROUND
        isAntiAlias = true
    }



    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = width

        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val centerX = width / 2f
        val centerY = height / 2f

        strWidth = width * 0.2f

        val radios = minOf(centerX, centerY) * 0.9f

        paintArg.color = backgroundColor
        canvas.drawArc(
            width.toFloat() - width.toFloat() * 0.9f,
            height.toFloat() - height.toFloat() * 0.9f,
            width.toFloat() * 0.9f, height.toFloat() * 0.9f,
            0f,
            360f,
            false,
            paintArg)

        paintArg.color = foregroundColor
        canvas.drawArc(
            width.toFloat() - width.toFloat() * 0.9f,
            height.toFloat() - height.toFloat() * 0.9f,
            width.toFloat() * 0.9f, height.toFloat() * 0.9f,
            -90f,
            (360f / countShares.toFloat() * count.toFloat()),
            false,
            paintArg)
    }

    fun updateCountShares(shares: Int) {
        countShares = shares
    }

    fun updateProgress() {
        count++
        invalidate()
    }

    fun chooseColor(color: CustomProgressBarColors) {
        foregroundColor = when(color) {
            CustomProgressBarColors.RED -> ContextCompat.getColor(context, R.color.progress1)
            CustomProgressBarColors.PURPLE -> ContextCompat.getColor(context, R.color.progress2)
            CustomProgressBarColors.BLUE -> ContextCompat.getColor(context, R.color.progress3)
            CustomProgressBarColors.SKY -> ContextCompat.getColor(context, R.color.progress4)
            CustomProgressBarColors.ORANGE -> ContextCompat.getColor(context, R.color.progress5)
            CustomProgressBarColors.GREEN -> ContextCompat.getColor(context, R.color.progress6)
            CustomProgressBarColors.YELLOW -> ContextCompat.getColor(context, R.color.progress7)
            CustomProgressBarColors.LITE_GREEN -> ContextCompat.getColor(context, R.color.progress8)
        }
    }

//    fun updateStrokeWidth() {
//        count++
//        invalidate()
//    }

}