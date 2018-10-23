package com.example.mayank.kwizzapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.mayank.kwizzapp.dialog.ShowDialog

class SampleActivity : AppCompatActivity() {


    private lateinit var dialog: ShowDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sample)

        dialog = ShowDialog()

        //dialog.showResultDialog(this, true)

    }
}
