package com.example.mayank.googleplaygame.network.wallet

import com.example.mayank.kwizzapp.viewmodels.CommonResult
import com.google.gson.annotations.SerializedName

class Transactions {

    class CheckBalance : CommonResult() {
        var mobileNumber: String? = null
        val balance: Double = 0.0
    }

    class SubtractBalance{
        var firstName: String? = null
        var lastName: String? = null
        var playerId: String? = null
        var mobileNumber: String? = null
        var email: String? = null
        var productInfo: String? = null
        var amount: Double? = null
        var addedOn: String? = null
        var createdOn: String? = null
        var transactionType: String? = null
        var status: String? = null
    }

    class AddPointToServer{
        var firstName: String? = null
        var lastName: String? = null
        var playerId: String? = null
        var mobileNumber: String? = null
        var email: String? = null
        var productInfo: String? = null
        var amount: Double? = null
        var txnId : String? = null
        var paymentId : String? = null
        var addedOn : String? = null
        var createdOn: String? = null
        var transactionType: String? = null
        var status: String? = null
        var bankRefNumber : String ? = null
        var bankCode : String ? = null

    }

    class WithdrawalPointsToServer{
        var firstName: String? = null
        var lastName: String? = null
        var playerId: String? = null
        var mobileNumber: String? = null
        var email: String? = null
        var productInfo: String? = null
        var amount: Double? = null
        var txnId : String? = null
        var addedOn : String? = null
        var createdOn: String? = null
        var transactionType: String? = null
        var status: String? = null
        var accountNumber : String? = null
        var ifscCode : String? = null
    }

    class TransferPointsToServer{
        var firstName: String? = null
        var lastName: String? = null
        var playerId: String? = null
        var mobileNumber: String? = null
        var transferToNumber : String? = null
        var email: String? = null
        var amount: Double? = null
        var txnId : String? = null
        var addedOn : String? = null
        var createdOn: String? = null
        var status: String? = null
    }

    class ResultBalance{
        var displayName :  String? = null
        var amount : Double? = 0.0
        var timeStamp : String? = null
        var productInfo: String?= null
    }

    class GetBalance : CommonResult() {
        val mobileNumber: String? = null
        val balance: Double? = null
    }

    class AddPoints {
        var amount: String? = null
        var firstName: String? = null
        var mobileNumber: String? = null
        var email: String? = null
        var product: String? = null
    }

    class WithdrawalPoints {
        var holderName : String? = null
        var amount : String? = null
        var accountNumber : String? = null
        var ifscCode : String? = null
    }

    class TransferPoints{
        var amount: String? = null
        var transferTo : String? = null
    }

    class Transaction : CommonResult(){
        var transactions =  mutableListOf<TransactionDetails>()
    }

    class TransactionDetails {
        val firstName :  String? = null
        val lastName : String ? =null
        val mobileNumber : String? = null
        val transferTo : String? = null
        val receivedFrom : String? =null
        val amount : Double = 0.0
        val txnId : String? = null
        val createdOn : String? = null
        val transactionType : String? = null
        val status : String? = null
        val productInfo : String? = null
    }


    @SerializedName("firstName")
    var firstName: String? = null

    @SerializedName("lastName")
    var lastName: String? = null

    @SerializedName("playGameName")
    var playGameName: String? = null

    @SerializedName("mobileNumber")
    var mobileNumber: String? = null

    @SerializedName("transferTo")
    var transferTo: String? = null

    @SerializedName("receivedFrom")
    var receivedFrom: String? = null

    @SerializedName("email")
    var email: String? = null

    @SerializedName("productInfo")
    var productInfo: String? = null

    @SerializedName("amount")
    var amount: String? = null

    @SerializedName("txnId")
    var txnId: String? = null

    @SerializedName("paymentId")
    var paymentId: String? = null

    @SerializedName("addedOn")
    var addedOn: String? = null

    @SerializedName("createdOn")
    var createdOn: String? = null

    @SerializedName("bankRefNumber")
    var bankRefNumber: String? = null

    @SerializedName("bankCode")
    var bankCode: String? = null

    @SerializedName("transactionType")
    var transactionType: String? = null

    @SerializedName("status")
    var status: String? = null

    @SerializedName("balance")
    var balance: String? = null

    @SerializedName("error")
    var error: String? = null

    @SerializedName("success")
    var result: String? = null


}