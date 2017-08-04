package com.nhsoft.view.widget


/**
 * Created by zhonghaojie on 2017-08-03.
 */
class Clock(context: android.content.Context?, attrs: android.util.AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : android.view.View(context, attrs, defStyleAttr, defStyleRes) {

    var minuteHandColor:Int= android.graphics.Color.BLACK
    var minuteHandLength=30f
    var secondHandColor:Int= android.graphics.Color.BLACK
    var secondHandWidth:Float=4f
    var secondHandLength=40f
    var hourHandColor:Int= android.graphics.Color.BLACK
    var hourHandLenght=20f
    var circleColor:Int= android.graphics.Color.BLACK
    var radius:Float=200f
    var lineWidth1:Float=20f
    var lineWidth2:Float=10f
    var hourLineLength:Float=30f
    var secondLineLength:Float=15f
    var lineColor2:Int= android.graphics.Color.BLACK
    var lineColor1:Int= android.graphics.Color.BLACK
    var circleStrokeWidth:Float=20f

    val paintCircle= android.graphics.Paint()
    val paintHourLine= android.graphics.Paint()
    val paintSecondLine= android.graphics.Paint()
    val paintHour= android.graphics.Paint()
    val paintMinute= android.graphics.Paint()
    val paintSecond= android.graphics.Paint()

    private val handler = object : android.os.Handler(android.os.Looper.getMainLooper()) {
        override fun handleMessage(msg: android.os.Message) {
            super.handleMessage(msg)
            if (msg.what == 0) {
                invalidate()
            }
        }
    }


    constructor(context: android.content.Context?, attrs: android.util.AttributeSet?, defStyleAttr: Int):this(context,attrs,defStyleAttr,0)
    constructor(context: android.content.Context?):this(context,null,0,0)
    constructor(context: android.content.Context?, attrs: android.util.AttributeSet?):this(context,attrs,0,0){
        val typeArray: android.content.res.TypedArray?=context?.obtainStyledAttributes(attrs, com.nhsoft.view.R.styleable.Clock)
        typeArray?.let {
            minuteHandColor=it.getColor(com.nhsoft.view.R.styleable.Clock_minuteHandColor, android.graphics.Color.BLACK)
            secondHandColor=it.getColor(com.nhsoft.view.R.styleable.Clock_secondHandColor, android.graphics.Color.BLACK)
            hourHandColor=it.getColor(com.nhsoft.view.R.styleable.Clock_hourHandColor, android.graphics.Color.BLACK)
            circleColor=it.getColor(com.nhsoft.view.R.styleable.Clock_circleColor, android.graphics.Color.BLACK)
            lineWidth1=it.getDimension(com.nhsoft.view.R.styleable.Clock_lineWidth1,20f)
            lineWidth2=it.getDimension(com.nhsoft.view.R.styleable.Clock_lineWidth2,10f)
            lineColor1=it.getColor(com.nhsoft.view.R.styleable.Clock_lineColor1, android.graphics.Color.BLACK)
            lineColor2=it.getColor(com.nhsoft.view.R.styleable.Clock_lineColor2, android.graphics.Color.BLACK)
            hourLineLength=it.getDimension(com.nhsoft.view.R.styleable.Clock_hourLineLength,30f)
            circleStrokeWidth=it.getDimension(com.nhsoft.view.R.styleable.Clock_circleStrokeWidth,20f)
            secondLineLength =it.getDimension(com.nhsoft.view.R.styleable.Clock_secondLineLength,15f)
            secondHandWidth=it.getDimension(com.nhsoft.view.R.styleable.Clock_secondHandWidth,4f)
            minuteHandLength=it.getDimension(com.nhsoft.view.R.styleable.Clock_minuteHandLength,30f)
            secondHandLength=it.getDimension(com.nhsoft.view.R.styleable.Clock_secondHandLength,40f)
            hourHandLenght=it.getDimension(com.nhsoft.view.R.styleable.Clock_hourHandLength,20f)

            paintCircle.isAntiAlias=true
            paintCircle.style= android.graphics.Paint.Style.STROKE
            paintCircle.strokeWidth=circleStrokeWidth
            paintCircle.color=circleColor

            paintHourLine.style= android.graphics.Paint.Style.STROKE
            paintHourLine.color=lineColor1
            paintHourLine.strokeWidth=lineWidth1

            paintSecondLine.style= android.graphics.Paint.Style.STROKE
            paintSecondLine.color=lineColor2
            paintSecondLine.strokeWidth=lineWidth2

            val pathEffect1= android.graphics.DashPathEffect(floatArrayOf(10f, 5f), 4f)
            val pathEffect2= android.graphics.DiscretePathEffect(5f, 10f)
            paintHour.style= android.graphics.Paint.Style.FILL
            paintHour.color=hourHandColor
            paintHour.pathEffect= android.graphics.ComposePathEffect(pathEffect1, pathEffect2)
            paintHour.setShadowLayer(4f,4f,3f, android.graphics.Color.parseColor("#212112"))
            paintHour.strokeWidth=lineWidth1

            paintMinute.style= android.graphics.Paint.Style.FILL
            paintMinute.color=minuteHandColor
            paintMinute.pathEffect= android.graphics.ComposePathEffect(pathEffect1, pathEffect2)
            paintMinute.setShadowLayer(4f,4f,5f, android.graphics.Color.parseColor("#212112"))
            paintMinute.strokeWidth=lineWidth2

            paintSecond.style= android.graphics.Paint.Style.FILL
            paintSecond.color=secondHandColor
            paintSecond.pathEffect= android.graphics.ComposePathEffect(pathEffect1, pathEffect2)
            paintSecond.setShadowLayer(4f,4f,7f, android.graphics.Color.parseColor("#212112"))
            paintSecond.strokeWidth=secondHandWidth
            it.recycle()
        }
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var width:Int
        val widthMode= android.view.View.MeasureSpec.getMode(widthMeasureSpec)
        val widthSize= android.view.View.MeasureSpec.getSize(widthMeasureSpec)
        if(widthMode== android.view.View.MeasureSpec.EXACTLY){
            width=widthSize
        }else{
            width=paddingStart+200+paddingEnd
        }
        setMeasuredDimension(width,width)
        radius=width/2f
    }

    override fun onDraw(canvas: android.graphics.Canvas?) {
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
            android.util.Log.i("time","hour ${getHour()}  angle  $angle")
            it.drawLine(radius,radius,caculateX(angle,hourHandLenght.toInt()),caculateY(angle,hourHandLenght.toInt()),paintHour)
            angle=getMinute()*6
            android.util.Log.i("time","minute ${getMinute()}  angle  $angle")
            it.drawLine(radius,radius,caculateX(angle,minuteHandLength.toInt()),caculateY(angle,minuteHandLength.toInt()),paintMinute)
            angle=getSecond()*6
            android.util.Log.i("time","second ${getSecond()}  angle  $angle")
            it.drawLine(radius,radius,caculateX(angle,secondHandLength.toInt()),caculateY(angle,secondHandLength.toInt()),paintSecond)
            handler.sendEmptyMessageDelayed(0,200)
        }
    }

    private fun getHour():Int{
        val calendar: java.util.Calendar = java.util.Calendar.getInstance()
        return calendar.get(java.util.Calendar.HOUR)
    }
    private fun getMinute():Int{
        val calendar: java.util.Calendar = java.util.Calendar.getInstance()
        return calendar.get(java.util.Calendar.MINUTE)
    }

    private fun getSecond():Int{
        val calendar: java.util.Calendar = java.util.Calendar.getInstance()
        return calendar.get(java.util.Calendar.SECOND)
    }

    private fun caculateX(angle:Int,length:Int):Float{
        val x=radius+length.times(Math.sin(angle.toDouble()*Math.PI/180)).toFloat()
        android.util.Log.i("position","x $x")
        return x
    }
    private fun caculateY(angle:Int,length:Int):Float{
        val y=radius-length.times(Math.cos(angle.toDouble()*Math.PI/180)).toFloat()
        android.util.Log.i("position","y $y")
        return y
    }
}




