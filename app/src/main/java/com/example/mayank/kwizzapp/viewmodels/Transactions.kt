package com.example.mayank.googleplaygame.network.wallet

import com.example.mayank.kwizzapp.viewmodels.CommonResult
import com.google.gson.annotations.SerializedName

class Transactions {

    class CheckBalance : CommonResult(){
        val mobileNumber : String? = null
        val balance : Double = 0.0
    }

    class GetBalance : CommonResult(){
        val mobileNumber : String? = null
        val balance : Double? = null
    }

    class AddPoints  {
        var amount : String? =null
        var firstName : String? =null
        var mobileNumber : String? = null
        var email : String? = null
        var product :String? = null
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