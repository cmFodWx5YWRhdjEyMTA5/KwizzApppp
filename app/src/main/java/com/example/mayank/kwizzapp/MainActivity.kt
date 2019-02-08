package com.example.mayank.kwizzapp

import android.app.AlertDialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.net.Uri
import android.os.AsyncTask
import android.support.v7.widget.Toolbar
import android.util.Log
import android.widget.Toast
import com.example.mayank.kwizzapp.dashboard.DashboardFragment
import com.example.mayank.kwizzapp.dependency.components.DaggerInjectActivityComponent
import com.example.mayank.kwizzapp.gamedetail.GameDetailFragment
import com.example.mayank.kwizzapp.gamemenu.GameMenuFragment
import com.example.mayank.kwizzapp.gameresult.GameResultFragment
import com.example.mayank.kwizzapp.helpers.processRequest
import com.example.mayank.kwizzapp.libgame.LibPlayGame
import com.example.mayank.kwizzapp.login.LoginFragment
import com.example.mayank.kwizzapp.network.IUser
import com.example.mayank.kwizzapp.quiz.QuizFragment
import com.example.mayank.kwizzapp.userInfo.UserInfoFragment
import org.jetbrains.anko.find
import com.example.mayank.kwizzapp.singleplay.SinglePlayDetails
import com.example.mayank.kwizzapp.singleplay.SinglePlayQuizFragment
import com.example.mayank.kwizzapp.singleplay.SinglePlayResultFragment
import com.example.mayank.kwizzapp.viewmodels.Users
import com.example.mayank.kwizzapp.wallet.WalletActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.games.Games
import io.reactivex.disposables.CompositeDisposable
import net.rmitsolutions.mfexpert.lms.helpers.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import javax.inject.Inject
import org.jsoup.Jsoup
import android.os.AsyncTask.execute
import android.view.Window
import java.util.concurrent.ExecutionException


class MainActivity : AppCompatActivity(), LoginFragment.OnFragmentInteractionListener,
        UserInfoFragment.OnFragmentInteractionListener, DashboardFragment.OnFragmentInteractionListener,
        GameMenuFragment.OnFragmentInteractionListener,
        GameDetailFragment.OnFragmentInteractionListener, QuizFragment.OnFragmentInteractionListener,
        GameResultFragment.OnFragmentInteractionListener, SinglePlayDetails.OnFragmentInteractionListener,
        SinglePlayQuizFragment.OnFragmentInteractionListener, SinglePlayResultFragment.OnFragmentInteractionListener {

    @Inject
    lateinit var userService: IUser
    private lateinit var toolBar: Toolbar
    private var libPlayGame: LibPlayGame? = null
    private lateinit var compositeDisposable: CompositeDisposable
    private var back_pressed = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        window.requestFeature(Window.FEATURE_CONTENT_TRANSITIONS)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolBar = find(R.id.toolbar)
        setSupportActionBar(toolBar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val depComponent = DaggerInjectActivityComponent.builder()
                .applicationComponent(KwizzApp.applicationComponent)
                .build()
        depComponent.injectMainActivity(this)

        compositeDisposable = CompositeDisposable()

        if (isSignedIn()) {
            val playersClient = Games.getPlayersClient(this, GoogleSignIn.getLastSignedInAccount(this)!!)
            playersClient.currentPlayer.addOnSuccessListener { player ->
                if (player.displayName == getPref(SharedPrefKeys.DISPLAY_NAME, "")) {
                    val dashboardFragment = DashboardFragment()
                    switchToFragment(dashboardFragment)
                    //getOnlineVersion()
                } else {
                    val updateDisplayName = Users.UpdateDisplayName()
                    updateDisplayName.displayName = player.displayName
                    updateDisplayName.mobileNumber = getPref(SharedPrefKeys.MOBILE_NUMBER, "")
                    updateDisplayName(updateDisplayName)
                }
            }
        } else {
            val loginFragment = LoginFragment()
            switchToFragment(loginFragment)
        }
    }

    private fun getOnlineVersion() {
        someTask().execute()
    }

    class someTask() : AsyncTask<Void, Void, String>() {
        override fun doInBackground(vararg params: Void?): String? {
            // ...
            val newVersion = Jsoup.connect("https://play.google.com/store/apps/details?id=" + "com.kwizzapp" + "&hl=it")
                    .timeout(30000)
                    .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                    .referrer("http://www.google.com")
                    .get()
                    .select(".hAyfc .htlgb")
                    .get(5)
                    .ownText()

            return newVersion
        }

        override fun onPreExecute() {
            super.onPreExecute()
            // ...
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            Log.d("Async Task","New Version - $result")
            // ...
        }
    }


    private fun updateDisplayName(updateDisplayName: Users.UpdateDisplayName) {
        if (!isNetConnected()) {
            toast("No Internet!")
            return
        }
        showProgress()
        compositeDisposable.add(userService.updateDisplayName(updateDisplayName)
                .processRequest(
                        { response ->
                            hideProgress()
                            if (response.isSuccess) {
                                putPref(SharedPrefKeys.DISPLAY_NAME, updateDisplayName.displayName)
                                val dashboardFragment = DashboardFragment()
                                switchToFragment(dashboardFragment)
                            } else {
                                showDialog(this, "Error", response.message)
                            }
                        },
                        { err ->
                            hideProgress()
                            showDialog(this, "Error", err.toString())
                        }
                ))

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
        logD("Count - $count")
        when (count) {
            0 -> {
                when {
                    back_pressed + 2000 > System.currentTimeMillis() -> super.onBackPressed()
                    else -> {
                        Toast.makeText(baseContext, "Press again to exit", Toast.LENGTH_SHORT).show()
                        back_pressed = System.currentTimeMillis()
                    }
                }
            }
            1 -> supportFragmentManager.popBackStack()
            2 -> {
                val message = "Do you want to left the room ?"
                showAlert(message)
            }
            3 -> {
                val message = "If you left the game your bid points will be deducted.\nDo you want to leave the game?"
                showAlert(message)
            }
            else -> {
//                startActivity<MainActivity>()
//                finish()
                libPlayGame?.leaveRoom()
            }
        }
    }

    private fun showAlert(message: String) {
        AlertDialog.Builder(this).setMessage(message).setPositiveButton("Ok") { dialog, which ->
            super.onBackPressed()
            startActivity<MainActivity>()
            libPlayGame?.leaveRoom()
            finish()
            dialog.dismiss()
        }.setNegativeButton("Cancel") { dialog, which ->
            dialog.dismiss()
        }.show()
    }

}
