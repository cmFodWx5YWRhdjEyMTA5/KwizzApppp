package com.example.mayank.kwizzapp.viewmodels

import android.arch.persistence.room.Ignore
import com.google.gson.annotations.SerializedName

open class CommonResult {
    @Ignore
    @SerializedName("isSuccess")
    var isSuccess: Boolean = false
    @Ignore
    @SerializedName("message")
    var message: String = ""
}