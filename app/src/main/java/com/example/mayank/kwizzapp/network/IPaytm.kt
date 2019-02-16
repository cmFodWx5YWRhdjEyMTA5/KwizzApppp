package com.example.mayank.kwizzapp.network

import com.example.mayank.kwizzapp.models.RazorpayModel
import com.example.mayank.kwizzapp.paytm.Paytm
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.POST

interface IPaytm {

    @POST("payu/paytm/gratificationSamle.php")
    fun transferPointsToUser(@Body transferPoints:Paytm.RequestTransferPointsToUser ) : Observable<Paytm.TransferPointsResponse>
}