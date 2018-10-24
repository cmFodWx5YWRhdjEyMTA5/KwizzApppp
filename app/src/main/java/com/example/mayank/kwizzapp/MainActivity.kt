package com.example.mayank.kwizzapp

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.net.Uri
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import com.example.mayank.kwizzapp.dashboard.DashboardFragment
import com.example.mayank.kwizzapp.gamedetail.GameDetailFragment
import com.example.mayank.kwizzapp.gamemenu.GameMenuFragment
import com.example.mayank.kwizzapp.gameresult.GameResultFragment
import com.example.mayank.kwizzapp.libgame.LibPlayGame
import com.example.mayank.kwizzapp.login.LoginFragment
import com.example.mayank.kwizzapp.quiz.QuizFragment
import com.example.mayank.kwizzapp.userInfo.UserInfoFragment
import net.rmitsolutions.mfexpert.lms.helpers.showDialog
import net.rmitsolutions.mfexpert.lms.helpers.switchToFragment
import org.jetbrains.anko.find
import org.jetbrains.anko.toast
import android.support.v7.app.AppCompatDelegate




class MainActivity : AppCompatActivity(), LoginFragment.OnFragmentInteractionListener,
UserInfoFragment.OnFragmentInteractionListener, DashboardFragment.OnFragmentInteractionListener,
GameMenuFragment.OnFragmentInteractionListener, GameDetailFragment.OnFragmentInteractionListener,
QuizFragment.OnFragmentInteractionListener, GameResultFragment.OnFragmentInteractionListener{

    private lateinit var libPlayGame : LibPlayGame
    private lateinit var toolBar : Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolBar = find(R.id.toolbar)
        setSupportActionBar(toolBar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val loginFragment = LoginFragment()
        switchToFragment(loginFragment)
    }

    override fun onFragmentInteraction(uri: Uri) {

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode== Activity.RESULT_OK){
            libPlayGame = LibPlayGame(this)
            libPlayGame.onActivityResult(requestCode, resultCode, data)
        }
    }

}
