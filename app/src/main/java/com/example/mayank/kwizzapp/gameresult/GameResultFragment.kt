package com.example.mayank.kwizzapp.gameresult

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Typeface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import com.example.mayank.googleplaygame.network.wallet.Transactions
import com.example.mayank.kwizzapp.Constants
import com.example.mayank.kwizzapp.Constants.DROP_QUESTIONS
import com.example.mayank.kwizzapp.Constants.RIGHT_ANSWERS
import com.example.mayank.kwizzapp.Constants.WRONG_ANSWERS
import com.example.mayank.kwizzapp.KwizzApp

import com.example.mayank.kwizzapp.R
import com.example.mayank.kwizzapp.dependency.components.DaggerInjectFragmentComponent
import com.example.mayank.kwizzapp.dialog.ProgressDialog
import com.example.mayank.kwizzapp.dialog.ShowDialog
import com.example.mayank.kwizzapp.gameresult.adapter.ResultViewAdapter
import com.example.mayank.kwizzapp.helpers.processRequest
import com.example.mayank.kwizzapp.libgame.LibGameConstants
import com.example.mayank.kwizzapp.libgame.LibGameConstants.GameConstants.imageUri
import com.example.mayank.kwizzapp.libgame.LibGameConstants.GameConstants.listResult
import com.example.mayank.kwizzapp.libgame.LibGameConstants.GameConstants.mFinishedParticipants
import com.example.mayank.kwizzapp.libgame.LibGameConstants.GameConstants.mMyId
import com.example.mayank.kwizzapp.libgame.LibGameConstants.GameConstants.mParticipantDrop
import com.example.mayank.kwizzapp.libgame.LibGameConstants.GameConstants.mParticipantScore
import com.example.mayank.kwizzapp.libgame.LibGameConstants.GameConstants.mParticipantWrong
import com.example.mayank.kwizzapp.libgame.LibGameConstants.GameConstants.mParticipants
import com.example.mayank.kwizzapp.libgame.LibGameConstants.GameConstants.mRoomId
import com.example.mayank.kwizzapp.libgame.LibGameConstants.GameConstants.modelList
import com.example.mayank.kwizzapp.libgame.LibGameConstants.GameConstants.resultList
import com.example.mayank.kwizzapp.libgame.LibPlayGame
import com.example.mayank.kwizzapp.network.ITransaction
import com.example.mayank.kwizzapp.quiz.AMOUNT
import com.example.mayank.kwizzapp.viewmodels.ResultViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.games.Games
import com.google.android.gms.games.multiplayer.Participant
import com.technoholicdeveloper.kwizzapp.achievements.Achievements
import io.reactivex.disposables.CompositeDisposable
import net.rmitsolutions.mfexpert.lms.helpers.*
import org.jetbrains.anko.find
import javax.inject.Inject

class GameResultFragment : Fragment(), View.OnClickListener{

    private var listener: OnFragmentInteractionListener? = null

    @Inject
    lateinit var transactionService: ITransaction
    private lateinit var compositeDisposable: CompositeDisposable

    private var rightAnswers: Int? = 0
    private var wrongAnswers: Int? = 0
    private var dropQuestions: Int? = 0
    private var amount: Double? = 0.0
    private lateinit var resultRecyclerView: RecyclerView
    val adapter: ResultViewAdapter by lazy { ResultViewAdapter() }
    private var libPlayGame: LibPlayGame? = null
    private lateinit var buttonBack: Button
    private lateinit var list: MutableList<ResultViewModel>
    private lateinit var showDialog: ShowDialog

