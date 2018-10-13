package com.example.mayank.kwizzapp.gamemenu

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.mayank.kwizzapp.KwizzApp

import com.example.mayank.kwizzapp.R
import com.example.mayank.kwizzapp.dependency.components.DaggerInjectFragmentComponent
import com.example.mayank.kwizzapp.helpers.processRequest
import com.example.mayank.kwizzapp.libgame.LibPlayGame
import com.example.mayank.kwizzapp.network.ITransaction
import io.reactivex.disposables.CompositeDisposable
import net.rmitsolutions.mfexpert.lms.helpers.*
import org.jetbrains.anko.find
import javax.inject.Inject

class GameMenuFragment : Fragment(), View.OnClickListener {

    @Inject
    lateinit var transactionService: ITransaction
    private var listener: OnFragmentInteractionListener? = null
    private val CLICKABLES = intArrayOf(R.id.singlePlayerButton, R.id.quickGameButton, R.id.multiplayerButton, R.id.invitationButton)
    private var check: Int = -1
    private lateinit var libPlayGame: LibPlayGame
    private lateinit var compositeDisposable: CompositeDisposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
        libPlayGame = LibPlayGame(activity!!)
        compositeDisposable = CompositeDisposable()
        val depComponent = DaggerInjectFragmentComponent.builder()
                .applicationComponent(KwizzApp.applicationComponent)
                .build()
        depComponent.injectGameMenuFragment(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_game_menu, container, false)

        for (id in CLICKABLES) {
            view.find<Button>(id).setOnClickListener(this)
        }
        return view
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.singlePlayerButton -> {

            }
            R.id.quickGameButton -> {
                //showProgress()
                check = 0
                checkBalance()

            }
            R.id.multiplayerButton -> {
                showProgress()
                check = 1
                checkBalance()
            }
            R.id.invitationButton -> {
                showProgress()
                libPlayGame.showInvitationInbox()
            }
        }
    }


    private fun checkBalance() {
        val mobileNumber = activity?.getPref(SharedPrefKeys.MOBILE_NUMBER, "")
        if (mobileNumber != "") {
            compositeDisposable.add(transactionService.checkBalance(mobileNumber!!)
                    .processRequest({ response ->
                        if (response.isSuccess){
                            if (response.balance >= 10.00){
                                if (check == 0){
                                    libPlayGame.startQuickGame()
                                }else {
                                    libPlayGame.invitePlayers()
                                }
                                check = -1
                            }else{
                                showDialog(activity!!, "Message", activity?.getString(R.string.insufficient_balance)!!)
                            }
                        }
                    }, { err ->
                        logD("Message - $err")
                    }))
        }else{
            showDialog(activity!!,"Error", "Update mobile number to continue!")
        }
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

}
