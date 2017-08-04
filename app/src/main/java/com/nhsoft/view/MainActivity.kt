package com.nhsoft.view

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.startActivity

class MainActivity : AppCompatActivity() {


    var pro:Int=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Thread(Runnable {
            while (true){
                pro++
                progress.setProgressValue(pro)
                Thread.sleep(200)
            }
        }).start()
        next.setOnClickListener {
            startActivity<SecondActivity>()
        }
    }
}
