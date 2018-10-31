package com.example.mayank.kwizzapp.play

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import com.example.mayank.kwizzapp.R
import com.example.mayank.kwizzapp.gamedetail.GameDetailFragment
import com.example.mayank.kwizzapp.gamemenu.GameMenuFragment
import com.example.mayank.kwizzapp.gameresult.GameResultFragment
import com.example.mayank.kwizzapp.libgame.LibPlayGame
import com.example.mayank.kwizzapp.quiz.QuizFragment
import com.example.mayank.kwizzapp.singleplay.SinglePlayDetails
import com.example.mayank.kwizzapp.singleplay.SinglePlayQuizFragment
import com.example.mayank.kwizzapp.singleplay.SinglePlayResultFragment
import net.rmitsolutions.mfexpert.lms.helpers.switchToFragment
import org.jetbrains.anko.find

class PlayActivity : AppCompatActivity(), GameMenuFragment.OnFragmentInteractionListener,
        GameDetailFragment.OnFragmentInteractionListener, QuizFragment.OnFragmentInteractionListener,
        GameResultFragment.OnFragmentInteractionListener, SinglePlayDetails.OnFragmentInteractionListener,
        SinglePlayQuizFragment.OnFragmentInteractionListener , SinglePlayResultFragment.OnFragmentInteractionListener {

    private lateinit var toolBar : Toolbar
    private lateinit var libPlayGame : LibPlayGame

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play)

        toolBar = find(R.id.toolbar)
        setSupportActionBar(toolBar)

        val gameMenuFragment = GameMenuFragment()
        switchToFragment(gameMenuFragment)
    }

    override fun onBackPressed() {
        AlertDialog.Builder(this).setMessage("Do you want to leave the game ?").setPositiveButton("Ok") { dialog, which ->
            super.onBackPressed()
            finish()
            dialog.dismiss()
        }.setNegativeButton("Cancel"){ dialog, which ->
            dialog.dismiss()
        }.show()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode== Activity.RESULT_OK){
            libPlayGame = LibPlayGame(this)
            libPlayGame.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onFragmentInteraction(uri: Uri) {

    }
}
