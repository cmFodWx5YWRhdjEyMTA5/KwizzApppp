package com.example.mayank.kwizzapp.singleplay

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mayank.kwizzapp.Constants

import com.example.mayank.kwizzapp.R
import net.rmitsolutions.mfexpert.lms.helpers.logD

class SinglePlayResultFragment : Fragment() {
    private var listener: OnFragmentInteractionListener? = null
    private var rightAnswers: Int? = 0
    private var wrongAnswers: Int? = 0
    private var dropQuestions: Int? = 0
    private var noOfQues: Int? = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            rightAnswers = it.getInt(Constants.RIGHT_ANSWERS)
            wrongAnswers = it.getInt(Constants.WRONG_ANSWERS)
            dropQuestions = it.getInt(Constants.DROP_QUESTIONS)
            noOfQues = it.getInt(NO_OF_QUES)
        }

        logD("Total Ques - $noOfQues\n" +
                "Right Answer - $rightAnswers\n" +
                "Wrong Answer - $wrongAnswers\n" +
                "Drop Ques - $dropQuestions")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_single_play_result, container, false)
        return view
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
    }
}
