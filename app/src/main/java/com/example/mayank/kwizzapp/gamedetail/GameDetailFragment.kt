package com.example.mayank.kwizzapp.gamedetail

import android.content.*
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.mayank.kwizzapp.KwizzApp

import com.example.mayank.kwizzapp.R
import com.example.mayank.kwizzapp.dependency.components.DaggerInjectFragmentComponent
import com.example.mayank.kwizzapp.helpers.processRequest
import com.example.mayank.kwizzapp.libgame.LibGameConstants.GameConstants.displayName
import com.example.mayank.kwizzapp.libgame.LibPlayGame
import com.example.mayank.kwizzapp.network.ITransaction
import com.example.mayank.kwizzapp.quiz.QuizFragment
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.game_detail_layout.*
import net.rmitsolutions.mfexpert.lms.helpers.*
import org.jetbrains.anko.find
import java.lang.IllegalArgumentException
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class GameDetailFragment : Fragment(), View.OnClickListener {
    private var listener: OnFragmentInteractionListener? = null

    @Inject
    lateinit var transactionService: ITransaction
    private lateinit var libPlayGame: LibPlayGame
    private var i = -1
    private var j: Int = 0
    private var k = -1
    private var l: Int = 0
    private var subject: String? = null
    private var subCode: String? = null
    private var amount: String? = null
    private lateinit var amountList: Array<String>
    private lateinit var subjectList: Array<String>
    private lateinit var subjectCode: Array<String>
    private lateinit var textSwitcherCountdown: TextSwitcher
    private lateinit var textViewCount: TextView
    private val syncIntentFilter = IntentFilter(ACTION_MESSAGE_RECEIVED)
    private var timerStatus = TimerStatus.STOPPED
    private var progressBar: ProgressBar? = null
    private var textViewSeconds: TextView? = null
    private var countDownTimer: CountDownTimer? = null
    private var timeCountInMilliSeconds = (1 * 10000).toLong()
    private val CLICKABLES = intArrayOf(R.id.imageButtonNextAmount, R.id.imageButtonNextSubject,
            R.id.imageButtonPreviousAmount, R.id.imageButtonPreviousSubject)
    private var textLabel: TextView? = null
    private var subtract: Boolean = false
    private lateinit var compositeDisposable: CompositeDisposable

    private enum class TimerStatus {
        STARTED,
        STOPPED
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
        val depComponent = DaggerInjectFragmentComponent.builder()
                .applicationComponent(KwizzApp.applicationComponent)
                .build()
        depComponent.injectGameDetailFragment(this)
        compositeDisposable = CompositeDisposable()
        libPlayGame = LibPlayGame(activity!!)
        subjectList = resources.getStringArray(R.array.subjectList)
        subjectCode = resources.getStringArray(R.array.subject_code)
        amountList = resources.getStringArray(R.array.amount)
        activity?.registerReceiver(messageBroadcastReceiver, syncIntentFilter);
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_game_detail, container, false)
        progressBar = view.find(R.id.progressBar)
        textViewSeconds = view.find(R.id.textViewSeconds)
        textLabel = view.find(R.id.textViewLabel)
        view.find<Button>(R.id.buttonLeaveRoom).setOnClickListener(this)
        for (id in CLICKABLES) {
            view.findViewById<ImageButton>(id).setOnClickListener(this)
        }
        return view
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.imageButtonNextAmount -> {
                nextAmount()
                libPlayGame.broadcastMessage('A', 0)
                reset()
            }

            R.id.imageButtonPreviousAmount -> {
                previousAmount()
                libPlayGame.broadcastMessage('A', 1)
                reset()

            }
            R.id.imageButtonPreviousSubject -> {
                previousSubject()
                libPlayGame.broadcastMessage('S', 1)
                reset()
            }

            R.id.imageButtonNextSubject -> {
                nextSubject()
                reset()
                libPlayGame.broadcastMessage('S', 0)
            }

            R.id.buttonLeaveRoom -> {
                libPlayGame.leaveRoom()
            }
        }
    }

    private val messageBroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (ACTION_MESSAGE_RECEIVED == intent.action) {
                val state = intent.getCharExtra("state", 'Z')
                val value = intent.getIntExtra("value", -1)
                logD("State - $state")
                logD("Value - $value")
                if (state == 'A') {
                    if (value == 0) {
                        nextAmount()
                    } else if (value == 1) {
                        previousAmount()
                    }
                    reset()
                } else if (state == 'S') {
                    if (value == 0) {
                        nextSubject()
                    } else if (value == 1) {
                        previousSubject()
                    }
                    reset()
                }
            }
        }
    }

    private fun nextAmount() {
        if (k < 20) {
            k++
            l = k
            amount = amountList[k]
            textViewAmount.text = amount
        } else {
            k = 0
            amount = amountList[k]
            textViewAmount.text = amount
        }
    }

    private fun previousAmount() {
        if (l > 0) {
            l--
            k = l
            amount = amountList[l]
            textViewAmount.text = amount
        } else {
            l = 20
            amount = amountList[l]
            textViewAmount.text = amount
        }
    }

    private fun nextSubject() {
        if (i < 6) {
            i++
            j = i
            subject = subjectList[i]
            subCode = subjectCode[i]
            textViewSubject.text = subject
        } else {
            i = 0
            subject = subjectList[i]
            subCode = subjectCode[i]
            textViewSubject.text = subject
        }
    }

    private fun previousSubject() {
        if (j > 0) {
            j--
            i = j
            subCode = subjectCode[j]
            subject = subjectList[j]
            textViewSubject.text = subject
        } else {
            j = 6
            subCode = subjectCode[j]
            subject = subjectList[j]
            textViewSubject.text = subject
        }
    }

    private fun unRegisterBroadcastReceiver() {
        try {
            if (messageBroadcastReceiver != null) {
                activity?.unregisterReceiver(messageBroadcastReceiver)
            }
        } catch (e: IllegalArgumentException) {
            logE("Error - $e")
        }


    }

    private fun reset() {
        textLabel?.visibility = View.VISIBLE
        if (timerStatus == TimerStatus.STOPPED) {
            setTimerValues()
            setProgressBarValue()
            timerStatus = TimerStatus.STARTED
            startCountdownTimer()
            return
        }
        stopCountdownTimer()
        startCountdownTimer()
    }

    private fun setTimerValues() {
        var time = 1
        // assigning values after converting to milliseconds
        timeCountInMilliSeconds = (time * 10 * 1000).toLong()
    }

    private fun startCountdownTimer() {
        countDownTimer = object : CountDownTimer(timeCountInMilliSeconds, 1000) {
            override fun onTick(millisUntilFinished: Long) {

                textViewSeconds!!.text = secondsFormatter(millisUntilFinished)
                progressBar!!.progress = (millisUntilFinished / 1000).toInt()

            }

            override fun onFinish() {

                timerStatus = TimerStatus.STOPPED

                if (amount == null) {
                    Toast.makeText(activity, "Select a valid Amount!", Toast.LENGTH_SHORT).show()
                } else if (subject == null) {
                    Toast.makeText(activity, "Select a valid subject!", Toast.LENGTH_SHORT).show()
                } else {
                    checkBalance()
                }
            }

        }.start()
        countDownTimer!!.start()
    }

    private fun checkBalance() {
        val mobileNumber = activity?.getPref(SharedPrefKeys.MOBILE_NUMBER, "")
        if (mobileNumber != "") {
            compositeDisposable.add(transactionService.checkBalance(mobileNumber!!)
                    .processRequest({ response ->
                        if (response.isSuccess) {
                            if (amount?.toDouble()!! <=response.balance && response.balance > 10) {
                                if (!subtract) {
                                    subtractBalance(displayName!!, amount?.toDouble(), Calendar.getInstance().time.toString())
                                    subtract = true
                                }
                            } else {
                                AlertDialog.Builder(activity!!).setTitle("Warning").setMessage(activity?.getString(R.string.insufficient_balance))
                                        .setPositiveButton("Ok") { dialog, which ->
                                            libPlayGame.leaveRoom()
                                            dialog.dismiss()
                                        }
                            }
                        }else{

                            AlertDialog.Builder(activity!!).setTitle("Warning").setMessage(response.message)
                                    .setPositiveButton("Ok") { dialog, which ->
                                        libPlayGame.leaveRoom()
                                        dialog.dismiss()
                                    }
                        }
                    }, { err ->
                        logD("Message - $err")
                    }))
        } else {
            showDialog(activity!!, "Error", "Update mobile number to continue!")
        }
    }

    private fun subtractBalance(displayName: String, amount: Double?, time: String) {
        compositeDisposable.add(transactionService.subtractResultBalance(displayName, amount!!, time)
                .processRequest(
                        { response ->
                            if (response.isSuccess) {
                                if (countDownTimer != null) {
                                    countDownTimer?.cancel()
                                }
                                val bundle = Bundle()
                                bundle.putString("Subject", subject)
                                bundle.putString("SubjectCode", subCode)
                                bundle.putDouble("Amount", amount)
                                val quizFragment = QuizFragment()
                                quizFragment.arguments = bundle
                                switchToFragment(quizFragment)
                                unRegisterBroadcastReceiver()
                            }else{
                                showDialog(activity!!,"Error", response.message)
                            }

                        }, { err ->
                    showDialog(activity!!, "Error", err.toString())

                }))
    }

    private fun stopCountdownTimer() {
        if (timerStatus == TimerStatus.STARTED) {
            countDownTimer?.cancel()
        }
    }

    private fun setProgressBarValue() {
        progressBar!!.max = timeCountInMilliSeconds.toInt() / 1000
        progressBar!!.progress = timeCountInMilliSeconds.toInt() * 1000
    }

    private fun secondsFormatter(milliSeconds: Long): String {
        return String.format("%02d", TimeUnit.MILLISECONDS.toSeconds(milliSeconds) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliSeconds)))
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
        const val ACTION_MESSAGE_RECEIVED = "com.technoholicdeveloper.kwizzapp.ACTION_MESSAGE_RECEIVED"
    }

}
