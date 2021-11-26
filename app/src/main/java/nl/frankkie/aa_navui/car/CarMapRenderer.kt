package nl.frankkie.aa_navui.car

import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Typeface
import android.os.Handler
import android.os.SystemClock
import androidx.car.app.AppManager
import androidx.car.app.CarContext
import androidx.car.app.SurfaceCallback
import androidx.car.app.SurfaceContainer
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import kotlin.math.max

class CarMapRenderer(private val carContext: CarContext, serviceLifecycle: Lifecycle) :
    SurfaceCallback, DefaultLifecycleObserver {

    val handler = Handler()
    var surfaceContainer: SurfaceContainer? = null

    init {
        serviceLifecycle.addObserver(this)
    }

    override fun onSurfaceAvailable(surfaceContainer: SurfaceContainer) {
        this.surfaceContainer = surfaceContainer

        handler.post {
            // Start drawing the map on the android auto surface
            handler.removeCallbacksAndMessages(null)
            handler.post { drawOnSurfaceRecursive() }
        }
    }

    override fun onVisibleAreaChanged(visibleArea: Rect) {

    }

    override fun onStableAreaChanged(stableArea: Rect) {

    }

    override fun onSurfaceDestroyed(surfaceContainer: SurfaceContainer) {
        this.surfaceContainer = null
        handler.removeCallbacksAndMessages(null)
    }

    override fun onCreate(owner: LifecycleOwner) {
        carContext.getCarService(AppManager::class.java).setSurfaceCallback(this)
    }

    override fun onDestroy(owner: LifecycleOwner) {
        surfaceContainer = null
        handler.removeCallbacksAndMessages(null)
        try {
            carContext.getCarService(AppManager::class.java).setSurfaceCallback(null)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun drawOnSurfaceRecursive() {
        val drawingStart = SystemClock.elapsedRealtime()
        drawOnSurface()
        val drawingDelay =
            max(DRAWING_INTERVAL - (SystemClock.elapsedRealtime() - drawingStart), 0L)
        handler.postDelayed(::drawOnSurfaceRecursive, drawingDelay)
    }

    private fun drawOnSurface() {
        val paint = Paint()
        paint.color = Color.BLACK
        paint.isFakeBoldText = true
        paint.style = Paint.Style.FILL_AND_STROKE
        paint.textSize = 32F
        val surface = surfaceContainer?.surface ?: return
        val canvas = surface.lockHardwareCanvas()
        canvas.drawColor(Color.LTGRAY)
        canvas.drawText(
            "Route is active: ${BaseCarAppService.IS_ROUTE_ACTIVE}",
            canvas.width / 2.toFloat(),
            canvas.height / 2.toFloat(),
            paint
        )
        surface.unlockCanvasAndPost(canvas)
    }

    companion object {
        private const val FPS_LIMIT = 30
        private const val DRAWING_INTERVAL = (1000 / FPS_LIMIT).toLong()
    }
}