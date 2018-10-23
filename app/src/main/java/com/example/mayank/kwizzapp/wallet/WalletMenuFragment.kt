package com.example.mayank.kwizzapp.wallet

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.CardView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mayank.kwizzapp.KwizzApp

import com.example.mayank.kwizzapp.R
import com.example.mayank.kwizzapp.dependency.components.DaggerInjectFragmentComponent
import com.example.mayank.kwizzapp.helpers.processRequest
import com.example.mayank.kwizzapp.network.ITransaction
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.wallet_menu_layout.*
import net.rmitsolutions.mfexpert.lms.helpers.*
import org.jetbrains.anko.find
import javax.inject.Inject

class WalletMenuFragment : Fragment(), View.OnClickListener {
    @Inject
    lateinit var transactionService: ITransaction
    private lateinit var compositeDisposable: CompositeDisposable
    private var listener: OnFragmentInteractionListener? = null
    private val CLICKABLES = intArrayOf(R.id.addPointsLayout, R.id.withdrawalLayout, R.id.transferLayout, R.id.transactionLayout)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
        val depComponent = DaggerInjectFragmentComponent.builder()
                .applicationComponent(KwizzApp.applicationComponent)
                .build()
        depComponent.injectWalletMenuFragment(this)
        compositeDisposable = CompositeDisposable()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_wallet_menu, container, false)
        // Getting balance from server
        when {
            activity?.isNetConnected()!! -> checkBalance()
            else -> {
                toast("No Internet")
                walletPoints.text = "Error - No Internet !"
            }
        }
        for (id in CLICKABLES) view.find<CardView>(id).setOnClickListener(this)
        return view
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.addPointsLayout ->{
                val addPointsFragment = AddPointsFragment()
                switchToFragment(addPointsFragment)
            }

            R.id.withdrawalLayout->{
                val withdrawalPointsFragment = WithdrawalPointsFragment()
                switchToFragment(withdrawalPointsFragment)
            }

            R.id.transferLayout->{
                val transferPointsFragment = TransferPointsFragment()
                switchToFragment(transferPointsFragment)
            }

            R.id.transactionLayout ->{
                showDialog(activity!!, "Transactions", "Coming soon !")
            }
        }
    }

    private fun checkBalance(){
        val mobileNumber = activity?.getPref(SharedPrefKeys.MOBILE_NUMBER, "")
        when {
            mobileNumber!="" -> compositeDisposable.add(transactionService.checkBalance(mobileNumber!!)
                    .processRequest(
                            { response ->
                                when {
                                    response.isSuccess -> walletPoints.text ="Points - ${response.balance} ${activity?.getString(R.string.rupeeSymbol)}"
                                    else -> walletPoints.text = "Failed !"
                                }
                            },
                            { err->
                                walletPoints.text = "Failed !"
                                showDialog(activity!!, "Error", err.toString())
                            }
                    ))
            else -> {}//balanceTextView.visibility = View.GONE
        }

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
    }
}
