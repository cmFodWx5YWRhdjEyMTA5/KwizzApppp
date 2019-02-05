package com.example.mayank.kwizzapp.wallet

import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Window
import com.example.mayank.googleplaygame.network.wallet.Transactions
import com.example.mayank.kwizzapp.Constants
import com.example.mayank.kwizzapp.KwizzApp
import com.example.mayank.kwizzapp.R
import com.example.mayank.kwizzapp.dependency.components.DaggerInjectActivityComponent
import com.example.mayank.kwizzapp.helpers.Converters
import com.example.mayank.kwizzapp.helpers.processRequest
import com.example.mayank.kwizzapp.network.IRazorpay
import com.example.mayank.kwizzapp.network.ITransaction
import com.example.mayank.kwizzapp.settings.SettingsActivity
import com.example.mayank.kwizzapp.transactions.TransactionFragment
import com.payumoney.core.entity.TransactionResponse
import com.payumoney.sdkui.ui.utils.PayUmoneyFlowManager
import com.payumoney.sdkui.ui.utils.ResultModel
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import io.reactivex.disposables.CompositeDisposable
import net.rmitsolutions.mfexpert.lms.helpers.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.support.v4.startActivity
import org.jetbrains.anko.toast
import org.json.JSONObject
import javax.inject.Inject

class WalletActivity : AppCompatActivity(), WalletMenuFragment.OnFragmentInteractionListener,
        AddPointsFragment.OnFragmentInteractionListener, WithdrawalPointsFragment.OnFragmentInteractionListener,
        TransferPointsFragment.OnFragmentInteractionListener, TransactionFragment.OnFragmentInteractionListener,
        PaymentResultListener {

    private lateinit var compositeDisposable : CompositeDisposable
    @Inject
    lateinit var transactionService: ITransaction

    @Inject
    lateinit var razorpayService: IRazorpay

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wallet)

        val depComponent = DaggerInjectActivityComponent.builder()
                .applicationComponent(KwizzApp.applicationComponent)
                .build()
        depComponent.injectWalletActivity(this)

        compositeDisposable = CompositeDisposable()

        val walletMenuFragment = WalletMenuFragment()
        switchToFragment(walletMenuFragment)
    }

    override fun onFragmentInteraction(uri: Uri) {

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PayUmoneyFlowManager.REQUEST_CODE_PAYMENT && resultCode == AppCompatActivity.RESULT_OK && data !=
                null) {
            var transactionResponse = data.getParcelableExtra<TransactionResponse>(PayUmoneyFlowManager.INTENT_EXTRA_TRANSACTION_RESPONSE)

            var resultModel = data.getParcelableExtra<ResultModel>(PayUmoneyFlowManager.ARG_RESULT);
            // Response from Payumoney
            val payuResponse = transactionResponse.getPayuResponse();
            // Check which object is non-null
            if (payuResponse != null) {
                if (transactionResponse.transactionStatus == TransactionResponse.TransactionStatus.SUCCESSFUL) {
                    //Success Transaction
                    Log.d("TAG", "Pay u Response - $payuResponse")
                    if (payuResponse!=""){
                        val addPoints = Transactions.AddPointToServer()
                        val response = JSONObject(payuResponse)     // Done
                        val result = response.getJSONObject("result")   // Done
                        addPoints.status = result.getString("status")     // Done
                        addPoints.paymentId = result.getString("paymentId")
                        addPoints.txnId = result.getString("txnid")
                        addPoints.amount = result.getString("amount").toDouble()
                        addPoints.addedOn = result.getString("addedon")
                        val formatDate = Converters.fromTimestamp(result.getString("createdOn").toLong())
                        addPoints.createdOn = Constants.getFormatDate(formatDate!!)
                        addPoints.productInfo = result.getString("productinfo")
                        addPoints.firstName = result.getString("firstname")
                        addPoints.lastName = getPref(SharedPrefKeys.LAST_NAME, "")
                        addPoints.email = result.getString("email")
                        addPoints.mobileNumber = result.getString("phone")
                        addPoints.bankRefNumber = result.getString("bank_ref_num")
                        addPoints.bankCode = result.getString("bankcode")
                        addPoints.playerId = getPref(SharedPrefKeys.PLAYER_ID, "")
                        addPoints.transactionType = "Credited"

                        updateTransactionDetails(addPoints)

                    }else{
                        showDialog(this, "PayU Error", "PayU response is null.")
                    }

                } else {
                    logD("Transaction Failed!")
                }

            } else if (resultModel?.error != null) {
                showDialog(this,"Error response", resultModel.error.transactionResponse.toString())
            } else {
                showDialog(this, "PayU Error", "Both objects are null.")
            }
        }
    }

    private fun updateTransactionDetails(addPoints : Transactions.AddPointToServer) {
        compositeDisposable = CompositeDisposable()
        compositeDisposable.add(transactionService.addPoints(addPoints)
                .processRequest(
                        { response ->
                            if (response.isSuccess){
                                toast("Amount added successfully!")
                                startActivity<WalletActivity>()
                                finish()
                            }else{
                                showDialog(this, "Error", response.message)
                            }
                        },
                        { err ->
                            showDialog(this, "Error", err.toString())
                        }
                ))
    }

    override fun onBackPressed() {
        val count = supportFragmentManager.backStackEntryCount
        when (count) {
            0 -> super.onBackPressed()
            1 -> supportFragmentManager.popBackStack()
            else -> {
                finish()
                startActivity<WalletActivity>()
            }
        }
    }

    override fun onPaymentError(errorCode: Int, response: String?) {
        hideProgress()
        when (errorCode) {
            Checkout.NETWORK_ERROR -> toast("Network error occourred")
            Checkout.PAYMENT_CANCELED -> toast("Payment Cancelled")
            Checkout.INVALID_OPTIONS -> toast("Invalid Options")
            Checkout.TLS_ERROR -> toast("Device doesn't have TLS 1.1 or TLS 1.2")
        }
    }


    override fun onPaymentSuccess(razorpayPaymentID: String?) {
        logD("Success - $razorpayPaymentID")
        getTransactionDetails(razorpayPaymentID!!)
    }

    // Get the transaction details using Razorpay
    private fun getTransactionDetails(paymentId : String) {
        val payment = Transactions.AddPointToServer()
        payment.paymentId = paymentId
        payment.firstName = getPref(SharedPrefKeys.FIRST_NAME, "")
        payment.lastName = getPref(SharedPrefKeys.LAST_NAME, "")
        payment.mobileNumber = getPref(SharedPrefKeys.MOBILE_NUMBER, "")
        payment.playerId = getPref(SharedPrefKeys.PLAYER_ID, "")
        payment.transactionType = "Credited"
        payment.addedOn = System.currentTimeMillis().toString()
        payment.createdOn = System.currentTimeMillis().toString()
        payment.status = "success"
        compositeDisposable.add(razorpayService.getTransactionByPaymentId(payment)
                .processRequest(
                        { transactions ->
                            hideProgress()
                            toast(transactions.message)
                            finish()
                            startActivity<WalletActivity>()
                        },
                        { err ->
                            hideProgress()
                            toast("Error - $err")
                        }
                )
        )
    }
}
