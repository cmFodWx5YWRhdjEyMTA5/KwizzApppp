package com.example.mayank.kwizzapp.gameresult

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.mayank.kwizzapp.KwizzApp

import com.example.mayank.kwizzapp.R
import com.example.mayank.kwizzapp.dependency.components.DaggerInjectFragmentComponent
import com.example.mayank.kwizzapp.dialog.ProgressDialog
import com.example.mayank.kwizzapp.gameresult.adapter.ResultViewAdapter
import com.example.mayank.kwizzapp.helpers.processRequest
import com.example.mayank.kwizzapp.libgame.LibGameConstants.GameConstants.displayName
import com.example.mayank.kwizzapp.libgame.LibGameConstants.GameConstants.imageUri
import com.example.mayank.kwizzapp.libgame.LibGameConstants.GameConstants.listResult
import com.example.mayank.kwizzapp.libgame.LibGameConstants.GameConstants.mFinishedParticipants
import com.example.mayank.kwizzapp.libgame.LibGameConstants.GameConstants.mMyId
import com.example.mayank.kwizzapp.libgame.LibGameConstants.GameConstants.mParticipantScore
import com.example.mayank.kwizzapp.libgame.LibGameConstants.GameConstants.mParticipants
import com.example.mayank.kwizzapp.libgame.LibGameConstants.GameConstants.mRoomId
import com.example.mayank.kwizzapp.libgame.LibGameConstants.GameConstants.modelList
import com.example.mayank.kwizzapp.libgame.LibGameConstants.GameConstants.resultList
import com.example.mayank.kwizzapp.libgame.LibPlayGame
import com.example.mayank.kwizzapp.network.ITransaction
import com.example.mayank.kwizzapp.quiz.AMOUNT
import com.example.mayank.kwizzapp.quiz.DROP_QUESTIONS
import com.example.mayank.kwizzapp.quiz.RIGHT_ANSWERS
import com.example.mayank.kwizzapp.quiz.WRONG_ANSWERS
import com.example.mayank.kwizzapp.viewmodels.ResultViewModel
import com.google.android.gms.games.multiplayer.Participant
import io.reactivex.disposables.CompositeDisposable
import net.rmitsolutions.mfexpert.lms.helpers.logD
import net.rmitsolutions.mfexpert.lms.helpers.showDialog
import org.jetbrains.anko.find
import java.util.*
import javax.inject.Inject

class GameResultFragment : Fragment(), View.OnClickListener {

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

    private lateinit var showResultProgress: ProgressDialog
    private val syncIntentFilter = IntentFilter(ACTION_RESULT_RECEIVED)

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

        context?.registerReceiver(resultBroadcastReceiver, syncIntentFilter);
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_game_result, container, false)
        resultRecyclerView = view.findViewById(R.id.result_recycler_view)
        resultRecyclerView.layoutManager = LinearLayoutManager(activity)
        resultRecyclerView.setHasFixedSize(true)
        resultRecyclerView.addItemDecoration(DividerItemDecoration(activity, LinearLayoutManager.VERTICAL))
        buttonBack = view.find(R.id.buttonBack)
        buttonBack.setOnClickListener(this)
        resultRecyclerView.adapter = adapter
        showResultProgress.showResultProgress(activity!!)
        setItem()
        return view
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.buttonBack -> {
                libPlayGame?.leaveRoom()
            }
        }
    }

    private fun setItem() {
        modelList.clear()
        modelList.add(ResultViewModel(displayName!!, rightAnswers!!, imageUri))

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

                if (mParticipants?.size == mFinishedParticipants.size) {
                    if (p.displayName != displayName) {
                        if (!listResult.containsKey(p.displayName)) {
                            listResult[p.displayName] = ResultViewModel(p.displayName, score!!, p.iconImageUri)
                            val resultViewModel = listResult[p.displayName]
                            modelList.add(resultViewModel!!)
                        }
                        resultList = modelList.sortedByDescending {
                            it.rightAnswers
                        }.toMutableList()

                        showResultDialog(resultList)

                    } else {
                        logD("Display name are same")
                    }
                } else {
                }
                showResultProgress.hideProgressDialog()
                buttonBack.visibility = View.VISIBLE
            }
            setRecyclerViewAdapter(resultList!!)
        }
    }

    private fun showResultDialog(resultList: MutableList<ResultViewModel>?) {
        if (resultList!![0].playerName == displayName) {
            if (resultList[0].rightAnswers == resultList[1].rightAnswers) {
                if (!show) {
                    showDialog(activity!!, "Result", "Sorry! Its a Tie!\nYour bid points will credited to your wallet.")
                    updateBalance(displayName!!, amount!!, Calendar.getInstance().time.toString(), "Sample Message")
                    show = true
                }
            } else {
                if (!show) {
                    showDialog(activity!!, "Result", "Congrats! You Win!\nYour winning points will credited to your wallet.")
                    val totalAmount = (amount?.times(mFinishedParticipants.size))?.times(80)?.div(100)
                    updateBalance(displayName!!, totalAmount!!, Calendar.getInstance().time.toString(), "Sample Message")
                    show = true
                }
            }
        } else {
            if (!show) {
                showDialog(activity!!, "Result", "Sorry! You Loose")
                show = true
            }
        }
    }


    private fun updateBalance(displayName: String, amount: Double, timeStamp: String, message: String) {
        compositeDisposable.add(transactionService.updateResultBalance(displayName, amount, timeStamp)
                .processRequest(
                        { response ->
                            if (response.isSuccess) {
                                logD("Message = ${response.message}")
                                logD("Response balance = ${response.balance}")
                            } else {
                                logD("Message - ${response.message}")
                            }
                        },
                        { error ->
                            logD("$error")
                        }
                ))
    }

    private fun updateGameStatus() {

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
