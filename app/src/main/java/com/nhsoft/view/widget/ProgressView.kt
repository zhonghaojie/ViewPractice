package com.nhsoft.view.widget

import android.app.Notification
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import com.nhsoft.view.R
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.info

/**
 * Created by zhonghaojie on 2017/9/23.
 */
class ProgressView(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int):View(context, attrs, defStyleAttr, defStyleRes) {


    constructor(context:Context?, attrs:AttributeSet?, defStyleAttr: Int):this(context,attrs,defStyleAttr,0)
    constructor(context:Context?):this(context,null,0,0)

    var strokeColor:Int=Color.parseColor("#E91E63")
    var txtColor:Int=Color.WHITE
    var circleStrokeWidth:Float=20f
    var txtSize:Float=22f
    var radius:Float=70f
    val paint=Paint(Paint.ANTI_ALIAS_FLAG)
    val rectF=RectF()
    var progress=0
    set(value) {
        if(value in 0..100){
            field=value
            AnkoLogger<String>().info { "$value" }
            postInvalidate()
        }

    }
    constructor(context:Context?, attrs: AttributeSet?):this(context,attrs,0,0){
        val typedArray=context?.obtainStyledAttributes(attrs, R.styleable.ProgressView)
        typedArray?.let {
            strokeColor=it.getColor(R.styleable.ProgressView_strokeColor,Color.parseColor("#E91E63"))
            txtColor=it.getColor(R.styleable.ProgressView_txtColor,Color.WHITE)
            circleStrokeWidth=it.getDimension(R.styleable.ProgressView_circleStrokeWidth,20f)
            txtSize=it.getDimension(R.styleable.ProgressView_txtSize,20f)
            radius=it.getDimension(R.styleable.ProgressView_radius,30f)
            it.recycle()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width=2*(radius+circleStrokeWidth+40).toInt()
        rectF.set(40f, 40f, width-40f, width-40f)
        setMeasuredDimension(width,width)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val centerX=width/2f
        val centerY=height/2f
        //画圆弧
        paint.color=strokeColor
        paint.style=Paint.Style.STROKE
        paint.strokeWidth=20f
        paint.strokeCap=Paint.Cap.ROUND
        paint.alpha=(255*(progress.toFloat()/100)).toInt()


        AnkoLogger<RectF>().info { "${rectF.left}     ${rectF.top}    ${rectF.right}      ${rectF.bottom}" }
        canvas?.drawArc(rectF,135f,progress*2.7f,false,paint)

        paint.color=txtColor
        paint.style=Paint.Style.FILL
        paint.textSize=txtSize
        paint.textAlign= Paint.Align.CENTER
        canvas?.drawText(progress.toString()+"%",centerX,centerY-(paint.ascent()+paint.descent())/2,paint)


    }
}