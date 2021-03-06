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
import com.example.mayank.kwizzapp.Constants
import com.example.mayank.kwizzapp.KwizzApp

import com.example.mayank.kwizzapp.R
import com.example.mayank.kwizzapp.databinding.WithdrawalPointsBinding
import com.example.mayank.kwizzapp.dependency.components.DaggerInjectFragmentComponent
import com.example.mayank.kwizzapp.helpers.processRequest
import com.example.mayank.kwizzapp.network.ITransaction
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.withdrawal_points_layout.*
import net.rmitsolutions.mfexpert.lms.helpers.*
import org.jetbrains.anko.find
import org.jetbrains.anko.support.v4.startActivity
import java.util.*
import javax.inject.Inject


class WithdrawalPointsFragment : Fragment(), View.OnClickListener {


    private var listener: OnFragmentInteractionListener? = null
    @Inject
    lateinit var transactionService: ITransaction
    private lateinit var compositeDisposable: CompositeDisposable
    private lateinit var dataBinding: WithdrawalPointsBinding
    private lateinit var withdrawalPoints: Transactions.WithdrawalPoints
    private var accountNumber: String? = null
    private var ifscCode: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val depComponent = DaggerInjectFragmentComponent.builder()
                .applicationComponent(KwizzApp.applicationComponent)
                .build()
        depComponent.injectWithdrawalPointsFragment(this)
        compositeDisposable = CompositeDisposable()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_withdrawal_points, container, false)
        val view = dataBinding.root
        withdrawalPoints = Transactions.WithdrawalPoints()
        dataBinding.withdrawalPointsVm = withdrawalPoints
        view.find<Button>(R.id.buttonWithdrawalPoints).setOnClickListener(this)
        accountNumber = activity?.getPref(SharedPrefKeys.ACCOUNT_NUMBER, "")
        ifscCode = activity?.getPref(SharedPrefKeys.IFSC_CODE, "")
        when {
            accountNumber != "" -> disableBankDetail(true)
            else -> {
                disableBankDetail(false)
            }
        }
        return view
    }

    private fun disableBankDetail(disable: Boolean) {
        dataBinding.disableBankDetails = disable
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.buttonWithdrawalPoints -> withdrawalPoint()
        }
    }

    private fun withdrawalPoint() {
        val withdrawal = Transactions.WithdrawalPointsToServer()

        withdrawal.amount = dataBinding.withdrawalPointsVm?.amount?.toDouble()
        withdrawal.firstName = activity?.getPref(SharedPrefKeys.FIRST_NAME, "")
        withdrawal.lastName = activity?.getPref(SharedPrefKeys.LAST_NAME, "")
        withdrawal.mobileNumber = activity?.getPref(SharedPrefKeys.MOBILE_NUMBER, "")
        withdrawal.email = activity?.getPref(SharedPrefKeys.EMAIL, "")
        withdrawal.playerId = activity?.getPref(SharedPrefKeys.PLAYER_ID, "")
        val displayName = activity?.getPref(SharedPrefKeys.DISPLAY_NAME, "")
        withdrawal.txnId = displayName + System.currentTimeMillis()
        when {
            dataBinding.disableBankDetails != null -> when {
                !dataBinding.disableBankDetails!! -> {
                    accountNumber = dataBinding.withdrawalPointsVm?.accountNumber
                    ifscCode = dataBinding.withdrawalPointsVm?.ifscCode
                }
            }
        }
        withdrawal.accountNumber = accountNumber
        withdrawal.ifscCode = ifscCode
        withdrawal.productInfo = "Withdrawn points by itself."
        withdrawal.addedOn = System.currentTimeMillis().toString()
        withdrawal.createdOn = System.currentTimeMillis().toString()
        withdrawal.transactionType = "Debited"
        withdrawal.status = "Processed"

        when {
            validate() -> {
                showProgress()
                activity?.putPref(SharedPrefKeys.ACCOUNT_NUMBER, accountNumber)
                activity?.putPref(SharedPrefKeys.IFSC_CODE, ifscCode)
                compositeDisposable.add(transactionService.withdrawalPoint(withdrawal)
                        .processRequest(
                                { response ->
                                    if (response.isSuccess) {
                                        toast("Withdrawal request register successfully")
                                        hideProgress()
                                        startActivity<WalletActivity>()
                                        activity?.finish()
                                    } else {
                                        showDialog(activity!!, "Error", response.message)
                                        hideProgress()
                                    }
                                },
                                { err ->
                                    showDialog(activity!!, "Error", err.toString())
                                    hideProgress()
                                }
                        ))
            }
        }
    }

    private fun validate(): Boolean {
        when {
            dataBinding.withdrawalPointsVm?.amount.isNullOrBlank() -> {
                inputLayoutAmount.error = "Enter valid Amount"
                return false
            }
            else -> inputLayoutAmount.error = null
        }

        when {
            !dataBinding.disableBankDetails!! -> {
                when {
                    dataBinding.withdrawalPointsVm?.accountNumber.isNullOrBlank() -> {
                        inputLayoutAccountNumber.error = "Enter valid Account Number"
                        return false
                    }
                    else -> inputLayoutAccountNumber.error = null
                }

                when {
                    dataBinding.withdrawalPointsVm?.ifscCode.isNullOrBlank() -> {
                        inputLayoutIfscCode.error = "Enter valid IFSC Code"
                        return false
                    }
                    else -> inputLayoutIfscCode.error = null
                }
            }
        }
        return true
    }

    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        when (context) {
            is OnFragmentInteractionListener -> listener = context
            else -> throw RuntimeException("$context must implement OnFragmentInteractionListener")
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
