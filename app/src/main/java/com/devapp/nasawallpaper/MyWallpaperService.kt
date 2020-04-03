package com.devapp.nasawallpaper

import android.animation.Animator
import android.animation.ValueAnimator
import android.graphics.*
import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.service.wallpaper.WallpaperService
import android.view.MotionEvent
import android.view.SurfaceHolder
import com.devapp.nasawallpaper.logic.WallPapersRotator
import com.devapp.nasawallpaper.utils.UtilSensors
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicBoolean

class MyWallpaperService: WallpaperService() {
    override fun onCreateEngine(): Engine {
        return MyWallpaperEngine()
    }

    inner class MyWallpaperEngine : Engine() {

        private val handler = Handler(Looper.getMainLooper())
        private val drawRunner = Runnable { draw(0.00, 0.00) }
        var width = 0
        var height = 0
        private var visible = true
        private var va : ValueAnimator? = null
        private var animation = true
        private var wallPapersRotator: WallPapersRotator

        init {
            val handlerThread = HandlerThread("MyHandlerThread")
            handlerThread.start()
            wallPapersRotator = WallPapersRotator((application as App).dataRepository)
        }

        override fun onVisibilityChanged(visible: Boolean) {
            this.visible = visible
            if (visible) {
                animation = applicationContext.getSharedPreferences("WDPP", 0).getBoolean("animation", true)
                if(wallPapersRotator.currentBitmap != null || isPreview){
                    handler.post(drawRunner)
                }
                else {
                   nextImage()
                }
                UtilSensors.start(object : UtilSensors.SensorChangeListener {
                    override fun onUpdate(deltaX: Float, deltaY: Float) {

                        if(isPreview || wallPapersRotator.currentBitmap == null || !animation){
                            return
                        }

                        var percentX = deltaX/0.05
                        percentX = if(Math.abs(percentX) > 100 ) 100.00*(if(percentX < 0) -1 else 1) else percentX
                        var percentY = deltaY/0.05
                        percentY = if(Math.abs(percentY) > 100 ) 100.00*(if(percentY < 0) -1 else 1) else percentY

                        handler.post { this@MyWallpaperEngine.animated(percentX, percentY) }
                    }
                })
            } else {
                handler.removeCallbacks(drawRunner)
                nextImage()
                UtilSensors.stop()
            }
        }

        override fun onSurfaceRedrawNeeded(holder: SurfaceHolder?) {
            super.onSurfaceRedrawNeeded(holder)
            handler.post(drawRunner)
        }

        override fun onSurfaceDestroyed(holder: SurfaceHolder?) {
            super.onSurfaceDestroyed(holder)
            visible = false
            handler.removeCallbacks(drawRunner)
        }

        override fun onSurfaceChanged(
            holder: SurfaceHolder?, format: Int,
            width: Int, height: Int
        ) {
            this.width = width
            this.height = height
            super.onSurfaceChanged(holder, format, width, height)
        }

        override fun onTouchEvent(event: MotionEvent) {
//            if (touchEnabled) {
//                val x: Float = event.getX()
//                val y: Float = event.getY()
//                val holder: SurfaceHolder = surfaceHolder
//                var canvas: Canvas? = null
//                try {
//                    canvas = holder.lockCanvas()
//                    if (canvas != null) {
//                        canvas.drawColor(Color.BLACK)
//
//                    }
//                } finally {
//                    if (canvas != null) holder.unlockCanvasAndPost(canvas)
//                }
//                super.onTouchEvent(event)
//            }
        }

        private fun nextImage(){
            GlobalScope.launch {
                val success = wallPapersRotator.getNextImage()
                if(success){
                    handler.post(drawRunner)
                }
            }
        }

        var lastX = 0f
        var lastY = 0f
        val isRun = AtomicBoolean()
        var lastNewX = 0f
        var lastNewY = 0f

        private fun animated(newX: Double, newY: Double){
            lastNewX = newX.toFloat()
            lastNewY = newY.toFloat()

            if(va?.isRunning == true && Math.abs(lastX) - Math.abs(lastNewX) < 0.5){
                return
            }

            va?.cancel()

            va = ValueAnimator.ofFloat(lastX, newX.toFloat())
            va?.duration = (20 * Math.abs(lastX - newX)).toLong()
            va?.addUpdateListener(object : ValueAnimator.AnimatorUpdateListener{
                override fun onAnimationUpdate(animation: ValueAnimator) {
                    lastX = (animation.animatedValue as Float)
                    lastY = newY.toFloat()

                    draw((animation.animatedValue as Float) * 1.00, 0.00)
                }
            })

            va?.addListener(object : Animator.AnimatorListener{

                override fun onAnimationRepeat(animation: Animator?) {

                }

                override fun onAnimationEnd(animation: Animator?) {
                    isRun.set(false)
                }

                override fun onAnimationCancel(animation: Animator?) {
                    isRun.set(false)
                }

                override fun onAnimationStart(animation: Animator?) {

                }
            })


            va?.start()
        }

        private fun draw(percentX : Double, percentY: Double) {
            val holder: SurfaceHolder = surfaceHolder
            var canvas: Canvas? = null
            try {
                if(!holder.surface.isValid){
                    return
                }
                canvas = holder.lockCanvas()
                if (canvas != null) {

                    if(wallPapersRotator.currentBitmap != null && !isPreview)
                    {
                       drawBitmap(canvas, wallPapersRotator.currentBitmap!!, percentX, percentY)
                    }
                    else{
                        val options = BitmapFactory.Options()
                        options.inPreferredConfig = Bitmap.Config.ARGB_8888
                        // options.inJustDecodeBounds = true;
                        options.inSampleSize = calculateInSampleSize(
                            options,
                            width,
                            height
                        )
                        val defaultBg =
                            BitmapFactory.decodeResource(resources, R.drawable.default_bg, options)
                        drawBitmap(canvas, defaultBg, percentX, percentY)
                    }
                }
            } finally {
                if(!holder.surface.isValid){
                    return
                }
                try {
                    if (canvas != null) holder.unlockCanvasAndPost(canvas)
                } catch (e : Exception){
                    e.printStackTrace()
                }
            }
        }

        private fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
            // Raw height and width of image
            val (height: Int, width: Int) = options.run { outHeight to outWidth }
            var inSampleSize = 1

            if (height > reqHeight || width > reqWidth) {

                val halfHeight: Int = height / 2
                val halfWidth: Int = width / 2

                // Calculate the largest inSampleSize value that is a power of 2 and keeps both
                // height and width larger than the requested height and width.
                while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                    inSampleSize *= 2
                }
            }

