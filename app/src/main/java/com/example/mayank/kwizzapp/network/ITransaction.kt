package com.example.mayank.kwizzapp.network

import com.example.mayank.googleplaygame.network.wallet.Transactions
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import io.reactivex.Observable
import retrofit2.http.Body

interface ITransaction {


    @POST("payu/checkBalance.php")
    fun checkBalance(@Body mobileNumber: Transactions.CheckBalance): Observable<Transactions.CheckBalance>


    @POST("payu/subtractBalance.php")
    fun subtractBalance(@Body subtractBalance: Transactions.SubtractBalance): Observable<Transactions.CheckBalance>

    @FormUrlEncoded
    @POST("payu/updateResultBalance.php")
    fun updateResultBalance(
            @Field("displayName") displayName: String,
            @Field("amount") amount: Double,
            @Field("timeStamp") timeStamp: String): Observable<Transactions.GetBalance>

    @FormUrlEncoded
    @POST("payu/addPoints.php")
    fun addTransactionDetails(
            @Field("firstName") firstName: String,
            @Field("lastName") lastName: String,
            @Field("playerId") displayName: String,
            @Field("mobileNumber") mobileNumber: String,
            @Field("transferTo") transferTo: String,
            @Field("receivedFrom") receivedFrom: String,
            @Field("email") email: String,
            @Field("productInfo") productInfo: String,
            @Field("amount") amount: String,
            @Field("txnId") txnId: String,
            @Field("paymentId") paymentId: String,
            @Field("addedOn") addedOn: String,
            @Field("createdOn") createdOn: String,
            @Field("bankRefNumber") bankRefNumber: String,
            @Field("bankCode") bankIfscCode: String,
            @Field("transactionType") transactionType: String,
            @Field("status") status: String): Observable<Transactions.GetBalance>

    @FormUrlEncoded
    @POST("payu/withdrawalPoints.php")
    fun withdrawalPoints(
            @Field("firstName") firstName: String,
            @Field("lastName") lastName: String,
            @Field("playerId") playGameName: String,
            @Field("mobileNumber") mobileNumber: String,
            @Field("transferTo") transferTo: String,
            @Field("receivedFrom") receivedFrom: String,
            @Field("email") email: String,
            @Field("productInfo") productInfo: String,
            @Field("amount") amount: String,
            @Field("txnId") txnId: String,
            @Field("paymentId") paymentId: String,
            @Field("addedOn") addedOn: String,
            @Field("createdOn") createdOn: String,
            @Field("bankRefNumber") bankRefNumber: String,
            @Field("bankCode") bankCode: String,
            @Field("transactionType") transactionType: String,
            @Field("accountNumber") accountNumber : String,
            @Field("ifscCode") ifscCode : String,
            @Field("status") status: String): Observable<Transactions.GetBalance>

    @FormUrlEncoded
    @POST("payu/transferPoints.php")
    fun transferPoints(
            @Field("firstName") firstName: String,
            @Field("lastName") lastName: String,
            @Field("playerId") displayName: String,
            @Field("mobileNumber") mobileNumber: String,
            @Field("transferTo") transferTo: String,
            @Field("receivedFrom") receivedFrom: String,
            @Field("email") email: String,
            @Field("productInfo") productInfo: String,
            @Field("amount") amount: String,
            @Field("txnId") txnId: String,
            @Field("paymentId") paymentId: String,
            @Field("addedOn") addedOn: String,
            @Field("createdOn") createdOn: String,
            @Field("bankRefNumber") bankRefNumber: String,
            @Field("bankCode") bankCode: String,
            @Field("transactionType") transactionType: String,
            @Field("status") status: String): Observable<Transactions.GetBalance>


    @FormUrlEncoded
    @POST("payu/fetchTransactions.php")
    fun fetchTransactions(@Field("mobileNumber") mobileNumber: String) :Observable<Transactions.Transaction>
}