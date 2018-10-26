package com.example.mayank.kwizzapp.settings

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.mayank.kwizzapp.R
import com.example.mayank.kwizzapp.settings.menusettings.SettingMenuFragment
import net.rmitsolutions.mfexpert.lms.helpers.switchToFragment

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val menuFragment = SettingMenuFragment()
        switchToFragment(menuFragment)
    }
}
