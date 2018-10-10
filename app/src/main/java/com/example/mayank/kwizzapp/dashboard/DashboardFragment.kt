package com.example.mayank.kwizzapp.dashboard

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button

import com.example.mayank.kwizzapp.R
import com.example.mayank.kwizzapp.login.LoginFragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import net.rmitsolutions.mfexpert.lms.helpers.*
import org.jetbrains.anko.find


class DashboardFragment : Fragment(), View.OnClickListener {

    private var listener: OnFragmentInteractionListener? = null
    private lateinit var signOut : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)
        signOut = view.find(R.id.buttonSignOut)
        signOut.setOnClickListener(this)
        return view
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.buttonSignOut ->{
                signOut()
            }
        }
    }

    private fun signOut() {
        if (isSignedIn()){
            val signInClient = GoogleSignIn.getClient(activity!!,
                    GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN)
            signInClient.signOut().addOnCompleteListener(activity!!) {
                // at this point, the user is signed out.
                toast("Sign out successfully!")
                activity?.clearPrefs()
                val loginFragment = LoginFragment()
                switchToFragment(loginFragment)
            }
        }else{
            toast("Already signed out!")
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
