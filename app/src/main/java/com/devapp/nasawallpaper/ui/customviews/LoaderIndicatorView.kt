package com.devapp.nasawallpaper.ui.customviews

import android.animation.TimeAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Point
import android.util.AttributeSet
import android.view.View
import android.view.View.MeasureSpec.AT_MOST
import android.view.View.MeasureSpec.EXACTLY
import androidx.core.content.ContextCompat
import com.devapp.nasawallpaper.R
import kotlin.math.cos
import kotlin.math.sin

class LoaderIndicatorView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private val CIRCLES_COUNT = 3

    private val DEFAULT_CIRCLE_RADIUS_PX = 6
    private val DEFAULT_CIRCLE_SPACING_PX = 8

    private val INITIAL_CIRCLE_SCALE = 1f
    private val MAX_CIRCLE_SCALE = 1.3f

    private val circles = arrayOfNulls<Circle>(CIRCLES_COUNT)
    private val colors = arrayOf(
        ContextCompat.getColor(getContext(), R.color.colorAccent),
        ContextCompat.getColor(getContext(), R.color.colorAccent),
        ContextCompat.getColor(getContext(), R.color.colorAccent)
    )

    private val animator = TimeAnimator()
    private val paint = Paint()

    private var circleRadius: Int
    private var circleMaxRadius: Int
    private var spaceBetweenCenters: Int
    private var animationWidth = 0
    private var animationHeight = 0
    private var animationRadius = 0

    private var animationX: Float = 0f
    private var animationY: Float = 0f

    private var animationDelay: Int

    private val startAnimationRunnable = {startAnimation()}

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.LoaderIndicatorView)
        circleRadius = typedArray.getDimensionPixelSize(R.styleable.LoaderIndicatorView_loader_circleRadius, DEFAULT_CIRCLE_RADIUS_PX)
        circleMaxRadius = (circleRadius * MAX_CIRCLE_SCALE).toInt()

        val circleSpacing = typedArray.getDimensionPixelOffset(R.styleable.LoaderIndicatorView_loader_circleSpacing, DEFAULT_CIRCLE_SPACING_PX)
        spaceBetweenCenters = circleRadius * 2 + circleSpacing
        animationDelay = typedArray.getInteger(R.styleable.LoaderIndicatorView_loader_delay, 0);

        colors[0] = typedArray.getColor(R.styleable.LoaderIndicatorView_loader_color_1, colors[0])
        colors[1] = typedArray.getColor(R.styleable.LoaderIndicatorView_loader_color_2, colors[1])
        colors[2] = typedArray.getColor(R.styleable.LoaderIndicatorView_loader_color_3, colors[2])

        val mode = typedArray.getInt(R.styleable.LoaderIndicatorView_loader_mode, 0)

        typedArray.recycle()

        for (i in 0 until CIRCLES_COUNT){
            circles[i] = Circle(Point(), 0, INITIAL_CIRCLE_SCALE, colors[i])
        }
        paint.isAntiAlias = true

        val animatable = setMode(Mode.getModeByInt(mode))

        animator.setTimeListener { anim, totalTime, deltaTime -> run {
            animatable.animate(totalTime.toInt())
            postInvalidate()
        } }
    }

    fun setMode(mode: Mode): Animatable{
        when(mode){
            Mode.ROTATE -> {
                animationRadius = (spaceBetweenCenters / cos(30 * Math.PI / 180)).toInt()
                animationWidth = (animationRadius + circleMaxRadius) * 2 + 2;
                animationHeight = animationWidth
            }
            else -> {
                animationRadius = 0
                animationWidth = spaceBetweenCenters * (CIRCLES_COUNT - 1) + circleMaxRadius * 2 + 2
                animationHeight = circleMaxRadius * 2 + 2
            }

        }
        return when(mode){
            Mode.SCALE -> createSimpleAnimatable()
            Mode.ALPHA -> createAlphaAnimatable()
            Mode.ROTATE -> createFullAnimatable()
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        updateAnimationState()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        updateAnimationState()
    }

    override fun onVisibilityChanged(changedView: View, visibility: Int) {
        updateAnimationState()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val requiredWidth = animationWidth + paddingLeft + paddingRight
        val measuredWidth = calcMeasuredSize(widthMeasureSpec, requiredWidth);
        val requiredHeight = animationHeight + paddingTop + paddingBottom
        val measuredHeight = calcMeasuredSize(heightMeasureSpec, requiredHeight);
        setMeasuredDimension(measuredWidth, measuredHeight);
    }

    override fun layout(l: Int, t: Int, r: Int, b: Int) {
        super.layout(l, t, r, b)
        val width = r - l
        val height = b - t
        animationX = (width - animationWidth + paddingLeft + paddingRight) / 2f
        animationY = (height - animationHeight + paddingLeft + paddingRight) / 2f
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        canvas.save()
        canvas.translate(animationX, animationY)
        canvas.clipRect(0, 0, animationWidth, animationHeight)
        for (i in 0 until CIRCLES_COUNT){
            val circle = circles[i]
            val radius = circleRadius * circle!!.scale
            paint.color = circle.color
            val space = if(animationRadius == 0) 0f else animationWidth / 2f
            canvas.drawCircle(circle.position.x + space, circle.position.y + animationRadius.toFloat(), radius, paint)
        }
        canvas.restore()
    }

    private fun calcMeasuredSize(measureSpec: Int, requiredSize: Int): Int{
        val requestedSize = MeasureSpec.getSize(measureSpec)
        val sizeMode = MeasureSpec.getMode(measureSpec)
        return when (sizeMode) {
            EXACTLY -> requestedSize
            AT_MOST -> if(requiredSize < requestedSize) requiredSize else requestedSize
            else -> requiredSize
        }
    }

    private fun startAnimation(){
        if(!animator.isStarted) animator.start()
    }

    private fun stopAnimation(){
        if (animator.isStarted){
            animator.currentPlayTime = 0
            animator.cancel()
        }
        resetCirclesState()
        invalidate()
    }

    /**
     * Check view state and start or stop animation
     */
    private fun updateAnimationState(){
        val shouldStartAnimation = isAttachedToWindow && visibility == VISIBLE
        if(animator.isStarted != shouldStartAnimation){
            if(shouldStartAnimation){
                postDelayed(startAnimationRunnable, 500)
            }
            else {
                removeCallbacks(startAnimationRunnable)
                stopAnimation()
            }
        }
    }

    /**
     * Reset parameters after stop animation
     */
    private fun resetCirclesState(){
        circles.forEach {
            it?.let {
                it.color = colors[it.index]
                it.scale = INITIAL_CIRCLE_SCALE
            }
        }
    }

    /**
     * First scenario for animation
     */
    private fun createSimpleAnimatable(): Animatable{
        var x = circleMaxRadius
        val y = circleMaxRadius
        for (i in 0 until CIRCLES_COUNT) {
            circles[i]!!.position.x = x
            circles[i]!!.position.y = y
            x += spaceBetweenCenters
        }
        val upScale = AnimationSet(arrayOf(Scale(1f, MAX_CIRCLE_SCALE),  Alpha(255f, 200f)))
        val downScale = AnimationSet(arrayOf(Scale(MAX_CIRCLE_SCALE, 1f),  Alpha(200f, 255f)))

        val animatorsArray = arrayOfNulls<Animatable>(CIRCLES_COUNT)
        val animateStep = 1000 / CIRCLES_COUNT
        for (i in 0 until CIRCLES_COUNT) {
            animatorsArray[i] = Animator(circles[i]!!)
                .add(upScale, i * animateStep, (i+1)*animateStep)
                .add(downScale, ((i+0.5)*animateStep).toInt(), (i+2)*animateStep)
        }
        val animators = AnimatorSet(animatorsArray)

        return RepeatingAnimator(animators, 0, 1000, 0)
    }

    /**
     * Alpha animation
     */
    private fun createAlphaAnimatable(): Animatable{
        var x = circleMaxRadius
        val y = circleMaxRadius
        for (i in 0 until CIRCLES_COUNT) {
            circles[i]!!.position.x = x
            circles[i]!!.position.y = y
            x += spaceBetweenCenters
        }

        val decreaseAlpha = Alpha(255f, 127f)
        val increaseAlpha = Alpha(127f, 255f)

        val animatorsArray = arrayOfNulls<Animatable>(CIRCLES_COUNT)
        val animateStep = 1000 / CIRCLES_COUNT
        for (i in 0 until CIRCLES_COUNT) {
            animatorsArray[i] = Animator(circles[i]!!)
                .add(decreaseAlpha, i * animateStep, (i+1)*animateStep)
                .add(increaseAlpha, ((i+0.5)*animateStep).toInt(), (i+2)*animateStep)
        }
        val animators = AnimatorSet(animatorsArray)

        return RepeatingAnimator(animators, 0, 1000, 0)
    }

    /**
     * Animation with rotation
     */
    private fun createFullAnimatable(): Animatable{
        val timings = intArrayOf(0, 500, 1000, 1500, 2000)
        val centerX = animationWidth / 2
        val centerY = animationHeight / 2
        circles[1]!!.position.x = centerX
        circles[1]!!.position.y = centerY

        val leftCircleX = centerX - spaceBetweenCenters
        val leftCircleY = centerY
        circles[0]!!.position.x = leftCircleX
        circles[0]!!.position.y = leftCircleY

        val rightCircleX = centerX + spaceBetweenCenters
        val rightCircleY = centerY
        circles[2]!!.position.x = rightCircleX
        circles[2]!!.position.y = rightCircleY

        val lastSwirlRadius = (rightCircleX - centerX) / 2

        val animator1 = Animator(circles[0]!!)
            .add(Move(centerX, centerY, leftCircleX, leftCircleY), timings[0], timings[1]) // move to left
            .add(Swirl(leftCircleX + lastSwirlRadius, leftCircleY, lastSwirlRadius, lastSwirlRadius, -180, 180), timings[1], timings[2]) // rotate to center
            .add(Swirl(centerX + lastSwirlRadius, centerY, lastSwirlRadius, lastSwirlRadius, 180, -180), timings[2], timings[3]) // rotate to right
            .add(Move(rightCircleX, rightCircleY, centerX, centerY), timings[3], timings[4]) // move to center

        val animator2 = Animator(circles[1]!!)
            .add(Swirl(centerX - lastSwirlRadius, centerY, -lastSwirlRadius, -lastSwirlRadius, -180, 180), timings[1], timings[2]) // rotate to left
            .add(Move(leftCircleX, leftCircleY, centerX, centerY), timings[3], timings[4]) // move to center

        val animator3 = Animator(circles[2]!!)
            .add(Move(centerX, centerY, rightCircleX, leftCircleY), timings[0], timings[1]) // move to left
            .add(Swirl(centerX + lastSwirlRadius, centerY, -lastSwirlRadius, -lastSwirlRadius, 180, -180), timings[2], timings[3]) // rotate to center

        val animators = AnimatorSet(arrayOf(animator1, animator2, animator3))
        return RepeatingAnimator(animators, 0, timings[4], 0);
    }

    class AnimatorSet(private val animatable: Array<Animatable?>): Animatable{
        override fun animate(time: Int) {
            animatable.forEach { it?.let { it.animate(time)  }}
        }
    }

    class RepeatingAnimator(private val animatable: Animatable, private val delayBefore: Int, duration: Int, delayAfter: Int): Animatable{
        private val iterationTimeEnd = delayBefore + duration
        private val totalDuration = iterationTimeEnd + delayAfter

        override fun animate(time: Int) {
            val iterationTime = time % totalDuration
            if (iterationTime in delayBefore..iterationTimeEnd) {
                animatable.animate(iterationTime - delayBefore);
            }
        }
    }

    class Animator(private val circle: Circle): Animatable{
        private val scheduledAnimations = ArrayList<ScheduledAnimation>()

        fun add(animation: Animation, timeFrom: Int, timeTo: Int): Animator{
            scheduledAnimations.add(ScheduledAnimation(animation, timeFrom, timeTo))
            return this
        }

        override fun animate(time: Int) {
            scheduledAnimations.forEach {
                if (time >= it.timeFrom && time <= it.timeTo) {
                    it.animation.update(circle, time - it.timeFrom, it.duration)
                    it.isRunning = true
                }
                else if (it.isRunning) {
                    it.animation.update(circle, it.duration, it.duration)
                    it.isRunning = false
                }
            }
        }
    }

    interface Animatable{
        fun animate(time: Int)
    }

    class ScheduledAnimation(val animation: Animation, val timeFrom: Int, val timeTo: Int){
        var isRunning = false
        val duration = timeTo - timeFrom
    }

    class Alpha(private val from: Float, to: Float ) : Animation {
        private val range = to - from

        override fun update(circle: Circle, time: Int, duration: Int) {
            val alpha = from + range * time / duration
            circle.color = Color.argb(
                alpha.toInt(),
                Color.red(circle.color),
                Color.green(circle.color),
                Color.blue(circle.color)
            )
        }
    }

    class Scale(private val from: Float, to: Float ) : Animation {
        private val range = to - from

        override fun update(circle: Circle, time: Int, duration: Int) {
            circle.scale = from + range * time / duration
        }
    }

    class Move(private val xFrom: Int, private val yFrom: Int, xTo: Int, yTo: Int) : Animation {
        private val moveX = xTo - xFrom
        private val moveY = yTo - yFrom

        override fun update(circle: Circle, time: Int, duration: Int) {
            circle.position.x = xFrom + moveX * time / duration
            circle.position.y = yFrom + moveY * time / duration
        }
    }

    class Swirl(private val centerX: Int, private val centerY: Int, private val radiusFrom: Int, radiusTo: Int, private val startAngle: Int, private val rotation: Int): Animation{
        private val radiusRange = radiusTo - radiusFrom

        override fun update(circle: Circle, time: Int, duration: Int) {
            val angle = startAngle + rotation * time / duration.toDouble()
            val angleRad = Math.toRadians(angle)
            val radius = radiusFrom + radiusRange * time / duration.toDouble()
            circle.position.x = centerX + (cos(angleRad) * radius).toInt()
            circle.position.y = centerY + (sin(angleRad) * radius).toInt()
        }

    }

    class AnimationSet(private val animations: Array<Animation>): Animation{
        override fun update(circle: Circle, time: Int, duration: Int) {
            animations.forEach { it.update(circle, time, duration) }
        }
    }


    interface Animation {
        fun update(circle: Circle, time: Int, duration: Int)
    }

    class Circle (val position: Point, var index: Int, var scale: Float, var color: Int)

    enum class Mode(mode: Int){
        SCALE(0),
        ALPHA(1),
        ROTATE(2);

        companion object {
            fun getModeByInt(mode: Int): Mode{
                return when(mode){
                    1 -> ALPHA
                    2 -> ROTATE
                    else -> SCALE
                }
            }
        }

    }
}