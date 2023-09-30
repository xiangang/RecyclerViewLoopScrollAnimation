package com.nxg.loopscrollanimation

import android.animation.Animator
import android.animation.ValueAnimator
import android.os.Build
import android.view.MotionEvent
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.BaseInterpolator
import android.view.animation.Interpolator
import androidx.annotation.RequiresApi
import androidx.dynamicanimation.animation.DynamicAnimation
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce
import androidx.recyclerview.widget.RecyclerView
import com.nxg.loopscrollanimation.utils.LogUtil
import java.lang.Exception
import kotlin.math.pow
import kotlin.math.sin

/**
 * 适用于Android RecyclerView的循环滚动动画的帮助类
 * 可实现类似于老虎机抽奖，数字滚动等效果。
 */
open class RecyclerViewLoopScrollAnimation {

    companion object {

        private const val TAG = "ScrollView"

        /**
         * 此处用法见：
         * 你会用Kotlin实现构建者模式吗？
         * https://zhuanlan.zhihu.com/p/267145868
         */
        inline fun build(block: Configuration.() -> Unit) = Configuration().apply(block)
    }

    /**
     * attached的RecyclerView
     */
    var mRecyclerView: RecyclerView? = null

    /**
     * 配置
     */
    class Configuration {
        /**
         * 是否禁止Item相应手指滑动
         */
        var disableItemTouchListener = true

        /**
         * 第一阶段的滚动动画时长
         */
        var scrollAnimatorDuration = 3000L

        /**
         * 第一阶段的滚动动画的插值器
         */
        var scrollAnimatorInterpolator: BaseInterpolator = AccelerateDecelerateInterpolator()

        /**
         * 是否开启弹簧动画
         */
        var enableSpringAnimator = true

        /**
         * 第一阶段的滚动动画针对item的滚动步长（scrollBy方法的y参数）
         */
        var scrollAnimatorScrollByYStep = 30

        /**
         * 第二阶段的弹簧动画的SpringForce
         */
        var springAnimatorForce: SpringForce = SpringForce(0f)
            .setDampingRatio(SpringForce.DAMPING_RATIO_HIGH_BOUNCY)
            .setStiffness(SpringForce.STIFFNESS_VERY_LOW)
            .setDampingRatio(0.2f)
            .setStiffness(350f)

        /**
         * 弹簧动画的起始位置
         */
        var springAnimatorStartValue = -50f

    }

    /**
     * 配置
     */
    private var configuration: Configuration = build {}

    /**
     * 第一阶段的滚动动画
     */
    private var scrollAnimator: ValueAnimator = ValueAnimator.ofFloat(1f)

    /**
     * 当前的选中的Position
     */
    private var selectedPosition = 0

    /**
     * Item触摸事件监听器
     */
    private val recyclerViewDisableItemTouch: RecyclerView.OnItemTouchListener =
        RecyclerViewDisableItemTouch()

    /**
     * init代码块，执行顺序在主函数和次构造函数之间
     */
    init {
        LogUtil.i(TAG, "init block called!")
    }

    /**
     * RecyclerView滚动过程中需要执行的Action
     * 必须实现
     */
    private var recyclerViewScrollAction: RecyclerViewScrollAction? = null

    /**
     * 第一节阶段的滚动动画的动画更新监听器
     * 用于实现Item滚动的效果
     */
    private val scrollAnimatorUpdateListener =
        ValueAnimator.AnimatorUpdateListener { animation ->
            val value = animation.animatedValue as Float
            LogUtil.i(
                TAG,
                "scrollAnimatorUpdateListener : : value = $value, y = ${(-value * configuration.scrollAnimatorScrollByYStep).toInt()}"
            )
            mRecyclerView?.scrollBy(0, -(value * configuration.scrollAnimatorScrollByYStep).toInt())

        }

    /**
     * 滚动动画监听器
     */
    private var onScrollAnimatorListener: Animator.AnimatorListener? = null

    /**
     * 第一阶段的滚动动画的动画监听器
     * 主要用于动画结束后执行第二阶段的弹簧动画
     */
    private val scrollAnimatorListener = object : Animator.AnimatorListener {
        override fun onAnimationStart(animation: Animator?) {
            onScrollAnimatorListener?.onAnimationStart(animation)
        }

        override fun onAnimationEnd(animation: Animator?) {
            selectedPosition = recyclerViewScrollAction?.findLastVisibleItemPosition()!!
            mRecyclerView?.smoothScrollToPosition(selectedPosition)
            onScrollAnimatorListener?.onAnimationEnd(animation)
            if (configuration.enableSpringAnimator) {
                startSpringAnimator()
            }
        }

        override fun onAnimationCancel(animation: Animator?) {
            onScrollAnimatorListener?.onAnimationCancel(animation)
        }

        override fun onAnimationRepeat(animation: Animator?) {
            onScrollAnimatorListener?.onAnimationRepeat(animation)
        }
    }

