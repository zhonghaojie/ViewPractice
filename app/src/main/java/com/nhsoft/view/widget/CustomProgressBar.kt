package com.nhsoft.view.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import com.nhsoft.view.R



/**
 * Created by zhonghaojie on 2017-08-10.
 */
class CustomProgressBar(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : View(context, attrs, defStyleAttr, defStyleRes) {
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int):this(context,attrs,defStyleAttr,0)
    constructor(context: Context?):this(context,null,0,0)


    var paint=Paint()
    var firstColor:Int=Color.BLACK
    var secondColor:Int=Color.BLACK
    var circleWidth:Float=40f
    var speed:Int=500
    var progress:Float=0f
    var mode:Boolean=false
    constructor(context: Context?, attrs: AttributeSet?):this(context,attrs,0,0){
        paint.isAntiAlias=true
        val typedArray=context?.obtainStyledAttributes(attrs, R.styleable.CustomProgressBar)
        typedArray?.let {
            firstColor=it.getColor(R.styleable.CustomProgressBar_firstColor,Color.BLACK)
            secondColor=it.getColor(R.styleable.CustomProgressBar_secondColor,Color.BLACK)
            circleWidth=it.getDimension(R.styleable.CustomProgressBar_circleWidth,40f)
            speed=it.getInt(R.styleable.CustomProgressBar_speed,10
            )
            it.recycle()
        }
        Thread(Runnable {
            while (true){
                progress++
                if(progress>=360f){
                    progress=0f
                    mode=!mode
                }

                postInvalidate()
                Thread.sleep(speed.toLong())
            }
        }).start()

    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        val centerX=measuredWidth/2f
        val r=centerX-circleWidth
        paint.style=Paint.Style.STROKE
        paint.strokeWidth=circleWidth

        val oval = RectF(centerX - r, centerX - r, centerX + r, centerX + r)
        canvas?.let {

            if(mode){
                paint.color=firstColor
                it.drawCircle(centerX,centerX,r,paint)
                paint.color=secondColor
            }else{
                paint.color=secondColor
                it.drawCircle(centerX,centerX,r,paint)
                paint.color=firstColor
            }
            it.drawArc(oval,-90f,progress,false,paint)
        }
    }
}