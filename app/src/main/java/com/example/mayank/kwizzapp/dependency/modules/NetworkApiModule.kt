package com.example.mayank.kwizzapp.dependency.modules

import com.example.mayank.kwizzapp.dependency.scopes.ApplicationScope
import com.example.mayank.kwizzapp.network.*
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

    @Provides
    @ApplicationScope
    fun transactionService(retrofit: Retrofit): ITransaction {
        return retrofit.create(ITransaction::class.java)
    }

    @Provides
    @ApplicationScope
    fun questionService(retrofit: Retrofit): IQuestion {
        return retrofit.create(IQuestion::class.java)
    }

    @Provides
    @ApplicationScope
    fun razorpayService(retrofit: Retrofit): IRazorpay {
        return retrofit.create(IRazorpay::class.java)
    }

    @Provides
    @ApplicationScope
    fun paytmService(retrofit: Retrofit): IPaytm {
        return retrofit.create(IPaytm::class.java)
    }
}