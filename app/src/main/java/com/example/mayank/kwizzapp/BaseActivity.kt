package com.example.mayank.kwizzapp

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

class BaseActivity : AppCompatActivity() {

    companion object {
        private var backPressedTime: Long = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
}