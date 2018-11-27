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
import com.example.mayank.kwizzapp.databinding.TransferPointsBinding
import com.example.mayank.kwizzapp.dependency.components.DaggerInjectFragmentComponent
import com.example.mayank.kwizzapp.helpers.processRequest
import com.example.mayank.kwizzapp.network.ITransaction
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.transfer_points_layout.*
import net.rmitsolutions.mfexpert.lms.helpers.*
import org.jetbrains.anko.find
import org.jetbrains.anko.support.v4.startActivity
import java.util.*
import javax.inject.Inject


class TransferPointsFragment : Fragment(), View.OnClickListener {

    private var listener: OnFragmentInteractionListener? = null
    @Inject
    lateinit var transactionService: ITransaction
    private lateinit var compositeDisposable: CompositeDisposable
    private lateinit var dataBinding: TransferPointsBinding
    private lateinit var transferPoints: Transactions.TransferPoints

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
        val depComponent = DaggerInjectFragmentComponent.builder()
                .applicationComponent(KwizzApp.applicationComponent)
                .build()
        depComponent.injectTransferPointsFragment(this)
        compositeDisposable = CompositeDisposable()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_transfer_points, container, false)
        val view = dataBinding.root
        transferPoints = Transactions.TransferPoints()
        dataBinding.transferPointsVm = transferPoints
        view.find<Button>(R.id.buttonTransferPoints).setOnClickListener(this)
        return view
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.buttonTransferPoints -> transferPoint()
        }
    }

    private fun transferPoint() {
        val transfer = Transactions.TransferPointsToServer()
        transfer.amount = dataBinding.transferPointsVm?.amount?.toDouble()
        transfer.firstName = activity?.getPref(SharedPrefKeys.FIRST_NAME, "")
        transfer.lastName = activity?.getPref(SharedPrefKeys.LAST_NAME, "")
        transfer.mobileNumber = activity?.getPref(SharedPrefKeys.MOBILE_NUMBER, "")
        transfer.email = activity?.getPref(SharedPrefKeys.EMAIL, "")
        val displayName = activity?.getPref(SharedPrefKeys.DISPLAY_NAME, "")
        transfer.txnId = displayName + System.currentTimeMillis()
        transfer.transferToNumber = dataBinding.transferPointsVm?.transferTo
        transfer.playerId = activity?.getPref(SharedPrefKeys.PLAYER_ID, "")
        transfer.addedOn = Constants.getFormatDate(Calendar.getInstance().time)
        transfer.createdOn = Constants.getFormatDate(Calendar.getInstance().time)
        transfer.status = "Success"

        if (validate()) {
            showProgress()
            compositeDisposable.add(transactionService.transferPoint(transfer)
                    .processRequest(
                            { response ->
                                when {
                                    response.isSuccess -> {
                                        hideProgress()
                                        toast(response.message)
                                        startActivity<WalletActivity>()
                                        activity?.finish()
                                    }
                                    else ->{
                                        hideProgress()
                                        showDialog(activity!!, "Error", response.message)
                                    }
                                }
                            },
                            { err ->
                                hideProgress()
                                showDialog(activity!!, "Error", err.toString())
                            }
                    ))
        }

    }

    private fun validate(): Boolean {
        when {
            dataBinding.transferPointsVm?.amount.isNullOrBlank() -> {
                inputLayoutAmount.error = "Enter valid amount."
                return false
            }
            else -> inputLayoutAmount.error = null
        }
        when {
            dataBinding.transferPointsVm?.transferTo.isNullOrBlank() -> {
                inputLayoutMobileNumber.error = "Enter Mobile Number"
                return false
            }
            else -> inputLayoutMobileNumber.error = null
        }
        when {
            dataBinding.transferPointsVm?.transferTo?.length != 10 -> {
                inputLayoutMobileNumber.error = "Enter valid 10 digit mobile number."
                return false
            }
            else -> inputLayoutMobileNumber.error = null
        }

        val mobileNumber = activity?.getPref(SharedPrefKeys.MOBILE_NUMBER, "")
        when {
            mobileNumber != "" -> if (dataBinding.transferPointsVm?.transferTo == mobileNumber) {
                inputLayoutMobileNumber.error = "Mobile number must be different from your number"
                return false
            } else {
                inputLayoutMobileNumber.error = null
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