    private lateinit var showResultProgress: ProgressDialog
    private val syncIntentFilter = IntentFilter(ACTION_RESULT_RECEIVED)
    private lateinit var resultLayout : LinearLayout
    private lateinit var achievements: Achievements
    private var win : Boolean = false
    private var displayName :  String? = null
    private lateinit var greetOne : TextView
    private lateinit var greetTwo : TextView
    private lateinit var greetThree : TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            rightAnswers = it.getInt(RIGHT_ANSWERS)
            wrongAnswers = it.getInt(WRONG_ANSWERS)
            dropQuestions = it.getInt(DROP_QUESTIONS)
            amount = it.getDouble(AMOUNT)
        }
        val depComponent = DaggerInjectFragmentComponent.builder()
                .applicationComponent(KwizzApp.applicationComponent)
                .build()
        depComponent.injectGameResultFragment(this)
        compositeDisposable = CompositeDisposable()

        libPlayGame = LibPlayGame(activity!!)
        resultList = mutableListOf<ResultViewModel>()
        showResultProgress = ProgressDialog()
        list = mutableListOf<ResultViewModel>()

        achievements = Achievements(activity!!)

        showDialog = ShowDialog()

        context?.registerReceiver(resultBroadcastReceiver, syncIntentFilter);
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_game_result, container, false)
        resultRecyclerView = view.findViewById(R.id.result_recycler_view)
        resultRecyclerView.layoutManager = LinearLayoutManager(activity)
        resultRecyclerView.setHasFixedSize(true)
        resultRecyclerView.addItemDecoration(DividerItemDecoration(activity, 0))
        //buttonBack = view.find(R.id.buttonBack)
        resultLayout = view.find(R.id.result_layout)
        resultLayout.visibility = View.GONE
        //buttonBack.setOnClickListener(this)
        resultRecyclerView.adapter = adapter
        greetOne = view.find(R.id.greetOne)
        greetTwo = view.find(R.id.greetTwo)
        greetThree = view.find(R.id.greetThree)
        showResultProgress.showResultProgress(activity!!)
        setItem()
        return view
    }


    override fun onClick(v: View?) {
        when (v?.id) {
//            R.id.buttonBack -> {
//                context?.unregisterReceiver(resultBroadcastReceiver)
//                libPlayGame?.leaveRoom()
//            }
        }
    }

    private fun setItem() {
        modelList.clear()
        displayName = activity?.getPref(SharedPrefKeys.DISPLAY_NAME, "")
        modelList.add(ResultViewModel(displayName!!, rightAnswers!!, imageUri, wrongAnswers!!, dropQuestions!!))
        logD("Player Image Uri - $imageUri")

        for (p in mParticipants!!) {
            if (mRoomId != null) {
                val pid = p.participantId
                if (pid == mMyId) {
                    mFinishedParticipants.add(pid)
                }
            }
        }
        updateScore()
    }

    private val resultBroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (GameResultFragment.ACTION_RESULT_RECEIVED == intent.action) {
                updateScore()
            }
        }
    }

    private var show: Boolean = false

    private fun updateScore() {
        logD("Inside Update Score")
        if (mRoomId != null) {
            for (p in mParticipants!!) {
                val pid = p.participantId
                if (pid == mMyId) {
                    continue
                }
                if (p.status != Participant.STATUS_JOINED) {
                    continue
                }

                if (mParticipants?.size != mFinishedParticipants.size) {
                    continue
                }

                val score = if (mParticipantScore.containsKey(pid)) mParticipantScore[pid] else 0
                val wrong = if (mParticipantWrong.containsKey(pid)) mParticipantWrong[pid] else 0
                val drop = if (mParticipantDrop.containsKey(pid)) mParticipantDrop[pid] else 0

                if (mParticipants?.size == mFinishedParticipants.size) {
                    logD("Participants and finished participants are equal")
                    if (p.displayName != displayName) {
                        logD("Display name are equal")
                        if (!listResult.containsKey(p.displayName)) {
                            listResult[p.displayName] = ResultViewModel(p.displayName, score!!, p.hiResImageUri, wrong!!, drop!!)
                            logD("Opponent Image Uri - ${p.hiResImageUri}")
                            val resultViewModel = listResult[p.displayName]
                            modelList.add(resultViewModel!!)
                        }
                        // If model list is equals finishedParticipants size then arrange list in descending order
                        if (modelList.size == mFinishedParticipants.size){
                            resultList = modelList.sortedByDescending {
                                it.rightAnswers
                            }.toMutableList()
                            showResultDialog(resultList)
                            showResultProgress.hideProgressDialog()
                            //buttonBack.visibility = View.VISIBLE
                        }

                    } else {
                        logD("Display name are same")
                    }
                } else {
                }
            }
            setRecyclerViewAdapter(resultList!!)
            // Check this where to write coz of this also will be a problem
        }else{
            logD("Room Id is null")
        }
    }

    private fun showResultDialog(resultList: MutableList<ResultViewModel>?) {
        if (resultList!![0].playerName == displayName) {
            logD("List 0 score - ${resultList[0].rightAnswers} List 1 score - ${resultList[1].rightAnswers}")
            val updateResult = Transactions.ResultBalance()
            if (resultList[0].rightAnswers == resultList[1].rightAnswers) {
                if (!show) {
                    win = false
                    //showDialogResult(activity!!, "Sorry", "It's a Tie","Your bid points will credited to your wallet", R.mipmap.ic_loose)
                    updateResult.displayName = displayName
                    updateResult.amount = amount
                    updateResult.timeStamp = System.currentTimeMillis().toString()
                    updateResult.productInfo = "Points refund due to tie in Quiz."
                    updateBalance(updateResult)
                    show = true
                    resultLayout.visibility = View.VISIBLE
                }
            } else {
                if (!show) {
                    win = true
                    //showDialogResult(activity!!,"Congrats", "You Win !", "Your winning points will credited to your wallet", R.mipmap.ic_done)
                    val totalAmount = (amount?.times(mFinishedParticipants.size))?.times(80)?.div(100)
                    updateResult.displayName = displayName
                    updateResult.amount = totalAmount
                    updateResult.timeStamp = System.currentTimeMillis().toString()
                    updateResult.productInfo = "You have won for Quiz."
                    updateBalance(updateResult)
                    show = true
                    resultLayout.visibility = View.VISIBLE
                }
            }
        } else {
            if (!show) {
                win = false
                //showDialogResult(activity!!, "Sorry", "You Loose !", "Better luck next time", R.mipmap.ic_loose)
                show = true
                greetThree.textSize = 14F
                greetThree.typeface = Typeface.DEFAULT
                setGreetingText("Sorry", "You Loose", "Better luck next time")
                resultLayout.visibility = View.VISIBLE
                achievements.checkAchievements(0, LibGameConstants.GameConstants.resultList?.size!!,win, rightAnswers!!)
            }
        }
    }

    private fun submitScoreToLeaderboards(score : Long){
        Games.getLeaderboardsClient(activity!!, GoogleSignIn.getLastSignedInAccount(activity)!!)
                .submitScore(getString(R.string.leaderboard_top_scores), score * 1000000);
    }

    private fun showDialogResult(activity: Activity, bigTitle: String, smallTitle : String, message: String, imageResource : Int){
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N_MR1){
            showDialog.showResultDialog(activity, bigTitle,smallTitle,message,imageResource)
        }else{
            showDialog(activity, bigTitle, "$smallTitle\n\n$message")
        }
    }


    private fun updateBalance(updateResult : Transactions.ResultBalance) {
        compositeDisposable.add(transactionService.updateResultBalance(updateResult)
                .processRequest(
                        { response ->
                            if (response.isSuccess) {
                                logD("Message = ${response.message}")
                                logD("Response balance = ${response.balance}")
                                submitScoreToLeaderboards(response.balance.toLong())
                                achievements.checkAchievements(response.balance.toInt(), resultList?.size!!,win, rightAnswers!!)
                                if (updateResult.amount.toString().toDouble() > amount!!){
                                    setGreetingText("Congratulations", "You've won", "${updateResult.amount}")
                                }else{
                                    greetThree.textSize = 14F
                                    greetThree.typeface = Typeface.DEFAULT
                                    setGreetingText("Sorry", "It's a tie", "Try one more time")
                                }
                            } else {
                                logD("Message - ${response.message}")
                            }
                        },
                        { error ->
                            logD("$error")
                        }
                ))
    }

    private fun setGreetingText(greet1 : String, greet2 : String, greet3 : String){
        greetOne.text = greet1
        greetTwo.text = greet2
        greetThree.text = greet3
    }

    // Update game status
    private fun updateGameStatus() {

    }

    override fun onDestroy() {
        super.onDestroy()
        //libPlayGame?.leaveRoom()
        context?.unregisterReceiver(resultBroadcastReceiver)
    }

    private fun setRecyclerViewAdapter(list: List<ResultViewModel>) {
        adapter.items = list
        adapter.notifyDataSetChanged()
    }

    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        const val ACTION_RESULT_RECEIVED = "com.technoholicdeveloper.kwizzapp.ACTION_RESULT_RECEIVED"
    }
}
