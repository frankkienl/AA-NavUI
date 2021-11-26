package nl.frankkie.aa_navui.car

import android.graphics.*
import androidx.annotation.IntDef
import androidx.car.app.CarContext
import androidx.core.content.ContextCompat


object CarSignsUtil {

    @JvmStatic
    fun buildRoadSignBitmap(
        signText: String,
        isExit: Boolean = false,
        density: Float = 3f,
        marginHorizontal: Float = 8f,
        marginVertical: Float = 5f,
        textSize: Float = 22f,
        roundRectRadius: Float = 4f,
        borderWidth: Float = 2f,
        dotted: Boolean = false,
        showBorder: Boolean = false
    ): Bitmap? {
        if (signText.isEmpty()) {
            return null
        }
        val paint = Paint()
        val paintMarginHor = marginHorizontal * density
        val paintMarginVert = marginVertical * density
        val paintTextSize = textSize * density
        val paintRoundRectRadius = roundRectRadius * density
        val paintBorderWidth = borderWidth * density
        val backgroundColor = if (!isExit) {
            Color.WHITE
        } else {
            Color.parseColor("#374253") // gray_700
        }
        val borderColor = if (!isExit) {
            Color.WHITE
        } else {
            Color.parseColor("#374253") // gray_700
        }
        paint.textSize = paintTextSize
        paint.isFakeBoldText = true
        paint.isAntiAlias = true
        paint.strokeWidth = paintBorderWidth
        val textBoundsRect = Rect()
        paint.getTextBounds(
            signText,
            0,
            signText.length,
            textBoundsRect
        )
        val width = (paintMarginHor * 2) + textBoundsRect.width()
        val height = (paintMarginVert * 2) + textBoundsRect.height()
        val bitmap = Bitmap.createBitmap(width.toInt(), height.toInt(), Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        //Background
        paint.style = Paint.Style.FILL
        paint.color = backgroundColor
        canvas.drawRoundRect(
            RectF(0F, 0F, canvas.width.toFloat(), canvas.height.toFloat()),
            paintRoundRectRadius,
            paintRoundRectRadius,
            paint
        )
        //Text
        paint.color = Color.BLACK
        canvas.drawText(
            signText,
            (width / 2) - textBoundsRect.exactCenterX(),
            (height / 2) - textBoundsRect.exactCenterY(),
            paint
        )
        //Border (dotted), if applicable
        if (showBorder && borderColor != backgroundColor) {
            //no need to draw if border and background color are the same; You wouldn't see difference.
            paint.style = Paint.Style.STROKE
            paint.color = borderColor
            if (dotted) {
                paint.pathEffect =
                    DashPathEffect(floatArrayOf(roundRectRadius, roundRectRadius), 0f)
            }
            canvas.drawRoundRect(
                RectF(
                    0F + paintBorderWidth / 2,
                    0F + paintBorderWidth / 2,
                    canvas.width.toFloat() - paintBorderWidth / 2,
                    canvas.height.toFloat() - paintBorderWidth / 2
                ),
                paintRoundRectRadius,
                paintRoundRectRadius,
                paint
            )
        }
        return bitmap
    }
}