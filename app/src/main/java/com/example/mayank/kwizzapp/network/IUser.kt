package com.example.mayank.kwizzapp.network

import com.example.mayank.kwizzapp.viewmodels.CommonResult
import com.example.mayank.kwizzapp.viewmodels.Users
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import java.util.*

interface IUser {

    @FormUrlEncoded
    @POST("payu/insertUserInfo.php")
    fun addDetails(
            @Field("firstName") firstName: String,
            @Field("lastName") lastName: String,
            @Field("displayName") displayName: String,
            @Field("playerId") playerId : String,
            @Field("mobileNumber") mobileNumber: String,
            @Field("email") email: String): Observable<CommonResult>

    @POST("payu/insertUserInfo.php")
    fun addDetailsNew(@Body userInfo: Users.UserInfo): Observable<CommonResult>
}