package com.example.mayank.kwizzapp.dashboard

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.mayank.kwizzapp.Constants

import com.example.mayank.kwizzapp.R
import com.example.mayank.kwizzapp.gamemenu.GameMenuFragment
import com.example.mayank.kwizzapp.login.LoginFragment
import com.example.mayank.kwizzapp.wallet.WalletActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.games.Games
import net.rmitsolutions.mfexpert.lms.helpers.*
import org.jetbrains.anko.find
import org.jetbrains.anko.support.v4.startActivity
import kotlin.math.sign


class DashboardFragment : Fragment(), View.OnClickListener {

    private var listener: OnFragmentInteractionListener? = null
    private val CLICKABLES = intArrayOf(R.id.buttonPlay, R.id.buttonAchievements, R.id.buttonLeaderboards, R.id.buttonWallet, R.id.buttonSignOut)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)
        for (id in CLICKABLES) {
            view.find<Button>(id).setOnClickListener(this)
        }
        return view
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.buttonSignOut -> signOut()
            R.id.buttonAchievements -> showAchievements()
            R.id.buttonLeaderboards -> showLeaderBoards()
            R.id.buttonWallet -> startActivity<WalletActivity>()
            R.id.buttonPlay -> openGameMenuFragment()
        }
    }

    private fun openGameMenuFragment() {
        val gameMenu = GameMenuFragment()
        switchToFragment(gameMenu)
    }


    private fun showAchievements() {
        Games.getAchievementsClient(activity!!, GoogleSignIn.getLastSignedInAccount(activity)!!)
                .achievementsIntent
                .addOnSuccessListener { intent -> startActivityForResult(intent, Constants.RC_ACHIEVEMENT_UI) }
    }

    private fun showLeaderBoards() {
        Games.getLeaderboardsClient(activity!!, GoogleSignIn.getLastSignedInAccount(activity)!!)
                .getLeaderboardIntent(getString(R.string.leaderboard_global_rank))
                .addOnSuccessListener { intent -> startActivityForResult(intent, Constants.RC_LEADERBOARD_UI) }
    }

    private fun signOut() {
        when {
            isSignedIn() -> {
                val signInClient = GoogleSignIn.getClient(activity!!, GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN)
                signInClient.signOut().addOnCompleteListener(activity!!) {
                    // at this point, the user is signed out.
                    toast("Sign out successfully!")
                    activity?.clearPrefs()
                    val loginFragment = LoginFragment()
                    switchToFragment(loginFragment)
                }
            }
            else -> toast("Already signed out!")
        }

    }

    private fun isSignedIn(): Boolean {
        return GoogleSignIn.getLastSignedInAccount(activity) != null
    }


    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        when (context) {
            is OnFragmentInteractionListener -> listener = context
            else -> throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
                DashboardFragment().apply {
                    arguments = Bundle().apply {
                    }
                }
    }
}
