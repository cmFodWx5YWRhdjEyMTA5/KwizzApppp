package com.example.mayank.kwizzapp.network

import com.example.mayank.googleplaygame.network.wallet.Transactions
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import io.reactivex.Observable

interface ITransaction {

    @FormUrlEncoded
    @POST("payu/checkBalance.php")
    fun checkBalance(@Field("mobileNumber") mobileNumber: String): Observable<Transactions.CheckBalance>

    @FormUrlEncoded
    @POST("payu/subtractBalance.php")
    fun subtractResultBalance(
            @Field("displayName") displayName: String,
            @Field("amount") amount: Double,
            @Field("timeStamp") timeStamp: String): Observable<Transactions.SubtractBalance>
}