package com.example.mayank.kwizzapp.wallet

import android.content.Context
import android.databinding.DataBindingUtil
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
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
import com.example.mayank.kwizzapp.models.RazorpayModel
import com.example.mayank.kwizzapp.network.IRazorpay
import com.razorpay.Checkout
import com.technoholicdeveloper.kwizzapp.gateway.PayUMoney
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.add_points_layout.*
import net.rmitsolutions.mfexpert.lms.helpers.*
import org.jetbrains.anko.find
import org.json.JSONObject
import javax.inject.Inject

class AddPointsFragment : Fragment(), View.OnClickListener {

    private var listener: OnFragmentInteractionListener? = null
    private lateinit var dataBinding: AddPointsFragmentBinding
    private lateinit var addPointsVm: Transactions.AddPoints
    private lateinit var payUMoney: PayUMoney
    private lateinit var compositeDisposable: CompositeDisposable
    @Inject
    lateinit var razorpayService: IRazorpay

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
        payUMoney = PayUMoney(activity!!)

        val depComponent = DaggerInjectFragmentComponent.builder()
                .applicationComponent(KwizzApp.applicationComponent)
                .build()
        depComponent.injectAddPointsFragment(this)
        compositeDisposable = CompositeDisposable()
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
            R.id.buttonPay -> {
                if (validate()) addPointsRazorpay()
            }
        }
    }

    // By Using Razorpay
    private fun addPointsRazorpay() {
        showProgress()
        getOrderId()
    }

    private fun validate(): Boolean {
        if (dataBinding.addPointsVm?.amount.isNullOrBlank()) {
            inputLayoutAmount.error = " Enter valid Amount"
            return false
        } else {
            inputLayoutAmount.error = null
        }
        return true
    }

    // By using PayUMoney
    private fun addPoints() {
        dataBinding.addPointsVm?.mobileNumber = activity?.getPref(SharedPrefKeys.MOBILE_NUMBER, "")
        dataBinding.addPointsVm?.email = activity?.getPref(SharedPrefKeys.EMAIL, "")
        dataBinding.addPointsVm?.firstName = activity?.getPref(SharedPrefKeys.FIRST_NAME, "")
        dataBinding.addPointsVm?.product = SharedPrefKeys.PRODUCT_RECHARGE_POINTS

        payUMoney.launchPayUMoney(dataBinding.addPointsVm?.amount?.toDouble()!!, dataBinding.addPointsVm?.firstName!!,
                dataBinding.addPointsVm?.mobileNumber!!, dataBinding.addPointsVm?.email!!, dataBinding.addPointsVm?.product!!)

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

    // To get the order id from razorpay
    private fun getOrderId() {
        val orderDetails = RazorpayModel.OrderDetails()
        orderDetails.amount = (dataBinding.addPointsVm?.amount!!.toInt() * 100).toString()
        orderDetails.receipt = "K${System.currentTimeMillis()}"
        orderDetails.currency = "INR"
        orderDetails.payment_capture = "1"
        compositeDisposable.add(razorpayService
                .getOrderId(orderDetails)
                .processRequest(
                        { order ->
                            if (order.status == "created") {
                                startPayment(order.id)
                            } else {
                                hideProgress()
                                toast("${order.status}")
                            }
                        },
                        { err ->
                            hideProgress()
                            logD(err.toString())
                        }
                ))
    }

    // Start payment using Razorpay
    private fun startPayment(orderId: String) {
        val checkout = Checkout()
        checkout.setImage(R.drawable.app_logo)
        try {
            val options = JSONObject()
            options.put("name", "Alchemy Education")
            options.put("description", "Points recharged by yourself.")
            options.put("currency", "INR")
            options.put("order_id", orderId)
            /**
             * Amount is always passed in PAISE
             * Eg: "500" = Rs 5.00
             */
            var amount = dataBinding.addPointsVm?.amount!!.toInt()
            amount *= 100
            logD("Amount - $amount")
            options.put("amount", amount)
            val preFill = JSONObject()
            preFill.put("email", activity?.getPref(SharedPrefKeys.EMAIL, ""))
            preFill.put("contact", activity?.getPref(SharedPrefKeys.MOBILE_NUMBER, ""))
            options.put("prefill", preFill)
            checkout.open(activity, options)
        } catch (e: Exception) {
            Log.e("Error", "Error in starting Razorpay Checkout", e)
        }
    }
}
