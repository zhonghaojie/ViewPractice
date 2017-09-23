package com.nhsoft.view

import android.animation.Keyframe
import android.animation.ObjectAnimator
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.view.animation.FastOutSlowInInterpolator
import android.view.animation.BounceInterpolator
import android.view.animation.LinearInterpolator
import android.widget.SeekBar
import com.nhsoft.view.widget.ProgressView
import kotlinx.android.synthetic.main.activity_fifth.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class FifthActivity : AppCompatActivity() {

    lateinit var view: ProgressView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fifth)
        btn_start.setOnClickListener {
            download()
        }
    }

    private fun startAnim(start: Int, end: Int) {
        val anim = ObjectAnimator.ofInt(progress_view, "progress", start, end)
        anim.duration = 500
        anim.interpolator = LinearInterpolator()
        anim.start()
    }

    private fun download() {
        var progress = 0
        doAsync {
            while (progress <= 100) {
                Thread.sleep(300)
                progress += 1
                uiThread {
                    ObjectAnimator.ofInt(progress_view, "progress", progress-1, progress).start()
                }
            }
        }
    }
}