    /**
     * 启动弹簧动画
     * 介绍见：https://developer.android.google.cn/guide/topics/graphics/spring-animation?hl=zh-cn
     */
    private fun startSpringAnimator() {
        SpringAnimation(mRecyclerView, DynamicAnimation.TRANSLATION_Y).apply {
            spring = configuration.springAnimatorForce
            cancel()
            setStartValue(configuration.springAnimatorStartValue)
            start()
        }
    }

    private var onSpringAnimationEndListener: DynamicAnimation.OnAnimationEndListener? = null

    /**
     * 弹簧插值器
     * @factor 值越小，代表弹簧的弹性越大，即上下振幅的频率越高
     * 弹簧算法： (2.0.pow((-10 * x).toDouble()) * sin((x - factor / 4) * (2 * Math.PI) / factor) + 1)
     * 算法来源于：http://inloop.github.io/interpolator/
     */
    class SpringAnimationInterpolator(private val factor: Float = 0.3f) : Interpolator {

        override fun getInterpolation(x: Float): Float {
            return (2.0.pow((-10 * x).toDouble()) * sin((x - factor / 4) * (2 * Math.PI) / factor) + 1).toFloat()
        }
    }

    /**
     * 拦截RecyclerView的item的touch事件，达到无法通过手指滑动RecyclerView的效果
     */
    class RecyclerViewDisableItemTouch : RecyclerView.OnItemTouchListener {

        override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
            return true
        }

        override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {

        }

        override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {

        }
    }

    /**
     * Called when an instance of a [RecyclerView] is attached.
     */
    @Throws(IllegalStateException::class)
    private fun setupCallbacks() {

    }

    /**
     * Called when the instance of a [RecyclerView] is detached.
     */
    private fun destroyCallbacks() {

    }

    /***************************** Public API below ***********************************************/

    /**
     * 自定义配置
     */
    fun setConfiguration(configuration: Configuration) {
        LogUtil.i(TAG, "setConfiguration function called! ${configuration.scrollAnimatorDuration}")
        this.configuration = configuration
    }

    /**
     * 附加RecyclerViewLoopScrollAnimation到RecyclerView上，使动画生效
     * Attaches the {@link RecyclerViewLoopScrollAnimation} to the provided RecyclerView,
     */
    @Throws(IllegalStateException::class)
    fun attachToRecyclerView(recyclerView: RecyclerView?) {
        if (mRecyclerView === recyclerView) {
            return  // nothing to do
        }
        if (mRecyclerView != null) {
            destroyCallbacks()
        }
        mRecyclerView = recyclerView
        if (mRecyclerView != null) {
            setupCallbacks()
        }
        this.scrollAnimator.addUpdateListener(scrollAnimatorUpdateListener)
        this.scrollAnimator.addListener(scrollAnimatorListener)
        if (this.configuration.disableItemTouchListener) {
            mRecyclerView?.addOnItemTouchListener(recyclerViewDisableItemTouch)
        }
    }

    /**
     * RecyclerView滚动过程中需要执行的Action
     */
    fun setRecyclerViewScrollAction(recyclerViewScrollAction: RecyclerViewScrollAction) {
        this.recyclerViewScrollAction = recyclerViewScrollAction
    }

    /**
     * 设置滚动动画监听器
     */
    fun setOnSpringAnimationEndListener(onScrollAnimatorListener: Animator.AnimatorListener) {
        this.onScrollAnimatorListener = onScrollAnimatorListener
    }

    /**
     * 设置弹簧动画结束监听器
     * 可用于动画结束后执行爆炸粒子效果
     */
    fun setOnSpringAnimationEndListener(onAnimationEndListener: DynamicAnimation.OnAnimationEndListener) {
        this.onSpringAnimationEndListener = onAnimationEndListener
    }

    /**
     * 开始执行动画
     * @param delay 延迟时间
     */
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    fun start(delay: Long = 0L) {
        LogUtil.i(TAG, "start function called! ${configuration.scrollAnimatorDuration}")
        if (recyclerViewScrollAction == null) {
            throw Exception("You must invoke setRecyclerViewScrollAction before start animation!!!")
        }
        this.scrollAnimator.duration = configuration.scrollAnimatorDuration
        this.scrollAnimator.interpolator = configuration.scrollAnimatorInterpolator
        this.scrollAnimator.cancel()
        this.scrollAnimator.startDelay = delay
        this.scrollAnimator.start()
    }
}

/**
 * 定义RecyclerView滚动过程中需要执行的Action
 */
interface RecyclerViewScrollAction {

    /**
     * 返回当前显示的第一个Item的Position
     * Returns the adapter position of the first visible view. This position does not include
     * adapter changes that were dispatched after the last layout pass.
     * return LooperLinearLayoutManager/LinearLayoutManager.findLastVisibleItemPosition()即可
     */
    fun findFirstVisibleItemPosition(): Int

    /**
     * 返回当前显示的最后一个Item的Position
     * Returns the adapter position of the last visible view. This position does not include
     * adapter changes that were dispatched after the last layout pass.
     * return LooperLinearLayoutManager/LinearLayoutManager.findLastVisibleItemPosition()即可
     */
    fun findLastVisibleItemPosition(): Int

}