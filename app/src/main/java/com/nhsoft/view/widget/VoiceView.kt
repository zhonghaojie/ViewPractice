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

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        when(keyCode){
            KeyEvent.KEYCODE_VOLUME_DOWN->{
                progress=am.getStreamVolume(currentStreamType)
            }
            KeyEvent.KEYCODE_VOLUME_UP->{
                progress=am.getStreamVolume(currentStreamType)
            }
        }
        return super.onKeyDown(keyCode, event)
    }
    constructor(context: Context?) : this(context, null, 0, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : this(context, attrs, defStyleAttr, 0)

    val paint = Paint()
    var bottomColor: Int = Color.parseColor("#252420")
    var topColor: Int = Color.parseColor("#ffffff")
    var bgColor: Int = Color.parseColor("#ffffff")
    var bmp: Bitmap? = null
    var dotCount: Int = 12
    var stroke: Float = 20f
    var radius: Float = 50f
    var splitWidth: Float = 30f
    var imgScale: Int = 1
    var maxProgress:Int=100
    lateinit var am:AudioManager
    var currentStreamType=AudioManager.STREAM_RING
    var bgCorner:Float=30f

    var progress: Int = 0
        set(value) {
            if (value <= 0) {
                field = 0
            } else if (value <= maxProgress) {
                field = value
            } else if (value > maxProgress) {
                field = maxProgress
            }
            postInvalidate()
        }

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
            bgCorner=it.getDimension(R.styleable.VoiceView_bgCorner,30f)
            dotCount = it.getInt(R.styleable.VoiceView_dotCount, 12)
            stroke = it.getDimension(R.styleable.VoiceView_circleStrokeWidth, 20f)
            radius = it.getDimension(R.styleable.VoiceView_radius, 50f)
            splitWidth = it.getDimension(R.styleable.VoiceView_splitWidth, 30f)
            bgColor=it.getColor(R.styleable.VoiceView_bgColor,Color.WHITE)
            topColor=it.getColor(R.styleable.VoiceView_fillColor,Color.WHITE)
            it.recycle()


            am=context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
            if(am.isMusicActive){
                currentStreamType=AudioManager.STREAM_MUSIC
            }else{
                currentStreamType=AudioManager.STREAM_RING
            }
            maxProgress=am.getStreamMaxVolume(currentStreamType)
            progress=am.getStreamVolume(currentStreamType)
        }
        isFocusable=true
        isFocusableInTouchMode=true
    }


    override fun onDraw(canvas: Canvas?) {
        val rect: RectF = RectF(0f
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
            var blockLength: Float = (((4f / 3f * Math.PI * radius) - (dotCount-1) * splitWidth) / dotCount).toFloat()
            if (blockLength <= 0) {
                splitWidth = 30f
                blockLength = (((4f / 3f * Math.PI * radius) - (dotCount-1) * splitWidth) / dotCount).toFloat()
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
                    , measuredHeight.toFloat() / 2 + radius  )
            it.drawArc(rect2, 150f, 240f, false, paint)

            //画中间的图案

            bmp?.let {
                var leftBmp = measuredWidth / 2 - (bmp?.width)!! / 2f
                var topBmp= measuredHeight/2-bmp?.height!!/2f
                var rightBmp=measuredWidth/2+bmp?.width!!/2f
                var bottomBmp=measuredHeight/2+bmp?.height!!/2f

                val left = measuredWidth / 2f - radius * Math.sin(45.0)
                val top = left
                val right = measuredWidth / 2f + radius * Math.sin(45.0)
                val bottom = right
                if(leftBmp<left){
                    leftBmp= left.toFloat()
                }
                if(topBmp<top){
                    topBmp=top.toFloat()
                }
                if(rightBmp>right){
                    rightBmp=right.toFloat()
                }
                if(bottomBmp>bottom){
                    bottomBmp=bottom.toFloat()
                }
                val centerRect = RectF(leftBmp, topBmp, rightBmp, bottomBmp)
//                var alpha = (progress.toFloat()/maxProgress.toFloat())*255
//                if (alpha ==0f) {
//                    alpha = 10f
//                }
//                paint.alpha = alpha.toInt()
                val saved = canvas.saveLayer(null, null, Canvas.ALL_SAVE_FLAG)
                canvas.drawBitmap(bmp, null, centerRect, paint)


                val pXfermode=PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
                paint.xfermode=pXfermode
                paint.color=topColor
                paint.style=Paint.Style.FILL
                var width = (progress.toFloat()/maxProgress.toFloat())*(rightBmp-leftBmp)+leftBmp
                val rect=RectF(leftBmp, topBmp,width, bottomBmp)
                canvas.drawRect(rect,paint)
                paint.xfermode=null
                canvas.restoreToCount(saved)

            }

            //画进度
            paint.style=Paint.Style.STROKE
            paint.alpha = 255
            paint.color = topColor
            if (progress <= 0f) {
                progress = 0

            } else if (progress < maxProgress) {
                it.drawArc(rect2, 150f, 240*(progress.toFloat()/maxProgress.toFloat()), false, paint)
            } else {
                progress = maxProgress
                it.drawArc(rect2, 150f, 240f, false, paint)
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        var width: Int = 200
        var widthMode = MeasureSpec.getMode(widthMeasureSpec)
        if (widthMode == MeasureSpec.AT_MOST) {
            if (bmp != null) {
                val bmpSize: Int = Math.max(bmp!!.width, bmp!!.height)
                if(bmpSize>radius){
                    val halfBmp=bmpSize/2
                    radius= (Math.sqrt(2.0)*halfBmp).toFloat()
                }
            }
            width = (2 * (radius+stroke) + paddingEnd + paddingStart).toInt()
        } else if (widthMode == MeasureSpec.EXACTLY) {
            val size = MeasureSpec.getSize(widthMeasureSpec)
            width = size
            if (width / 2 < radius) {
                radius = (width - paddingStart - paddingEnd-2*stroke) / 2f
            }
        }


        var height:Int=200
        val heightMode=MeasureSpec.getMode(heightMeasureSpec)
        if (heightMode == MeasureSpec.AT_MOST) {
            if (bmp != null) {
                val bmpSize: Int = Math.max(bmp!!.width, bmp!!.height)
                if(bmpSize>radius){
                    val halfBmp=bmpSize/2
                    radius= (Math.sqrt(2.0)*halfBmp).toFloat()
                }
            }
            height = (2 * (radius+stroke) + paddingTop + paddingBottom).toInt()
        } else if (heightMode == MeasureSpec.EXACTLY) {
            val size = MeasureSpec.getSize(widthMeasureSpec)
            height = size
            if (height / 2 < radius) {
                radius = (height - paddingTop - paddingBottom-2*stroke) / 2f
            }
        }
        setMeasuredDimension(width, height)
    }

}