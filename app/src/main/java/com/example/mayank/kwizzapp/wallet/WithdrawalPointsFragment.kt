package com.example.mayank.kwizzapp.wallet

import android.content.Context
import android.databinding.DataBindingUtil
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.mayank.googleplaygame.network.wallet.Transactions
import com.example.mayank.kwizzapp.KwizzApp

import com.example.mayank.kwizzapp.R
import com.example.mayank.kwizzapp.databinding.WithdrawalPointsBinding
import com.example.mayank.kwizzapp.dependency.components.DaggerInjectFragmentComponent
import com.example.mayank.kwizzapp.helpers.processRequest
import com.example.mayank.kwizzapp.network.ITransaction
import io.reactivex.disposables.CompositeDisposable
import net.rmitsolutions.mfexpert.lms.helpers.SharedPrefKeys
import net.rmitsolutions.mfexpert.lms.helpers.getPref
import net.rmitsolutions.mfexpert.lms.helpers.logD
import net.rmitsolutions.mfexpert.lms.helpers.showDialog
import org.jetbrains.anko.find
import org.jetbrains.anko.support.v4.startActivity
import javax.inject.Inject


class WithdrawalPointsFragment : Fragment(), View.OnClickListener {


    private var listener: OnFragmentInteractionListener? = null
    @Inject
    lateinit var transactionService: ITransaction
    private lateinit var compositeDisposable: CompositeDisposable
    private lateinit var dataBinding : WithdrawalPointsBinding
    private lateinit var withdrawalPoints : Transactions.WithdrawalPoints

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
        val depComponent = DaggerInjectFragmentComponent.builder()
                .applicationComponent(KwizzApp.applicationComponent)
                .build()
        depComponent.injectWithdrawalPointsFragment(this)
        compositeDisposable = CompositeDisposable()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dataBinding=DataBindingUtil.inflate(inflater,R.layout.fragment_withdrawal_points, container, false)
        val view = dataBinding.root
        withdrawalPoints = Transactions.WithdrawalPoints()
        dataBinding.withdrawalPointsVm = withdrawalPoints
        view.find<Button>(R.id.buttonWithdrawalPoints).setOnClickListener(this)
        val accountNumber = activity?.getPref(SharedPrefKeys.ACCOUNT_NUMBER, "")
        if (accountNumber!=""){
            disableBankDetail(true)
        }
        return view
    }

    private fun disableBankDetail(disable: Boolean) {
        dataBinding.disableBankDetails = disable
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.buttonWithdrawalPoints -> withdrawalPoint()
        }
    }

    private fun withdrawalPoint() {
        val amount = dataBinding.withdrawalPointsVm?.amount
        val firstName = activity?.getPref(SharedPrefKeys.FIRST_NAME, "")
        val lastName = activity?.getPref(SharedPrefKeys.LAST_NAME, "")
        val mobileNumber = activity?.getPref(SharedPrefKeys.MOBILE_NUMBER, "")
        val email = activity?.getPref(SharedPrefKeys.EMAIL, "")
        val accountNumber = dataBinding.withdrawalPointsVm?.accountNumber
        val ifsc = dataBinding.withdrawalPointsVm?.ifscCode
        val displayName = activity?.getPref(SharedPrefKeys.DISPLAY_NAME, "")
        val txnId = displayName+System.currentTimeMillis()

        compositeDisposable.add(transactionService.withdrawalPoints(firstName!!, lastName!!,displayName!!,mobileNumber!!,"",
                "",email!!,"Withdrawal Payments",amount!!,txnId,"",System.currentTimeMillis().toString(),
                "","","","Debited",accountNumber!!,ifsc!!,"Processed")
                .processRequest(
                        { response ->
                            if (response.isSuccess){
                                logD("Mobile Number - ${response.mobileNumber}\nBalance - ${response.balance}")
                                startActivity<WalletActivity>()
                            }else{
                                showDialog(activity!!,"Error",response.message)
                            }
                        },
                        { err ->
                            showDialog(activity!!, "Error", err.toString())
                        }
                ))


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
