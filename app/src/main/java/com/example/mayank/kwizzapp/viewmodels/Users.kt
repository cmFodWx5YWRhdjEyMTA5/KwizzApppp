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
        var displayName: String? = null
    }


}