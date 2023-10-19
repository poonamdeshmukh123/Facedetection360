package com.example.image360task

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect

import com.google.mlkit.vision.face.Face

class Graphic(
    overlay: GraphicOverlay,
    private val face: Face,
    private val imageRect: Rect
) : GraphicOverlay.Graphic(overlay) {


             val boxPaint:Paint = Paint().apply {
            color = Color.RED
            style = Paint.Style.STROKE
            strokeWidth = BOX_STROKE_WIDTH
        }


    override fun draw(canvas: Canvas?) {
        val rect = calculateRect(
            imageRect.height().toFloat(),
            imageRect.width().toFloat(),
            face.boundingBox
        )
        canvas?.drawRect(rect, boxPaint)
    }

    companion object {
        private const val BOX_STROKE_WIDTH = 6.0f
    }

}