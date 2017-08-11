package com.nhsoft.view.widget

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

/**
 * Created by zhonghaojie on 2017-08-11.
 */
class VoiceView(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : View(context, attrs, defStyleAttr, defStyleRes) {

    constructor(context: Context?):this(context,null,0,0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int):this(context,attrs,defStyleAttr,0)

    val paint=Paint()
    val bottomColor:Int=Color.parseColor("#252420")
    val topColor:Int=Color.parseColor("#ffffff")
    val bgColor:Int=Color.parseColor("#212121")
    var progress:Int=0
    set(value) {
        field=value
        postInvalidate()
    }
    constructor(context: Context?, attrs: AttributeSet?):this(context,attrs,0,0){
        paint.isAntiAlias=true

    }


    override fun onDraw(canvas: Canvas?) {
        val rect:RectF= RectF(0f,0f,measuredWidth.toFloat(),measuredHeight.toFloat())

        paint.setShadowLayer(4f,4f,4f,Color.parseColor("#212121"))
        canvas?.let {
            //画圆角的背景
            paint.style=Paint.Style.FILL
            paint.color=bgColor
            paint.alpha=10
            it.drawRoundRect(rect,4f,4f,paint)

            //画底部的progress
            paint.color=bottomColor
            val radius=Math.min(measuredWidth,measuredHeight)/2f-50f
            val blockLength=(radius*Math.PI/9).toFloat()//分成12块，总共240度，每一块20度，计算长度
            val effect=DashPathEffect(floatArrayOf(blockLength*2f/3f,blockLength/3f),0f)//每一块由虚线+空余组成
            paint.pathEffect = effect
            paint.style=Paint.Style.STROKE
            paint.alpha=200
            paint.strokeWidth=20f
            paint.strokeCap=Paint.Cap.ROUND
            val rect2= RectF(60f,60f,measuredWidth.toFloat()-60f,measuredHeight.toFloat()-60f)
            it.drawArc(rect2,150f,240f,false,paint)

            //画进度
            paint.color=topColor
            if(progress<=0f){
                progress=0

            }else if(progress<100){
                it.drawArc(rect2,150f,2.4f*progress,false,paint)
            }else{
                progress=100
                it.drawArc(rect2,150f,240f,false,paint)
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width=Math.min(MeasureSpec.getSize(widthMeasureSpec),MeasureSpec.getSize(heightMeasureSpec))
        setMeasuredDimension(width,width)
    }



}