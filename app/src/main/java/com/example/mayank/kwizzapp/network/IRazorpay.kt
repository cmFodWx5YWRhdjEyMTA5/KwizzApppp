package com.example.mayank.kwizzapp.network

import com.example.mayank.googleplaygame.network.wallet.Transactions
import com.example.mayank.kwizzapp.models.RazorpayModel
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.POST

interface IRazorpay {

    @POST("payu/GetOrderDetails.php")
    fun getOrderId(@Body orderDetails: RazorpayModel.OrderDetails) : Observable<RazorpayModel.OrderModel>

    @POST("payu/addPointsRazorpay.php")
    fun getTransactionByPaymentId(@Body payment : Transactions.AddPointToServer) : Observable<Transactions.CheckBalance>
}