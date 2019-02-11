package com.example.mayank.kwizzapp.network

import com.example.mayank.kwizzapp.sample.User
import com.example.mayank.kwizzapp.viewmodels.CommonResult
import com.example.mayank.kwizzapp.viewmodels.Users
import io.reactivex.Observable
import org.json.JSONObject
import retrofit2.http.*

interface IUser {
    // Working
    @POST("payu/updateProfileInfo.php")
    fun updateUserInfo(@Body user: Users.UpdateUserInfo) : Observable<CommonResult>

    @POST("payu/insertUser.php")
    fun insertUser(@Body user: User) : Observable<CommonResult>

    // Working
    @POST("payu/insertUserInfo.php")
    fun insertUserInfo(@Body user: Users.InsertUserInfo) : Observable<CommonResult>

    @POST("payu/updateDisplayName.php")
    fun updateDisplayName(@Body updateDisplayName: Users.UpdateDisplayName) : Observable<CommonResult>

    @POST("payu/getProfileData.php")
    fun getProfileData(@Body profile : Users.Profile) : Observable<Users.ProfileData>
}