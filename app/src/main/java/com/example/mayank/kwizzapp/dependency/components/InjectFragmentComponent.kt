package com.example.mayank.kwizzapp.dependency.components

import com.example.mayank.kwizzapp.dependency.scopes.ActivityScope
import com.example.mayank.kwizzapp.userInfo.UserInfoFragment
import dagger.Component

@ActivityScope
@Component(dependencies = arrayOf(ApplicationComponent::class))
interface InjectFragmentComponent {
    fun injectUserInfoFragment(userInfoFragment: UserInfoFragment)
}