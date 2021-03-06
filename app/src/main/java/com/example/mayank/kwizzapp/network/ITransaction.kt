package com.example.mayank.kwizzapp.network

import com.example.mayank.googleplaygame.network.wallet.Transactions
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import io.reactivex.Observable
import retrofit2.http.Body

interface ITransaction {

    // Check balance
    @POST("payu/checkBalance.php")
    fun checkBalance(@Body mobileNumber: Transactions.CheckBalance): Observable<Transactions.CheckBalance>

    // Subtract Balance
    @POST("payu/subtractBalance.php")
    fun subtractBalance(@Body subtractBalance: Transactions.SubtractBalance): Observable<Transactions.CheckBalance>

    // Subtract Balance
    @POST("payu/updateResultBalanceTest.php")
    fun updateResultBalance(@Body subtractBalance: Transactions.ResultBalance): Observable<Transactions.CheckBalance>

    @POST("payu/updateLoosePoints.php")
    fun updateLoosePoints(@Body updateLoosePoints: Transactions.UpdateLoosePoints): Observable<Transactions.CheckBalance>

//    @FormUrlEncoded
//    @POST("payu/updateResultBalance.php")
//    fun updateResultBalance(
//            @Field("displayName") displayName: String,
//            @Field("amount") amount: Double,
//            @Field("timeStamp") timeStamp: String): Observable<Transactions.GetBalance>

    // Add points to server
    @POST("payu/addPoints.php")
    fun addPoints(@Body addPoints: Transactions.AddPointToServer): Observable<Transactions.CheckBalance>

    // Withdrawal points
    @POST("payu/withdrawalPoints.php")
    fun withdrawalPoint(@Body addPoints: Transactions.WithdrawalPointsToServer): Observable<Transactions.CheckBalance>

    // Transfer Points
    @POST("payu/transferPoints.php")
    fun transferPoint(@Body addPoints: Transactions.TransferPointsToServer): Observable<Transactions.CheckBalance>

    @FormUrlEncoded
    @POST("payu/fetchTransactions.php")
    fun fetchTransactions(@Field("mobileNumber") mobileNumber: String) :Observable<Transactions.Transaction>
}