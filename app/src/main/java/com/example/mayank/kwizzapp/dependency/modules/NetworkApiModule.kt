package com.example.mayank.kwizzapp.dependency.modules

import com.example.mayank.kwizzapp.dependency.scopes.ApplicationScope
import com.example.mayank.kwizzapp.network.IUser
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module(includes = arrayOf(HttpModule::class))
class NetworkApiModule {

    @Provides
    @ApplicationScope
    fun userService(retrofit: Retrofit): IUser {
        return retrofit.create(IUser::class.java)
    }
}