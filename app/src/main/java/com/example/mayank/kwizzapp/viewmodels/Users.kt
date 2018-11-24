package com.example.mayank.kwizzapp.viewmodels

import android.databinding.BaseObservable
import android.databinding.ObservableField
import java.util.*

class Users {

    class UserInfo : BaseObservable(){
        var firstName= ObservableField<String>()
        var lastName = ObservableField<String>()
        var mobileNumber : String? = null
        var email :String? = null
        var playerId: String? = null
    }

    class InsertUserInfo{
        var firstName :String? = null
        var lastName : String? = null
        var mobileNumber : String? = null
        var email :String? = null
        var playerId: String? = null
    }

    class UpdateUserInfo{
        var firstName :String? = null
        var lastName : String? = null
        var mobileNumber : String? = null
        var email :String? = null
    }

    class UserBankDetails : BaseObservable(){
        var firstName : String? = null
        var lastName : String? = null
        var accountNumber : String? =  null
        var ifscCode : String? = null
    }


}