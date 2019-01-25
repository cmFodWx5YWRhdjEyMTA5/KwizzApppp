package com.example.mayank.kwizzapp.viewmodels

import android.net.Uri

class ResultViewModel(val playerName: String, val rightAnswers: Int, val imageUri: Uri?, val wrongAnswer : Int, val dropQuestion : Int)

class SinglePlayResultVm(var totalQues: String, var trueQues: String, var falseQues: String, var dropQues: String)