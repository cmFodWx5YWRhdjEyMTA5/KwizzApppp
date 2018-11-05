package com.example.mayank.kwizzapp

import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.widget.Button
import com.example.mayank.kwizzapp.dialog.ShowDialog
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.games.Games
import net.rmitsolutions.mfexpert.lms.helpers.logD
import net.rmitsolutions.mfexpert.lms.helpers.showDialog
import org.jetbrains.anko.find

class SampleActivity : AppCompatActivity() {


    private lateinit var postScore : Button
    private lateinit var inputPoints : TextInputEditText
    private lateinit var leaderboards : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sample)
        val gameClient = Games.getGamesClient(this, GoogleSignIn.getLastSignedInAccount(this)!!)
        gameClient.setViewForPopups(this.find(android.R.id.content))

        postScore = find(R.id.postScore)
        inputPoints = find(R.id.inputPoints)
        leaderboards = find(R.id.leaderboards)

        leaderboards.setOnClickListener{
            showLeaderBoards()
        }

        postScore.setOnClickListener {
            val points = inputPoints.text.toString().toLong()

//            Games.getLeaderboardsClient(this, GoogleSignIn.getLastSignedInAccount(this)!!)
//                    .submitScore(getString(R.string.leaderboard_kwizz_toppers), points)

            Games.getLeaderboardsClient(this, GoogleSignIn.getLastSignedInAccount(this)!!).submitScoreImmediate(getString(R.string.leaderboard_top_scores), points)

            logD("Score posted")

        }
    }

    private fun showLeaderBoards() {
        Games.getLeaderboardsClient(this, GoogleSignIn.getLastSignedInAccount(this)!!)
                .getLeaderboardIntent(getString(R.string.leaderboard_top_scores))
                .addOnSuccessListener { intent -> startActivityForResult(intent, Constants.RC_LEADERBOARD_UI) }.addOnFailureListener{ e->
                    logD("Error = $e")
                }
    }
}
