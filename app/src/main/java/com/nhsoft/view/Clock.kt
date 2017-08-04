package com.nhsoft.view

import android.content.Context
import android.content.res.TypedArray
import android.graphics.*
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.AttributeSet
import android.util.Log
import android.view.View
import java.util.*


/**
 * Created by zhonghaojie on 2017-08-03.
 */
class Clock(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : View(context, attrs, defStyleAttr, defStyleRes) {

    var minuteHandColor:Int=Color.BLACK
    var minuteHandLength=30f
    var secondHandColor:Int=Color.BLACK
    var secondHandWidth:Float=4f
    var secondHandLength=40f
    var hourHandColor:Int=Color.BLACK
    var hourHandLenght=20f
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

    private val handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            if (msg.what == 0) {
                invalidate()
            }
        }
    }


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
            secondHandWidth=it.getDimension(R.styleable.Clock_secondHandWidth,4f)
            minuteHandLength=it.getDimension(R.styleable.Clock_minuteHandLength,30f)
            secondHandLength=it.getDimension(R.styleable.Clock_secondHandLength,40f)
            hourHandLenght=it.getDimension(R.styleable.Clock_hourHandLength,20f)

            paintCircle.isAntiAlias=true
            paintCircle.style=Paint.Style.STROKE
            paintCircle.strokeWidth=circleStrokeWidth
            paintCircle.color=circleColor

            paintHourLine.style=Paint.Style.STROKE
            paintHourLine.color=lineColor1
            paintHourLine.strokeWidth=lineWidth1

            paintSecondLine.style=Paint.Style.STROKE
            paintSecondLine.color=lineColor2
            paintSecondLine.strokeWidth=lineWidth2

            val pathEffect1= DashPathEffect(floatArrayOf(10f,5f),4f)
            val pathEffect2= DiscretePathEffect(5f,10f)
            paintHour.style=Paint.Style.FILL
            paintHour.color=hourHandColor
            paintHour.pathEffect= ComposePathEffect(pathEffect1,pathEffect2)
            paintHour.setShadowLayer(4f,4f,3f,Color.parseColor("#212112"))
            paintHour.strokeWidth=lineWidth1

            paintMinute.style=Paint.Style.FILL
            paintMinute.color=minuteHandColor
            paintMinute.pathEffect= ComposePathEffect(pathEffect1,pathEffect2)
            paintMinute.setShadowLayer(4f,4f,5f,Color.parseColor("#212112"))
            paintMinute.strokeWidth=lineWidth2

            paintSecond.style=Paint.Style.FILL
            paintSecond.color=secondHandColor
            paintSecond.pathEffect= ComposePathEffect(pathEffect1,pathEffect2)
            paintSecond.setShadowLayer(4f,4f,7f,Color.parseColor("#212112"))
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
            super.onDraw(it)
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
            var angle:Int=getHour()*30+getMinute()/2
            Log.i("time","hour ${getHour()}  angle  $angle")
            it.drawLine(radius,radius,caculateX(angle,hourHandLenght.toInt()),caculateY(angle,hourHandLenght.toInt()),paintHour)
            angle=getMinute()*6
            Log.i("time","minute ${getMinute()}  angle  $angle")
            it.drawLine(radius,radius,caculateX(angle,minuteHandLength.toInt()),caculateY(angle,minuteHandLength.toInt()),paintMinute)
            angle=getSecond()*6
            Log.i("time","second ${getSecond()}  angle  $angle")
            it.drawLine(radius,radius,caculateX(angle,secondHandLength.toInt()),caculateY(angle,secondHandLength.toInt()),paintSecond)
            handler.sendEmptyMessageDelayed(0,200)
        }
    }

    private fun getHour():Int{
        val calendar:Calendar= Calendar.getInstance()
        return calendar.get(Calendar.HOUR)
    }
    private fun getMinute():Int{
        val calendar:Calendar= Calendar.getInstance()
        return calendar.get(Calendar.MINUTE)
    }

    private fun getSecond():Int{
        val calendar:Calendar= Calendar.getInstance()
        return calendar.get(Calendar.SECOND)
    }

    private fun caculateX(angle:Int,length:Int):Float{
        val x=radius+length.times(Math.sin(angle.toDouble()*Math.PI/180)).toFloat()
        Log.i("position","x $x")
        return x
    }
    private fun caculateY(angle:Int,length:Int):Float{
        val y=radius-length.times(Math.cos(angle.toDouble()*Math.PI/180)).toFloat()
        Log.i("position","y $y")
        return y
    }
}




