package com.example.mayank.kwizzapp

import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.mayank.kwizzapp.dialog.ShowDialog
import net.rmitsolutions.mfexpert.lms.helpers.logD
import net.rmitsolutions.mfexpert.lms.helpers.showDialog
import org.jetbrains.anko.find

class SampleActivity : AppCompatActivity() {


    private lateinit var dialog: ShowDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.setting_menu_row)

//        dialog = ShowDialog()
//
//        find<Button>(R.id.buttonPaySample).setOnClickListener {
//            logD("SDK Int - ${Build.VERSION.SDK_INT} = ${Build.VERSION_CODES.N_MR1}")
//            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N_MR1){
//                dialog.showResultDialog(this, "Test Title","Small Title Two","This is a Sample message",R.mipmap.ic_done)
//            }else{
//                showDialog(this, "Sample Title", "Sample Message")
//            }
//
////
//        }
    }
}
