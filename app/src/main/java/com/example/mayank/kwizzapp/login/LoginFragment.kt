package com.example.mayank.kwizzapp.login

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.mayank.kwizzapp.MainActivity

import com.example.mayank.kwizzapp.R
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.games.Games
import com.google.android.gms.games.InvitationsClient
import com.google.android.gms.games.RealTimeMultiplayerClient
import net.rmitsolutions.mfexpert.lms.helpers.logD
import net.rmitsolutions.mfexpert.lms.helpers.switchToFragment
import org.jetbrains.anko.find
import org.jetbrains.anko.support.v4.find

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [LoginFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class LoginFragment : Fragment(), View.OnClickListener {

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null
    private val TAG = LoginFragment::class.java.simpleName
    private lateinit var loginButton : SignInButton
    private val RC_SIGN_IN = 101
    private lateinit var logoutButton : Button
    private var mRealTimeMultiplayerClient: RealTimeMultiplayerClient? = null
    private var mInvitationsClient: InvitationsClient? = null
    private var mPlayerId: String? = null
//    private var playGameLibrary: PlayGameLibrary? = null
    private var invitationClient : InvitationsClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)
        loginButton = view.find(R.id.buttonSignIn)
        logoutButton = view.find(R.id.logoutButton)
        loginButton.setOnClickListener(this)
        logoutButton.setOnClickListener(this)
        return view
    }

    private fun isSignedIn(): Boolean {
        return GoogleSignIn.getLastSignedInAccount(activity) != null
    }

    override fun onResume() {
        super.onResume()
        signInSilently()
    }

    private fun signInSilently() {
        val signInClient = GoogleSignIn.getClient(activity!!,
                GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN)
        signInClient.silentSignIn().addOnCompleteListener(activity!!
        ) { task ->
            if (task.isSuccessful) {
                // The signed in account is stored in the task's result.
                val signedInAccount = task.result
                onConnected(signedInAccount!!)
            } else {
                logD("Task is not successful")
                // Player will need to sign-in explicitly using via UI
            }
        }
    }

    private fun onConnected(signInAccount: GoogleSignInAccount){
        val gameClient = Games.getGamesClient(activity!!, GoogleSignIn.getLastSignedInAccount(activity)!!)
        gameClient.setViewForPopups(activity!!.findViewById(android.R.id.content))
//            Games.getGamesClient(this, signedInAccount).setViewForPopups()
        // update the clients
        mRealTimeMultiplayerClient = Games.getRealTimeMultiplayerClient(activity!!, signInAccount)
        mInvitationsClient = Games.getInvitationsClient(activity!!, signInAccount)

        // get the playerId from the PlayersClient
        val playersClient = Games.getPlayersClient(activity!!, signInAccount)
        playersClient.currentPlayer.addOnSuccessListener { player ->
            mPlayerId = player.playerId
            Log.d("Player ID", mPlayerId)
            Log.d("Display Name", player.displayName)
//            putPref(SharedPrefKeys.PLAYER_ID, mPlayerId!!)
//            putPref( SharedPrefKeys.DISPLAY_NAME, player.displayName)
            //val intent = Intent(this@MainActivity, DashboardActivity::class.java)
            var nameArray : List<String>? = null
            if (player.name !=null){
                nameArray = player.name.split(" ")
            }

            var firstName = nameArray?.get(0)
            var lastName = nameArray?.get(1)
            if (firstName==null || lastName==null){
                firstName = "Default"
                lastName = "Default"
            }

            logD("First Name - $firstName Last Name - $lastName")




//            putPref( SharedPrefKeys.FIRST_NAME, firstName)
//            putPref( SharedPrefKeys.LAST_NAME, lastName)
            //startActivity(intent)
//            logD("${getPref(SharedPrefKeys.FIRST_NAME, "")}")
//            val email = getPref(SharedPrefKeys.EMAIL, "")
//            val mobileNumber = getPref(SharedPrefKeys.MOBILE_NUMBER, "")

//            logD("Email - $email MobileNumber - $mobileNumber")
//            playGameLibrary = PlayGameLibrary(this)
//            invitationClient = Games.getInvitationsClient(this, playGameLibrary?.getSignInAccount()!!)
//            PlayGameLibrary.GameConstants.mInvitationClient?.registerInvitationCallback(playGameLibrary?.mInvitationCallbackHandler!!)
//            if (email=="" && mobileNumber == ""){
//                val userInfoFragment = UserInformationFragment()
//                switchToFragment(userInfoFragment)
//
//            }else{
//                val dashboardFrag = DashboardFragment()
//                switchToFragment(dashboardFrag)
//            }

        }

    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.buttonSignIn ->{
                startSignInIntent()
            }
            R.id.logoutButton ->{
                if (isSignedIn()){
                    signOut()
                }else {
                    logD("Already Signed Out!")
                }

            }
        }
    }

    private fun signOut() {
        val signInClient = GoogleSignIn.getClient(activity!!,
                GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN)
        signInClient.signOut().addOnCompleteListener(activity!!) {
            // at this point, the user is signed out.
            logD("Sign out successful")
        }
    }

    private fun startSignInIntent() {
        val signInClient = GoogleSignIn.getClient(activity!!, GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN)
        val intent = signInClient.signInIntent
        startActivityForResult(intent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        logD("Inside fragment activity result")
        if (requestCode == RC_SIGN_IN) {
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            if (result.isSuccess) {
                // The signed in account is stored in the result.
                val signedInAccount = result.signInAccount
                onConnected(signedInAccount!!)
            } else {
                var message = result.status.statusMessage
                if (message == null || message.isEmpty()) {
                    message = getString(R.string.signin_other_error)
                }
                AlertDialog.Builder(activity!!).setMessage(message)
                        .setNeutralButton(android.R.string.ok, null).show()
            }
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment LoginFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                LoginFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}
