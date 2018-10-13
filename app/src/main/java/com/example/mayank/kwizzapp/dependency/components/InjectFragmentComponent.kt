package com.example.mayank.kwizzapp.dependency.components

import com.example.mayank.kwizzapp.dependency.scopes.ActivityScope
import com.example.mayank.kwizzapp.gamedetail.GameDetailFragment
import com.example.mayank.kwizzapp.gamemenu.GameMenuFragment
import com.example.mayank.kwizzapp.quiz.QuizFragment
import com.example.mayank.kwizzapp.userInfo.UserInfoFragment
import dagger.Component

@ActivityScope
@Component(dependencies = arrayOf(ApplicationComponent::class))
interface InjectFragmentComponent {
    fun injectUserInfoFragment(userInfoFragment: UserInfoFragment)
    fun injectGameMenuFragment(gameMenuFragment: GameMenuFragment)
    fun injectGameDetailFragment(gameDetailFragment: GameDetailFragment)
    fun injectQuizFragment(quizFragment: QuizFragment)
}