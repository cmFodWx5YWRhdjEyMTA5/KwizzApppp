package com.example.mayank.kwizzapp

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.net.Uri
import android.support.v7.widget.Toolbar
import com.example.mayank.kwizzapp.dashboard.DashboardFragment
import com.example.mayank.kwizzapp.gamedetail.GameDetailFragment
import com.example.mayank.kwizzapp.gamemenu.GameMenuFragment
import com.example.mayank.kwizzapp.gameresult.GameResultFragment
import com.example.mayank.kwizzapp.libgame.LibPlayGame
import com.example.mayank.kwizzapp.login.LoginFragment
import com.example.mayank.kwizzapp.quiz.QuizFragment
import com.example.mayank.kwizzapp.userInfo.UserInfoFragment
import net.rmitsolutions.mfexpert.lms.helpers.switchToFragment
import org.jetbrains.anko.find
import com.example.mayank.kwizzapp.singleplay.SinglePlayDetails
import com.example.mayank.kwizzapp.singleplay.SinglePlayQuizFragment
import com.example.mayank.kwizzapp.singleplay.SinglePlayResultFragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import org.jetbrains.anko.startActivity


class MainActivity : AppCompatActivity(), LoginFragment.OnFragmentInteractionListener,
        UserInfoFragment.OnFragmentInteractionListener, DashboardFragment.OnFragmentInteractionListener,
        GameMenuFragment.OnFragmentInteractionListener,
        GameDetailFragment.OnFragmentInteractionListener, QuizFragment.OnFragmentInteractionListener,
        GameResultFragment.OnFragmentInteractionListener, SinglePlayDetails.OnFragmentInteractionListener,
        SinglePlayQuizFragment.OnFragmentInteractionListener, SinglePlayResultFragment.OnFragmentInteractionListener {

    private lateinit var toolBar: Toolbar
    private var libPlayGame: LibPlayGame? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolBar = find(R.id.toolbar)
        setSupportActionBar(toolBar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        if (isSignedIn()) {
            val dashboardFragment = DashboardFragment()
            switchToFragment(dashboardFragment)
        } else {
            val loginFragment = LoginFragment()
            switchToFragment(loginFragment)
        }
    }

    private fun isSignedIn(): Boolean {
        return GoogleSignIn.getLastSignedInAccount(this) != null
    }

    override fun onFragmentInteraction(uri: Uri) {

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        libPlayGame = LibPlayGame(this)
        libPlayGame?.onActivityResult(requestCode, resultCode, data)

    }


    override fun onBackPressed() {
        val count = supportFragmentManager.backStackEntryCount
        if (count >= 1) {
            AlertDialog.Builder(this).setMessage("Do you really want to leave the game?").setPositiveButton("Ok") { dialog, which ->
                super.onBackPressed()
                startActivity<MainActivity>()
                libPlayGame?.leaveRoom()
                finish()
                dialog.dismiss()
            }.setNegativeButton("Cancel") { dialog, which ->
                dialog.dismiss()
            }.show()
        } else {
            AlertDialog.Builder(this).setMessage("Do you really want to Exit?").setPositiveButton("Ok") { dialog, which ->
                super.onBackPressed()
                finish()
                dialog.dismiss()
            }.setNegativeButton("Cancel") { dialog, which ->
                dialog.dismiss()
            }.show()
        }


    }

}
