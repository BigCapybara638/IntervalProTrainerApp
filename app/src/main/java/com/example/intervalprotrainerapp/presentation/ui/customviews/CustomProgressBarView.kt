package com.example.intervalprotrainerapp.presentation.ui.customviews

import com.example.intervalprotrainerapp.R
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.example.intervalprotrainerapp.domain.models.TimerTime

enum class CustomProgressBarColors {
    RED, PURPLE, BLUE, SKY, ORANGE, GREEN, YELLOW, LITE_GREEN
}


class CustomProgressBarView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : View(context, attrs, defStyleAttr) {

    private var strWidth = 60f
    private var countShares = 60
    private var count = 1

    private var textToDraw = "00:00"

    private val backgroundColor = ContextCompat.getColor(context, R.color.progress0)
    private var foregroundColor = ContextCompat.getColor(context, R.color.progress1)

    private val paintArg = Paint().apply {
        color = Color.RED
        style = Paint.Style.STROKE
        strokeWidth = strWidth
        strokeCap = Paint.Cap.ROUND
        isAntiAlias = true
    }

    private val paintText = Paint().apply {
        color = Color.WHITE
        textSize = 180f
        textAlign = Paint.Align.CENTER
    }



    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        val width = minOf(widthSize, heightSize)
        val height = minOf(widthSize, heightSize)

        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

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

        val textBounds = Rect()
        paintText.textSize = width / 5f
        paintText.getTextBounds(textToDraw, 0, textToDraw.length, textBounds)
        val baseline = height / 2f - (textBounds.top + textBounds.bottom) / 2

        canvas.drawText(textToDraw, width / 2f, baseline, paintText)

    }

    fun updateCountShares(shares: Int) {
        countShares = shares + 1
    }

    fun updateProgress() {
        count++
        invalidate()
    }

    fun setProgress(progress: Int) {
        count = progress + 1
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

    fun setTimer(time: Int) {
        textToDraw = TimerTime(time).toString()
        invalidate()
    }

//    fun updateStrokeWidth() {
//        count++
//        invalidate()
//    }

}