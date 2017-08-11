package com.nhsoft.view

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.SeekBar
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_forth.*

class ForthActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forth)
        seekbar.progress=0
        seekbar.max=100
        seekbar.setOnSeekBarChangeListener(object :SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if(progress==0){
                    Toast.makeText(this@ForthActivity,"已经最小了", Toast.LENGTH_SHORT).show()
                }
                if(progress==100){
                    Toast.makeText(this@ForthActivity,"已经最大了", Toast.LENGTH_SHORT).show()
                }
                voice.progress=progress
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })
    }


}
