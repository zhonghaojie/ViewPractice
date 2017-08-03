package com.nhsoft.view

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

/**
 * Created by zhonghaojie on 2017-08-03.
 */
class Clock(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : View(context, attrs, defStyleAttr, defStyleRes) {

    var minuteHandColor:Int=Color.BLACK
    var minuteHandWidth:Float=7f
    var minuteHandLength=30f
    var secondHandColor:Int=Color.BLACK
    var secondHandWidth:Float=4f
    var secondHandLength=40f
    var hourHandColor:Int=Color.BLACK
    var hourHandLenght=20f
    var hourHandWidth:Float=10f
    var circleColor:Int=Color.BLACK
    var radius:Float=200f
    var lineWidth1:Float=20f
    var lineWidth2:Float=10f
    var hourLineLength:Float=30f
    var secondLineLength:Float=15f
    var lineColor2:Int=Color.BLACK
    var lineColor1:Int=Color.BLACK
    var circleStrokeWidth:Float=20f

    val paintCircle=Paint()
    val paintHourLine=Paint()
    val paintSecondLine=Paint()
    val paintHour=Paint()
    val paintMinute=Paint()
    val paintSecond=Paint()

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int):this(context,attrs,defStyleAttr,0)
    constructor(context: Context?):this(context,null,0,0)
    constructor(context: Context?, attrs: AttributeSet?):this(context,attrs,0,0){
        val typeArray:TypedArray?=context?.obtainStyledAttributes(attrs,R.styleable.Clock)
        typeArray?.let {
            minuteHandColor=it.getColor(R.styleable.Clock_minuteHandColor,Color.BLACK)
            secondHandColor=it.getColor(R.styleable.Clock_secondHandColor,Color.BLACK)
            hourHandColor=it.getColor(R.styleable.Clock_hourHandColor,Color.BLACK)
            circleColor=it.getColor(R.styleable.Clock_circleColor,Color.BLACK)
            lineWidth1=it.getDimension(R.styleable.Clock_lineWidth1,20f)
            lineWidth2=it.getDimension(R.styleable.Clock_lineWidth2,10f)
            lineColor1=it.getColor(R.styleable.Clock_lineColor1,Color.BLACK)
            lineColor2=it.getColor(R.styleable.Clock_lineColor2,Color.BLACK)
            hourLineLength=it.getDimension(R.styleable.Clock_hourLineLength,30f)
            circleStrokeWidth=it.getDimension(R.styleable.Clock_circleStrokeWidth,20f)
            secondLineLength =it.getDimension(R.styleable.Clock_secondLineLength,15f)
            minuteHandWidth=it.getDimension(R.styleable.Clock_minuteHandWidth,7f)
            secondHandWidth=it.getDimension(R.styleable.Clock_secondHandWidth,4f)
            hourHandWidth=it.getDimension(R.styleable.Clock_hourHandWidth,10f)
            minuteHandLength=it.getDimension(R.styleable.Clock_minuteHandLength,30f)
            secondHandLength=it.getDimension(R.styleable.Clock_secondHandLength,40f)
            hourHandLenght=it.getDimension(R.styleable.Clock_hourHandLength,20f)

            paintCircle.style=Paint.Style.STROKE
            paintCircle.strokeWidth=circleStrokeWidth
            paintCircle.color=circleColor

            paintHourLine.style=Paint.Style.STROKE
            paintHourLine.color=lineColor1
            paintHourLine.strokeWidth=lineWidth1

            paintSecondLine.style=Paint.Style.STROKE
            paintSecondLine.color=lineColor2
            paintSecondLine.strokeWidth=lineWidth2

            paintHour.style=Paint.Style.FILL
            paintHour.color=hourHandColor
            paintHour.setShadowLayer(4f,4f,4f,Color.parseColor("#212112"))
            paintHour.strokeWidth=hourHandWidth

            paintMinute.style=Paint.Style.FILL
            paintMinute.color=minuteHandColor
            paintMinute.setShadowLayer(4f,4f,4f,Color.parseColor("#212112"))
            paintMinute.strokeWidth=minuteHandWidth

            paintSecond.style=Paint.Style.FILL
            paintSecond.color=secondHandColor
            paintSecond.setShadowLayer(4f,4f,4f,Color.parseColor("#212112"))
            paintSecond.strokeWidth=secondHandWidth
            it.recycle()
        }
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var width:Int
        val widthMode=MeasureSpec.getMode(widthMeasureSpec)
        val widthSize=MeasureSpec.getSize(widthMeasureSpec)
        if(widthMode==MeasureSpec.EXACTLY){
            width=widthSize
        }else{
            width=paddingStart+200+paddingEnd
        }
        setMeasuredDimension(width,width)
        radius=width/2f
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.let {
            it.drawCircle(radius,radius,radius-paddingLeft,paintCircle)
            it.save()

            it.drawLine(radius,paddingTop+hourLineLength,radius,paddingTop.toFloat()+paintHourLine.strokeWidth,paintHourLine)
            for(angle in 0..360 step 6){
                it.rotate(angle.toFloat(),radius,radius)
                if(angle%30==0){
                    val yStart=paddingTop+paintCircle.strokeWidth+hourLineLength
                    val yEnd=paddingTop+paintCircle.strokeWidth-paintHourLine.strokeWidth
                    it.drawLine(radius,yStart,radius,yEnd,paintHourLine)
                }else{
                    val yStart=paddingTop+paintCircle.strokeWidth+secondLineLength
                    val yEnd=paddingTop+paintCircle.strokeWidth-paintSecondLine.strokeWidth
                    it.drawLine(radius,yStart,radius,yEnd,paintSecondLine)
                }
                it.restore()
                it.save()
            }
            val hourLine=radius-hourHandLenght

            it.drawLine(radius,radius,radius,hourLine,paintHour)
        }
    }
}




