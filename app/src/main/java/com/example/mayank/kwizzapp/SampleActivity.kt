package com.example.mayank.kwizzapp

import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.widget.Button
import com.example.mayank.kwizzapp.dependency.components.DaggerInjectActivityComponent
import com.example.mayank.kwizzapp.helpers.processRequest
import com.example.mayank.kwizzapp.network.IQuestion
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.games.Games
import com.google.android.gms.games.LeaderboardsClient
import com.google.android.gms.games.leaderboard.Leaderboard
import com.google.android.gms.games.leaderboard.LeaderboardScore
import com.google.android.gms.games.leaderboard.ScoreSubmissionData
import io.reactivex.disposables.CompositeDisposable
import net.rmitsolutions.mfexpert.lms.helpers.logD
import org.jetbrains.anko.find
import javax.inject.Inject

class SampleActivity : AppCompatActivity() {


    @Inject
    lateinit var questionService: IQuestion

    private lateinit var compositeDisposable: CompositeDisposable
    private lateinit var postScore : Button
    private lateinit var inputPoints : TextInputEditText
    private lateinit var leaderboards : Button

    val id = intArrayOf(2,4,3,1,5,9,8,7,6,10)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sample)
        val gameClient = Games.getGamesClient(this, GoogleSignIn.getLastSignedInAccount(this)!!)
        gameClient.setViewForPopups(this.find(android.R.id.content))

        val depComponent = DaggerInjectActivityComponent.builder()
                .applicationComponent(KwizzApp.applicationComponent)
                .build()
        depComponent.injectSampleActivity(this)

        compositeDisposable = CompositeDisposable()

        postScore = find(R.id.postScore)
        inputPoints = find(R.id.inputPoints)
        leaderboards = find(R.id.leaderboards)

        leaderboards.setOnClickListener{
//            for (quesId in id){
//                compositeDisposable.add(questionService.getQuestion(quesId.toString(),"apt_ques")
//                        .processRequest(
//                                { question ->
//                                    logD("Question - ${question.question}")
//                                },
//                                { error ->
//                                    logD("Error - $error")
//                                }
//                        ))
//            }
            showLeaderBoards()
        }

        postScore.setOnClickListener {
            val points = inputPoints.text.toString().toLong()

//            Games.getLeaderboardsClient(this, GoogleSignIn.getLastSignedInAccount(this)!!)
//                    .submitScore(getString(R.string.leaderboard_kwizz_toppers), points)

            //Games.getLeaderboardsClient(this, GoogleSignIn.getLastSignedInAccount(this)!!).submitScore(getString(R.string.leaderboard_top_scores), points)

            Games.getLeaderboardsClient(this, GoogleSignIn.getLastSignedInAccount(this)!!)
                    .submitScoreImmediate(getString(R.string.leaderboard_top_scores), points).addOnFailureListener { error ->
                        logD("Error - ${error.message}")
                    }.addOnSuccessListener { scores ->
                        logD("${scores.playerId} ${scores.getScoreResult(0)}")
                    }

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
