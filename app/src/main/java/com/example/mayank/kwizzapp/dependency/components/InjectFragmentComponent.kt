package com.example.mayank.kwizzapp.dependency.components

import com.example.mayank.kwizzapp.dependency.scopes.ActivityScope
import com.example.mayank.kwizzapp.gamedetail.GameDetailFragment
import com.example.mayank.kwizzapp.gamemenu.GameMenuFragment
import com.example.mayank.kwizzapp.gameresult.GameResultFragment
import com.example.mayank.kwizzapp.profile.EditProfileFragment
import com.example.mayank.kwizzapp.quiz.QuizFragment
import com.example.mayank.kwizzapp.singleplay.SinglePlayDetails
import com.example.mayank.kwizzapp.singleplay.SinglePlayQuizFragment
import com.example.mayank.kwizzapp.transactions.TransactionFragment
import com.example.mayank.kwizzapp.userInfo.UserInfoFragment
import com.example.mayank.kwizzapp.wallet.AddPointsFragment
import com.example.mayank.kwizzapp.wallet.TransferPointsFragment
import com.example.mayank.kwizzapp.wallet.WalletMenuFragment
import com.example.mayank.kwizzapp.wallet.WithdrawalPointsFragment
import dagger.Component

@ActivityScope
@Component(dependencies = arrayOf(ApplicationComponent::class))
interface InjectFragmentComponent {
    fun injectUserInfoFragment(userInfoFragment: UserInfoFragment)
    fun injectGameMenuFragment(gameMenuFragment: GameMenuFragment)
    fun injectGameDetailFragment(gameDetailFragment: GameDetailFragment)
    fun injectQuizFragment(quizFragment: QuizFragment)
    fun injectGameResultFragment(gameResultFragment: GameResultFragment)
    fun injectWalletMenuFragment(walletMenuFragment: WalletMenuFragment)
    fun injectAddPointsFragment(addPointsFragment: AddPointsFragment)
    fun injectWithdrawalPointsFragment(withdrawalPointsFragment: WithdrawalPointsFragment)
    fun injectTransferPointsFragment(transferPointsFragment: TransferPointsFragment)
    fun injectSinglePlayQuizFragment(singlePlayQuizFragment: SinglePlayQuizFragment)
    fun injectEditProfileFragment(editProfileFragment: EditProfileFragment)
    fun injectTransactionFragment(transactionFragment: TransactionFragment)
}