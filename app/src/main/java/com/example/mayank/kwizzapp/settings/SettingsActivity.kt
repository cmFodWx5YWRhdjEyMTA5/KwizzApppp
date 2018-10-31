package com.example.mayank.kwizzapp.settings

import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import com.example.mayank.kwizzapp.R
import com.example.mayank.kwizzapp.bankdetail.BankDetailFragment
import com.example.mayank.kwizzapp.bankdetail.EditBankDetailsFragment
import com.example.mayank.kwizzapp.policies.PoliciesFragment
import com.example.mayank.kwizzapp.profile.EditProfileFragment
import com.example.mayank.kwizzapp.profile.ProfileFragment
import com.example.mayank.kwizzapp.settings.menusettings.SettingMenuFragment
import net.rmitsolutions.mfexpert.lms.helpers.switchToFragment
import org.jetbrains.anko.find

class SettingsActivity : AppCompatActivity(), SettingMenuFragment.OnFragmentInteractionListener,
        ProfileFragment.OnFragmentInteractionListener, EditProfileFragment.OnFragmentInteractionListener,
        BankDetailFragment.OnFragmentInteractionListener, EditBankDetailsFragment.OnFragmentInteractionListener,
        PoliciesFragment.OnFragmentInteractionListener {

    private lateinit var toolBar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        toolBar = find(R.id.toolbar)
        setSupportActionBar(toolBar)

        val menuFragment = SettingMenuFragment()
        switchToFragment(menuFragment)
    }

    override fun onFragmentInteraction(uri: Uri) {

    }
}
