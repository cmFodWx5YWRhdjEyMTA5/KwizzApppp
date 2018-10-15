package com.example.mayank.kwizzapp.wallet

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.mayank.googleplaygame.network.wallet.Transactions
import com.example.mayank.kwizzapp.KwizzApp

import com.example.mayank.kwizzapp.R
import com.example.mayank.kwizzapp.databinding.AddPointsFragmentBinding
import com.example.mayank.kwizzapp.dependency.components.DaggerInjectFragmentComponent
import com.example.mayank.kwizzapp.helpers.processRequest
import com.example.mayank.kwizzapp.network.ITransaction
import com.payumoney.core.entity.TransactionResponse
import com.payumoney.sdkui.ui.utils.PayUmoneyFlowManager
import com.payumoney.sdkui.ui.utils.ResultModel
import com.technoholicdeveloper.kwizzapp.gateway.PayUMoney
import io.reactivex.disposables.CompositeDisposable
import net.rmitsolutions.mfexpert.lms.helpers.*
import org.jetbrains.anko.find
import org.json.JSONObject
import javax.inject.Inject


class AddPointsFragment : Fragment(), View.OnClickListener {

    private val TAG = AddPointsFragment::class.java.simpleName
    private var listener: OnFragmentInteractionListener? = null
    @Inject
    lateinit var transactionService: ITransaction
    private lateinit var compositeDisposable: CompositeDisposable
    private lateinit var dataBinding: AddPointsFragmentBinding
    private lateinit var addPointsVm: Transactions.AddPoints
    private lateinit var payUMoney: PayUMoney

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
        val depComponent = DaggerInjectFragmentComponent.builder()
                .applicationComponent(KwizzApp.applicationComponent)
                .build()
        depComponent.injectAddPointsFragment(this)
        compositeDisposable = CompositeDisposable()
        payUMoney = PayUMoney(activity!!)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_points, container, false)
        val view = dataBinding.root
        addPointsVm = Transactions.AddPoints()
        dataBinding.addPointsVm = addPointsVm
        view.find<Button>(R.id.buttonPay).setOnClickListener(this)
        return view
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.buttonPay -> addPoints()
        }
    }

    private fun addPoints() {
        dataBinding.addPointsVm?.mobileNumber = activity?.getPref(SharedPrefKeys.MOBILE_NUMBER, "")
        dataBinding.addPointsVm?.email = activity?.getPref(SharedPrefKeys.EMAIL, "")
        dataBinding.addPointsVm?.firstName = activity?.getPref(SharedPrefKeys.FIRST_NAME, "")
        dataBinding.addPointsVm?.product = SharedPrefKeys.PRODUCT_RECHARGE_POINTS

        payUMoney.launchPayUMoney(dataBinding.addPointsVm?.amount?.toDouble()!!, dataBinding.addPointsVm?.firstName!!,
                dataBinding.addPointsVm?.mobileNumber!!, dataBinding.addPointsVm?.email!!,dataBinding.addPointsVm?.product!!)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PayUmoneyFlowManager.REQUEST_CODE_PAYMENT && resultCode == AppCompatActivity.RESULT_OK && data !=
                null) {
//            var transactionResponse = data.getParcelableExtra(PayUmoneyFlowManager.INTENT_EXTRA_TRANSACTION_RESPONSE);
            var transactionResponse = data.getParcelableExtra<TransactionResponse>(PayUmoneyFlowManager.INTENT_EXTRA_TRANSACTION_RESPONSE)

            var resultModel = data.getParcelableExtra<ResultModel>(PayUmoneyFlowManager.ARG_RESULT);


            // Response from Payumoney
            val payuResponse = transactionResponse.getPayuResponse();

            // Check which object is non-null
            if (payuResponse != null) {
                if (transactionResponse.transactionStatus == TransactionResponse.TransactionStatus.SUCCESSFUL) {
                    //Success Transaction
                    logD("Successfull transaction")

                    Log.d(TAG, "Pay u Response - $payuResponse")

                    if (payuResponse!=""){
                        val response = JSONObject(payuResponse)     // Done
                        val result = response.getJSONObject("result")   // Done
                        val status = result.getString("status")     // Done
                        val paymentId = result.getString("paymentId")
                        val txnId = result.getString("txnid")
                        val amount = result.getString("amount")
                        val addedOn = result.getString("addedon")
                        val createdOn = result.getString("createdOn")
                        val productInfo = result.getString("productinfo")
                        val firstName = result.getString("firstname")
//                        val lastName = result.getString("lastname")
                        val lastName = activity?.getPref(SharedPrefKeys.LAST_NAME, "")
                        val email = result.getString("email")
                        val mobileNumber = result.getString("phone")
                        val bankRefNumber = result.getString("bank_ref_num")
                        val bankCode = result.getString("bankcode")

                        // displayName = playGameLib?.getDisplayName()!!
                        updateTransactionDetails(firstName, lastName!!, "Display Name", mobileNumber, "", "",
                                email, productInfo, amount, txnId, paymentId, addedOn, createdOn, bankRefNumber, bankCode, "Credited", status)

                        logD("Transaction status - $status")
                    }else{
                        logD("PayU response is null")
                        showDialog(activity!!, "PayU Error", "PayU response is null.")
                    }

                } else {
                    //Failure Transaction
                    logD( "Failed transaction")
                    //showDialog(activity!!, "Response", "Transaction Failed!")
                    //toast("Transaction Failed")
                }

            } else if (resultModel?.error != null) {
                Log.d(TAG, "Error response : " + resultModel.error.transactionResponse)
                //showDialog(activity!!, "PayU Error", resultModel.error.transactionResponse.toString())
            } else {
                Log.d(TAG, "Both objects are null!");
                showDialog(activity!!, "PayU Error", "Both objects are null.")
            }
        }
    }

    private fun updateTransactionDetails(firstName: String?, lastName: String, displayName: String, mobileNumber: String?, transferTo: String, receiveFrom: String, email: String?, productInfo: String?, amount: String?, txnId: String?, paymentId: String?, addedOn: String?, createdOn: String?, bankRefNumber: String?, bankCode: String?, transactionType: String, status: String?) {
        compositeDisposable.add(transactionService.addTransactionDetails(firstName!!,lastName, displayName, mobileNumber!!,
                transferTo, receiveFrom, email!!, productInfo!!,amount!!, txnId!!, paymentId!!, addedOn!!, createdOn!!,
                bankRefNumber!!, bankCode!!, transactionType, status!!)
                .processRequest(
                        { response ->
                            if (response.isSuccess){
                                toast("Amount added successfully!")
                                val walletMenuFragment = WalletMenuFragment()
                                switchToFragment(walletMenuFragment)
                            }else{
                                showDialog(activity!!, "Error", response.message)
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
