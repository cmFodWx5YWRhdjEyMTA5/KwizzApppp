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
import com.example.mayank.kwizzapp.network.IUser
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
            //showLeaderBoards()

            val postData = JSONObject()

            val user = User()
            user.tableName = "users"
            user.mobileNumber = "1234567890"
            user.name = "Priyank Sharma"
            user.email = "testmail@gmail.com"
            user.password = "alchemy@123"
//            val jsonObject = JSONObject()
//            jsonObject.put("tableName", user.tableName)
//            jsonObject.put("name", user.name)
//            jsonObject.put("mobileNumber", user.mobile_number)
//            jsonObject.put("email", user.email)


            compositeDisposable.add(userService.insertUser(user)
                    .processRequest(
                            { response ->
                                if (response.isSuccess){
                                    toast(response.message)
                                    logD(response.message)
                                }else{
                                    logD("Message -" +response.message)
                                    toast(response.message)
                                    logD("Data - "+response.data)
                                }
                            },
                            {error ->
                                logD("Error - $error")

                            }
                    ))
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
