package com.example.mayank.kwizzapp.gameresult

import com.example.mayank.kwizzapp.viewmodels.ResultViewModel

interface PlayerFinished {

    fun onPlayerFinished(list : MutableList<ResultViewModel>)
}