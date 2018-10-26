package com.example.mayank.kwizzapp.settings

import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.mayank.kwizzapp.R
import com.example.mayank.kwizzapp.settings.menusettings.SettingMenuFragment
import net.rmitsolutions.mfexpert.lms.helpers.switchToFragment

class SettingsActivity : AppCompatActivity(), SettingMenuFragment.OnFragmentInteractionListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val menuFragment = SettingMenuFragment()
        switchToFragment(menuFragment)
    }

    override fun onFragmentInteraction(uri: Uri) {

    }
}
