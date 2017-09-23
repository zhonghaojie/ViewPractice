package com.nhsoft.view

import android.animation.ObjectAnimator
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.animation.BounceInterpolator
import android.widget.SeekBar
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_forth.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.startActivity

class FourthActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forth)

        seekbar.setOnSeekBarChangeListener(object :SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if(progress==0){
                    Toast.makeText(this@FourthActivity,"已经最小了", Toast.LENGTH_SHORT).show()
                }
                if(progress==seekBar?.max){
                    Toast.makeText(this@FourthActivity,"已经最大了", Toast.LENGTH_SHORT).show()
                }
                voice.progress=progress.toFloat()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })
       btn_next.setOnClickListener {
           startActivity<FifthActivity>()
       }
        btn_start_voice_anim.setOnClickListener {
            objectAnimator()
        }

    }

    private fun objectAnimator(){
        val anim=ObjectAnimator.ofFloat(voice,"progress",0f,100f)
        anim.duration=5000
        anim.interpolator= BounceInterpolator()
        anim.start()
    }




}
