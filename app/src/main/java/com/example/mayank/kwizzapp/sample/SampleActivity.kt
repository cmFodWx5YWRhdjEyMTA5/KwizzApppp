package com.example.mayank.kwizzapp.sample

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.widget.Button
import com.example.mayank.kwizzapp.Constants
import com.example.mayank.kwizzapp.KwizzApp
import com.example.mayank.kwizzapp.R
import com.example.mayank.kwizzapp.dependency.components.DaggerInjectActivityComponent
import com.example.mayank.kwizzapp.helpers.JsonHelper
import com.example.mayank.kwizzapp.helpers.processRequest
import com.example.mayank.kwizzapp.network.IPaytm
import com.example.mayank.kwizzapp.network.IUser
import com.example.mayank.kwizzapp.paytm.Paytm
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.games.Games
import io.reactivex.disposables.CompositeDisposable
import net.rmitsolutions.mfexpert.lms.helpers.logD
import org.jetbrains.anko.find
import org.jetbrains.anko.toast
import javax.inject.Inject
import org.json.JSONObject


class SampleActivity : AppCompatActivity() {


    @Inject
    lateinit var userService: IUser

    @Inject
    lateinit var payTmService: IPaytm

    private lateinit var compositeDisposable: CompositeDisposable
    private lateinit var postScore: Button
    private lateinit var inputPoints: TextInputEditText
    private lateinit var leaderboards: Button

    val id = intArrayOf(2, 4, 3, 1, 5, 9, 8, 7, 6, 10)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sample)
//        val gameClient = Games.getGamesClient(this, GoogleSignIn.getLastSignedInAccount(this)!!)
//        gameClient.setViewForPopups(this.find(android.R.id.content))

        val depComponent = DaggerInjectActivityComponent.builder()
                .applicationComponent(KwizzApp.applicationComponent)
                .build()
        depComponent.injectSampleActivity(this)

        compositeDisposable = CompositeDisposable()


        postScore = find(R.id.postScore)

        postScore.setOnClickListener {
            logD("Button Clicked")
            val transfer = Paytm.RequestTransferPointsToUser()
            transfer.merchantOrderId = "kwizz${System.currentTimeMillis()}"
            transfer.payeePhoneNumber = "7777777777"
            transfer.amount = 1.0
            compositeDisposable.add(payTmService.transferPointsToUser(transfer)
                    .processRequest(
                            { response ->

                                logD("Status - ${response.status}")
                                logD("${response.statusMessage}")
                                logD("Request GUID ${response.requestGuid}")
                                val res = response.response
                                logD("Wallet Transaction Id - ${res.walletSysTransactionId}")

                            },
                            { err ->
                                logD(err)

                            }
                    ))
        }
//        inputPoints = find(R.id.inputPoints)
//        leaderboards = find(R.id.leaderboards)

//        leaderboards.setOnClickListener{
//            showLeaderBoards()
//        }

//        postScore.setOnClickListener {
//            var points = inputPoints.text.toString().toLong()
//            points += points * 1000000
//
////            Games.getLeaderboardsClient(this, GoogleSignIn.getLastSignedInAccount(this)!!)
////                    .submitScore(getString(R.string.leaderboard_kwizz_toppers), points)
//
//            //Games.getLeaderboardsClient(this, GoogleSignIn.getLastSignedInAccount(this)!!).submitScore(getString(R.string.leaderboard_top_scores), points)
//
//            Games.getLeaderboardsClient(this, GoogleSignIn.getLastSignedInAccount(this)!!)
//                    .submitScoreImmediate(getString(R.string.leaderboard_top_scores), points).addOnFailureListener { error ->
//                        logD("Error - ${error.message}")
//                    }.addOnSuccessListener { scores ->
//                        logD("${scores.playerId} ${scores.getScoreResult(0)}")
//                    }
//
//            logD("Score posted")

//        }
    }

    private fun showLeaderBoards() {
        Games.getLeaderboardsClient(this, GoogleSignIn.getLastSignedInAccount(this)!!)
                .getLeaderboardIntent(getString(R.string.leaderboard_top_scores))
                .addOnSuccessListener { intent -> startActivityForResult(intent, Constants.RC_LEADERBOARD_UI) }.addOnFailureListener { e ->
                    logD("Error = $e")
                }
    }
}
