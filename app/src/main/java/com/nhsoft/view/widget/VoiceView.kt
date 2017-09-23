package com.nhsoft.view.widget

import android.content.Context
import android.graphics.*
import android.media.AudioManager
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.View
import com.nhsoft.view.R


/**
 * Created by zhonghaojie on 2017-08-11.
 */
class VoiceView(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : View(context, attrs, defStyleAttr, defStyleRes) {


    constructor(context: Context?) : this(context, null, 0, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : this(context, attrs, defStyleAttr, 0)

    private val paint = Paint()
    var bottomColor: Int = Color.parseColor("#252420")
    var topColor: Int = Color.parseColor("#ffffff")
    var bgColor: Int = Color.parseColor("#ffffff")
    var bmp: Bitmap? = null
    var dotCount: Int = 12
    var stroke: Float = 20f
    var radius: Float = 50f
    var splitWidth: Float = 30f
    var imgScale: Int = 1
    var maxProgress: Float = 100f
    var bgCorner: Float = 30f
    var progress: Float = 0f
        set(value) {
            if (value in 0f..maxProgress) {
                field = value
                postInvalidate()
            }
            postInvalidate()
        }
        get() = field

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0, 0) {
        paint.isAntiAlias = true
        bmp = BitmapFactory.decodeResource(resources, R.drawable.batman)
        val typedArray = context?.obtainStyledAttributes(attrs, R.styleable.VoiceView)
        typedArray?.let {
            imgScale = it.getInt(R.styleable.VoiceView_imgScale, 1)
            val option = BitmapFactory.Options()
            option.inSampleSize = imgScale
            val bmpResID = it.getResourceId(R.styleable.VoiceView_centerImg, 0)
            if (bmpResID != 0) {
                bmp = BitmapFactory.decodeResource(resources, bmpResID, option)
            }
            bgCorner = it.getDimension(R.styleable.VoiceView_bgCorner, 30f)
            dotCount = it.getInt(R.styleable.VoiceView_dotCount, 12)
            stroke = it.getDimension(R.styleable.VoiceView_circleStrokeWidth, 20f)
            radius = it.getDimension(R.styleable.VoiceView_radius, 50f)
            splitWidth = it.getDimension(R.styleable.VoiceView_splitWidth, 30f)
            bgColor = it.getColor(R.styleable.VoiceView_bgColor, Color.WHITE)
            topColor = it.getColor(R.styleable.VoiceView_fillColor, Color.WHITE)
            it.recycle()
        }
        isFocusable = true
        isFocusableInTouchMode = true
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val rect = RectF(0f
                , 0f
                , measuredWidth.toFloat()
                , measuredHeight.toFloat()
        )

        canvas?.let {
            //画圆角的背景
            paint.style = Paint.Style.FILL
            paint.color = bgColor
            paint.alpha = 180
            it.drawRoundRect(rect, bgCorner, bgCorner, paint)

            //画底部的progress
            paint.color = bottomColor
            var blockLength: Float = (((4f / 3f * Math.PI * radius) - (dotCount - 1) * splitWidth) / dotCount).toFloat()
            if (blockLength <= 0) {
                splitWidth = 30f
                blockLength = (((4f / 3f * Math.PI * radius) - (dotCount - 1) * splitWidth) / dotCount).toFloat()
            }
            val effect = DashPathEffect(floatArrayOf(blockLength, splitWidth), 0f)//每一块由虚线+空白组成
            paint.pathEffect = effect
            paint.style = Paint.Style.STROKE
            paint.alpha = 200
            paint.strokeWidth = stroke
            paint.strokeCap = Paint.Cap.ROUND
            val rect2 = RectF(measuredWidth / 2f - radius
                    , measuredHeight / 2f - radius
                    , measuredWidth.toFloat() / 2 + radius
                    , measuredHeight.toFloat() / 2 + radius)
            it.drawArc(rect2, 135f, 270f, false, paint)

            //画中间的图案

            bmp?.let {
                var leftBmp = measuredWidth / 2 - (bmp?.width)!! / 2f
                var topBmp = measuredHeight / 2 - bmp?.height!! / 2f
                var rightBmp = measuredWidth / 2 + bmp?.width!! / 2f
                var bottomBmp = measuredHeight / 2 + bmp?.height!! / 2f

                val left = measuredWidth / 2f - radius * Math.sin(45.0)
                val top = left
                val right = measuredWidth / 2f + radius * Math.sin(45.0)
                val bottom = right
                if (leftBmp < left) {
                    leftBmp = left.toFloat()
                }
                if (topBmp < top) {
                    topBmp = top.toFloat()
                }
                if (rightBmp > right) {
                    rightBmp = right.toFloat()
                }
                if (bottomBmp > bottom) {
                    bottomBmp = bottom.toFloat()
                }
                val centerRect = RectF(leftBmp, topBmp, rightBmp, bottomBmp)
                val saved = canvas.saveLayer(null, null, Canvas.ALL_SAVE_FLAG)
                canvas.drawBitmap(bmp, null, centerRect, paint)


                val pXfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
                paint.xfermode = pXfermode
                paint.color = topColor
                paint.style = Paint.Style.FILL
                var width = (progress / maxProgress) * (rightBmp - leftBmp) + leftBmp
                val rect = RectF(leftBmp, topBmp, width, bottomBmp)
                canvas.drawRect(rect, paint)
                paint.xfermode = null
                canvas.restoreToCount(saved)

            }

            //画进度
            paint.style = Paint.Style.STROKE
            paint.alpha = 255
            paint.color = topColor
            it.drawArc(rect2, 135f, progress * 2.7f, false, paint)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width=2*(radius+stroke+40).toInt()
        setMeasuredDimension(width,width)
    }

}