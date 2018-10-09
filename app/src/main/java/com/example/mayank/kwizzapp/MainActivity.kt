package com.example.mayank.kwizzapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import android.support.annotation.NonNull
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.View
import android.widget.Button
import com.example.mayank.kwizzapp.login.LoginFragment
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInResult
import com.google.android.gms.common.SignInButton
import com.google.android.gms.games.Games.signOut
import net.rmitsolutions.mfexpert.lms.helpers.switchToFragment


class MainActivity : AppCompatActivity(), LoginFragment.OnFragmentInteractionListener {

    private val TAG = MainActivity::class.java.simpleName
    private val RC_SIGN_IN = 101
    private lateinit var signInButton: SignInButton
    private lateinit var signOutButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val loginFragment = LoginFragment()
        switchToFragment(loginFragment)


//        signInButton = findViewById(R.id.buttonSignIn)
//        signOutButton = findViewById(R.id.buttonSignOut)
//        signInButton.setOnClickListener(this)
//        signOutButton.setOnClickListener(this)
//        if (isSignedIn()){
//            visibleLoginButton(false)
//        }
    }

//    private fun visibleLoginButton(visible : Boolean){
//        if (visible) {
//            signInButton.visibility = View.VISIBLE
//            signOutButton.visibility = View.GONE
//        } else{
//            signInButton.visibility = View.GONE
//            signOutButton.visibility = View.VISIBLE
//        }
//    }

    private fun isSignedIn(): Boolean {
        return GoogleSignIn.getLastSignedInAccount(this) != null
    }

//    private fun signInSilently() {
//        val signInClient = GoogleSignIn.getClient(this,
//                GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN)
//        signInClient.silentSignIn().addOnCompleteListener(this
//        ) { task ->
//            if (task.isSuccessful) {
//                // The signed in account is stored in the task's result.
//                val signedInAccount = task.result
//                logD("Task is successful")
//                logD("Sign in account - ${signedInAccount?.account}")
//                logD("Display Name - ${signedInAccount?.displayName}")
//                logD("Email - ${signedInAccount?.email}")
////                visibleLoginButton(false)
//            } else {
//                logD("Task is not successful")
////                visibleLoginButton(true)
//                // Player will need to sign-in explicitly using via UI
//            }
//        }
//    }
//
//    fun logD(message: String){
//        Log.d(TAG, message)
//    }
//
//    override fun onResume() {
//        super.onResume()
//        signInSilently()
//    }
//
//    private fun startSignInIntent() {
//        val signInClient = GoogleSignIn.getClient(this,GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN)
//        val intent = signInClient.signInIntent
//        startActivityForResult(intent, RC_SIGN_IN)
//    }


//    override fun onClick(view: View) {
//        if (view.id == R.id.buttonSignIn) {
//            // start the asynchronous sign in flow
//            startSignInIntent()
//        } else if (view.id == R.id.buttonSignOut) {
//            // sign out.
//            signOut()
//            // show sign-in button, hide the sign-out button
//            visibleLoginButton(true)
//        }
//    }

//    private fun signOut() {
//        val signInClient = GoogleSignIn.getClient(this,
//                GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN)
//        signInClient.signOut().addOnCompleteListener(this) {
//            // at this point, the user is signed out.
//            logD("Sign out successful")
//        }
//    }

    override fun onFragmentInteraction(uri: Uri) {

    }

}
