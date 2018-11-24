package com.example.mayank.kwizzapp.dependency.components

import com.example.mayank.kwizzapp.sample.SampleActivity
import com.example.mayank.kwizzapp.dependency.scopes.ActivityScope
import com.example.mayank.kwizzapp.wallet.WalletActivity
import dagger.Component

@ActivityScope
@Component(dependencies = arrayOf(ApplicationComponent::class))
interface InjectActivityComponent {
    fun injectWalletActivity(walletActivity: WalletActivity)
    fun injectSampleActivity(sampleActivity: SampleActivity)
}
