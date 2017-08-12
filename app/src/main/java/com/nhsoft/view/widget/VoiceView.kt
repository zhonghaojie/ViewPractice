package com.nhsoft.view.widget

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.nhsoft.view.R

/**
 * Created by zhonghaojie on 2017-08-11.
 */
class VoiceView(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : View(context, attrs, defStyleAttr, defStyleRes) {

    constructor(context: Context?):this(context,null,0,0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int):this(context,attrs,defStyleAttr,0)

    val paint=Paint()
    val bottomColor:Int=Color.parseColor("#252420")
    val topColor:Int=Color.parseColor("#ffffff")
    val bgColor:Int=Color.parseColor("#000000")
    lateinit var bmp:Bitmap
    var dotCount:Int=12
    var stroke:Float=20f
    var radius:Float=50f
    var splitWidth:Float=30f
    var progress:Int=0
    var imgScale:Int=1
    set(value) {
        if(value<=0){
            field=0
        }else if(value<=100){
            field=value
        }else if(value>100){
            field=100
        }
        postInvalidate()
    }
    constructor(context: Context?, attrs: AttributeSet?):this(context,attrs,0,0){
        paint.isAntiAlias=true
        bmp=BitmapFactory.decodeResource(resources, R.drawable.batman)
        val typedArray=context?.obtainStyledAttributes(attrs,R.styleable.VoiceView)
        typedArray?.let {
            imgScale=it.getInt(R.styleable.VoiceView_imgScale,1)
            val option=BitmapFactory.Options()
            option.inSampleSize=imgScale
            bmp=BitmapFactory.decodeResource(resources,it.getResourceId(R.styleable.VoiceView_centerImg,R.drawable.ic_launcher),option)
            dotCount=it.getInt(R.styleable.VoiceView_dotCount,12)
            stroke=it.getDimension(R.styleable.VoiceView_circleStrokeWidth,20f)
            radius=it.getDimension(R.styleable.VoiceView_radius,50f)
            splitWidth=it.getDimension(R.styleable.VoiceView_splitWidth,30f)
            it.recycle()
        }
    }


    override fun onDraw(canvas: Canvas?) {
        val rect:RectF= RectF(0f,0f,measuredWidth.toFloat(),measuredHeight.toFloat())

        canvas?.let {
            //画圆角的背景
            paint.style=Paint.Style.FILL
            paint.color=bgColor
            paint.alpha=30
            it.drawRoundRect(rect,4f,4f,paint)

            //画底部的progress
            paint.color=bottomColor
            if(radius>Math.min(measuredWidth,measuredHeight)/2f){
                radius=Math.min(measuredWidth,measuredHeight)/2f-50f
            }
            var blockLength:Float= (((4f/3f*Math.PI*radius)-(dotCount-1)*splitWidth)/dotCount).toFloat()
            if(blockLength<=0){
                splitWidth=30f
                blockLength=(((4f/3f*Math.PI*radius)-(dotCount-1)*splitWidth)/dotCount).toFloat()
            }
            val effect=DashPathEffect(floatArrayOf(blockLength,splitWidth),0f)//每一块由虚线+空余组成
            paint.pathEffect = effect
            paint.style=Paint.Style.STROKE
            paint.alpha=200
            paint.strokeWidth=stroke
            paint.strokeCap=Paint.Cap.ROUND
            val rect2= RectF(measuredWidth/2f-radius
                    ,measuredWidth/2f-radius
                    ,measuredWidth.toFloat()/2+radius
                    ,measuredWidth.toFloat()/2+radius)
            it.drawArc(rect2,150f,240f,false,paint)

            //画中间的图案

            val left=measuredWidth/2f-radius*Math.sin(45.0)+paddingStart
            val top=left
            val right=measuredWidth/2f+radius*Math.sin(45.0)-paddingEnd
            val bottom=right
            val centerRect=RectF(left.toFloat(),top.toFloat(),right.toFloat(),bottom.toFloat())
            var alpha=(progress+15f)/100f
            if(alpha>1f){
                alpha=1f
            }
            paint.alpha=(alpha*255f).toInt()
            it.drawBitmap(bmp,null,centerRect,paint)


            //画进度
            paint.alpha=255
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
        var width:Int=200
        var widthMode=MeasureSpec.getMode(widthMeasureSpec)
        if(widthMode == MeasureSpec.AT_MOST){
            val bmpSize:Int=Math.max(bmp.width,bmp.height)
            val size=Math.max(bmpSize,2*radius.toInt())+paddingEnd+paddingStart+stroke.toInt()*2
            width=size
        }else if(widthMode == MeasureSpec.EXACTLY){
            val size=MeasureSpec.getSize(widthMeasureSpec)
            width=size
            if(width/2<radius){
                width=2*radius.toInt()+stroke.toInt()*2
            }
        }
        setMeasuredDimension(width,width)
    }


    var y=0
    var x=0
    var dx=0
    var dy=0
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.let {
            when(it.action){
                MotionEvent.ACTION_DOWN->{
                    x=it.x.toInt()
                    y=it.y.toInt()
                }
                MotionEvent.ACTION_MOVE->{
                    dx= (it.x-x).toInt()
                    dy= (it.y-y).toInt()
                    val absDX=Math.abs(dx)
                    val absDY=Math.abs(dy)
                    if(absDX>absDY){
                        if(dx>0){
                            progress+=dx/20
                        }else{
                            progress-=absDX/20
                        }
                    }else{
                        if(dy>0){
                            progress-=dy/20
                        }else{
                            progress+=absDY/20
                        }
                    }
                    x=it.x.toInt()
                    y=it.y.toInt()
                }
            }
        }
        return true
    }


}