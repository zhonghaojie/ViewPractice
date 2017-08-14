package com.nhsoft.view

import android.app.Dialog
import android.content.Context
import android.media.AudioManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.KeyEvent
import android.view.LayoutInflater
import android.widget.SeekBar
import android.widget.Toast
import com.nhsoft.view.widget.VoiceView
import kotlinx.android.synthetic.main.activity_forth.*

class FourthActivity : AppCompatActivity() {

    lateinit var am:AudioManager
    var currentStreamType=AudioManager.STREAM_RING
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forth)

        am=getSystemService(Context.AUDIO_SERVICE) as AudioManager
        if(am.isMusicActive){
            currentStreamType=AudioManager.STREAM_MUSIC
            seekbar.max=am.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        }else{
            currentStreamType=AudioManager.STREAM_RING
            seekbar.max=am.getStreamMaxVolume(AudioManager.STREAM_RING)
        }
        seekbar.progress=am.getStreamVolume(currentStreamType)
        seekbar.setOnSeekBarChangeListener(object :SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if(progress==0){
                    Toast.makeText(this@FourthActivity,"已经最小了", Toast.LENGTH_SHORT).show()
                }
                if(progress==seekBar?.max){
                    Toast.makeText(this@FourthActivity,"已经最大了", Toast.LENGTH_SHORT).show()
                }
                voice.progress=progress
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        when(keyCode){
            KeyEvent.KEYCODE_VOLUME_DOWN->{
                showVoiceDialog()
            }
            KeyEvent.KEYCODE_VOLUME_UP->{
                showVoiceDialog()
            }
        }
        return super.onKeyDown(keyCode, event)
    }
    var dialog:Dialog?=null
    fun showVoiceDialog(){
        if(dialog!=null){
            dialog?.show()
        }else{
            val dialog=Dialog(this)
            val view=LayoutInflater.from(this).inflate(R.layout.voice,null,false)
            val voice:VoiceView=view.findViewById(R.id.voice) as VoiceView
            dialog.window.setBackgroundDrawableResource(android.R.color.transparent)
            dialog.setContentView(view)
            dialog.show()
        }
    }

}