            return inSampleSize
        }

        private fun drawBitmap(canvas: Canvas, bitmap: Bitmap, percentX : Double, percentY: Double){
            val mBitmapPaint = Paint(Paint.DITHER_FLAG)
            var w: Int = bitmap.getWidth()
            var h: Int = bitmap.getHeight()

            if(w < width && h < height){
                if(width - w >= height - h){
                    val r = width.toFloat() / w.toFloat()
                    w = (w * r).toInt()
                    h = (h * r).toInt()
                }
                else{
                    val r = height.toFloat() / h.toFloat()
                    w = (w * r).toInt()
                    h = (h * r).toInt()
                }
            }
            else if (w < width){
                val r = width.toFloat() / w.toFloat()
                w = (w * r).toInt()
                h = (h * r).toInt()
            }
            else if(h < height) {
                val r = height.toFloat() / h.toFloat()
                w = (w * r).toInt()
                h = (h * r).toInt()
            }
            else if(w > width && h > height) {
                if(w - width >= h - height){
                    val r = width.toFloat() / w.toFloat()
                    w = (w * r).toInt()
                    h = (h * r).toInt()
                }
                else {
                    val r = height.toFloat() / h.toFloat()
                    w = (w * r).toInt()
                    h = (h * r).toInt()
                }
            }

            canvas.drawColor(Color.BLACK)
            var left = (width - w)/2f
            var top = (height - h)/2f
            left = (left + (left * percentX/100)).toFloat()
            top = (top + (top * percentY/100)).toFloat()
            canvas.drawBitmap(getResizedBitmap(bitmap, w, h, true), left, top, mBitmapPaint)
        }

        private fun getResizedBitmap(
            bm: Bitmap,
            newWidth: Int,
            newHeight: Int,
            isNecessaryToKeepOrig: Boolean
        ): Bitmap {
            val width = bm.width
            val height = bm.height
            val scaleWidth = newWidth.toFloat() / width
            val scaleHeight = newHeight.toFloat() / height
            // CREATE A MATRIX FOR THE MANIPULATION
            val matrix = Matrix()
            // RESIZE THE BIT MAP
            matrix.postScale(scaleWidth, scaleHeight)
            // "RECREATE" THE NEW BITMAP
            val resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false)
            if (!isNecessaryToKeepOrig) {
                bm.recycle()
            }
            return resizedBitmap
        }

    }
}