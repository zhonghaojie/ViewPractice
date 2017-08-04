package com.nhsoft.view.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.nhsoft.view.R

/**
 * Created by zhonghaojie on 2017-07-27.
 */
class RoundProgress(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : View(context, attrs, defStyleAttr, defStyleRes) {


    private var roundColor:Int= Color.parseColor("#c1c1c1")
    private var roundProgressColor:Int= Color.parseColor("#212121")
    private var roundWidth=20f
    private var textColor:Int= Color.parseColor("#f04134")
    private var textSize=22f
    private var max=100
    private var isShowText=true
    private var style=0
    private val paint= Paint()
    private var progress=0
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int):this(context,attrs,defStyleAttr,0)
    constructor(context: Context?):this(context,null,0,0)
    constructor(context: Context?, attrs: AttributeSet?):this(context,attrs,0,0){
        val typedArray=context?.obtainStyledAttributes(attrs, R.styleable.RoundProgress)
        typedArray?.let {
            roundColor=it.getColor(R.styleable.RoundProgress_roundColor, Color.parseColor("#c1c1c1"))
            roundProgressColor=it.getColor(R.styleable.RoundProgress_roundProgressColor, Color.parseColor("#212121"))
            roundWidth=it.getDimension(R.styleable.RoundProgress_roundWidth,5f)
            textColor=it.getColor(R.styleable.RoundProgress_textColor, Color.parseColor("#f04134"))
            textSize=it.getDimension(R.styleable.RoundProgress_textSize,22f)
            max=it.getInt(R.styleable.RoundProgress_max,100)
            isShowText=it.getBoolean(R.styleable.RoundProgress_isShowText,true)
            style=it.getInt(R.styleable.RoundProgress_style,0)
            it.recycle()
        }
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let {
            paint.isAntiAlias=true
            //画圆
            paint.style= Paint.Style.STROKE
            paint.strokeWidth= roundWidth
            paint.color=roundColor
            val center=width/2f
            val radius=center - roundWidth/2
            val pathEffect= DashPathEffect(floatArrayOf(10f,5f),0f)
            paint.pathEffect=pathEffect
            it.drawCircle(center,center,radius,paint)
            paint.pathEffect=null
            if(isShowText){
                //画字
                paint.strokeWidth=0f
                paint.setShadowLayer(3f,3f,3f, Color.parseColor("#212121"))
                paint.textSize= textSize
                paint.color=textColor
                val textWidth=paint.measureText(progress.toString())
                it.drawText(progress.toString(), center-textWidth/2, center+textWidth/2,paint)
            }
            //画进度
            paint.pathEffect=pathEffect
            paint.clearShadowLayer()
            paint.strokeWidth= roundWidth
            paint.color=roundProgressColor
            val sweepAngle=(progress.toFloat()/max.toFloat())*360
            it.drawArc(roundWidth/2f,roundWidth/2f,center+radius,center+radius,0f,sweepAngle,false,paint)
            paint.pathEffect=null
        }
    }

    @Synchronized fun getMaxValue(): Int {
        return max
    }

    fun reset(){
        progress=0
        postInvalidate()
    }
    /**
     * 设置进度的最大值
     * @param max
     */
    @Synchronized fun setMaxValue(max: Int) {
        if (max < 0) {
            throw IllegalArgumentException("max not less than 0")
        }
        this.max = max
    }

    /**
     * 获取进度.需要同步
     * @return
     */
    @Synchronized fun getProgressValue(): Int {
        return progress
    }

    /**
     * 设置进度，此为线程安全控件，由于考虑多线的问题，需要同步
     * 刷新界面调用postInvalidate()能在非UI线程刷新
     * @param progress
     */
    @Synchronized fun setProgressValue(progress: Int) {
        var progress = progress
        if (progress < 0) {
            throw IllegalArgumentException("progress not less than 0")
        }
        if (progress > max) {
            progress = max
        }
        if (progress <= max) {
            this.progress = progress
            postInvalidate()
        }

    }

}



