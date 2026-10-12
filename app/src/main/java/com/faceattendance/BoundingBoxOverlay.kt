package com.faceattendance;

import android.content.Context
import android.graphics.Camera
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.util.AttributeSet
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import androidx.camera.core.CameraSelector
import androidx.core.graphics.toRectF
class BoundingBoxOverlay(
    context: Context,
    attributeSet: AttributeSet
) : View(context, attributeSet) {

    var areDimsInit = false
    var frameHeight = 0
    var frameWidth = 0

    var cameraFacing: Int = CameraSelector.LENS_FACING_FRONT
    var faceBoundingBoxes: ArrayList<Prediction>? = null
    var drawMaskLabel = true

    private var output2OverlayTransform: Matrix = Matrix()

    private val boxPaint = Paint().apply {
        color = Color.parseColor("#4D90caf9")
        style = Paint.Style.FILL
    }

    private val textPaint = Paint().apply {
        strokeWidth = 2.0f
        textSize = 32f
        color = Color.WHITE
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (faceBoundingBoxes == null) return

        if (!areDimsInit) {
            val viewWidth = width.toFloat()
            val viewHeight = height.toFloat()

            val xFactor = viewWidth / frameWidth.toFloat()
            val yFactor = viewHeight / frameHeight.toFloat()

            output2OverlayTransform.preScale(xFactor, yFactor)

            if (cameraFacing == CameraSelector.LENS_FACING_FRONT) {
                output2OverlayTransform.postScale(-1f, 1f, viewWidth / 2f, viewHeight / 2f)
            }

            areDimsInit = true
        }

        for (face in faceBoundingBoxes!!) {
            val boundingBox = face.bbox.toRectF()
            output2OverlayTransform.mapRect(boundingBox)

            canvas.drawRoundRect(boundingBox, 16f, 16f, boxPaint)
            canvas.drawText(face.label, boundingBox.centerX(), boundingBox.centerY(), textPaint)

            if (drawMaskLabel) {
                canvas.drawText(
                    face.maskLabel,
                    boundingBox.centerX(),
                    boundingBox.centerY() + 32,
                    textPaint
                )
            }
        }
    }
}
