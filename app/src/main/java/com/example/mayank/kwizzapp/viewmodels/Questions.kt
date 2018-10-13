package com.example.mayank.kwizzapp.viewmodels

import com.google.gson.annotations.SerializedName

class Questions {

    class CountRows : CommonResult(){
        @SerializedName("numberOfRows")
        val rowCount : Int? = null
    }

    class Question : CommonResult(){
        val quesCode: String? = null
        val question: String? = null
        val optionA: String? = null
        val optionB: String? = null
        val optionC: String? = null
        val optionD: String? = null
        val optionE: String? = null
        val answer: String? = null
    }
}