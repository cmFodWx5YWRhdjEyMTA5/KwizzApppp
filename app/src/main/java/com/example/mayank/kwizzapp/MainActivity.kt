package com.example.mayank.kwizzapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.google.android.gms.auth.api.signin.GoogleSignIn
import android.net.Uri
import com.example.mayank.kwizzapp.dashboard.DashboardFragment
import com.example.mayank.kwizzapp.login.LoginFragment
import com.example.mayank.kwizzapp.userInfo.UserInfoFragment
import net.rmitsolutions.mfexpert.lms.helpers.switchToFragment


class MainActivity : AppCompatActivity(), LoginFragment.OnFragmentInteractionListener,
UserInfoFragment.OnFragmentInteractionListener, DashboardFragment.OnFragmentInteractionListener{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val loginFragment = LoginFragment()
        switchToFragment(loginFragment)

    }

    override fun onFragmentInteraction(uri: Uri) {

    }

}
