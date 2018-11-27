package com.example.mayank.kwizzapp.network

import com.example.mayank.kwizzapp.viewmodels.CommonResult
import com.example.mayank.kwizzapp.viewmodels.Questions
import com.example.mayank.kwizzapp.viewmodels.Users
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

import io.reactivex.Observable
import retrofit2.http.Body

interface IQuestion {

    @FormUrlEncoded
    @POST("payu/fetchNumberOfRows.php")
    fun getNumberOfRows(@Field("tableName") tableName: String): Observable<Questions.CountRows>

//    @FormUrlEncoded
//    @POST("payu/getQuestions.php")
//    fun getQuestion(
//            @Field("quesId") questionID: String,
//            @Field("tableName") tableName : String): Observable<Questions.Question>

    @POST("payu/getQuestions.php")
    fun getQuestions(@Body question: Questions.GetQuestion) : Observable<Questions.Question>
}