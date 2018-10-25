package com.example.mayank.kwizzapp.viewmodels

import android.net.Uri

class ResultViewModel(val playerName: String, val rightAnswers: Int, val imageUri: Uri?)

class SinglePlayResultVm(val totalQues: String, val trueQues: String, val falseQues: String, val dropQues: String)