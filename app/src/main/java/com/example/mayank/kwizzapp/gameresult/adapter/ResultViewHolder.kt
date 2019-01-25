package com.technoholicdeveloper.kwizzapp.result.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.example.mayank.kwizzapp.viewmodels.ResultViewModel
import com.google.android.gms.common.images.ImageManager
import android.graphics.drawable.Drawable
import com.google.android.gms.games.Player
import android.net.Uri
import com.example.mayank.kwizzapp.R


class ResultViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bindView(context : Context, resultViewModel: ResultViewModel, position: Int){
        val textViewPlayerName = itemView.findViewById<TextView>(R.id.textViewPlayerName)
        val textViewRightAnswer = itemView.findViewById<TextView>(R.id.textViewRightAnswer)
        val textViewWrongAnswer = itemView.findViewById<TextView>(R.id.textViewWrongAnswer)
        val textViewDropQuestion = itemView.findViewById<TextView>(R.id.textViewDropQuestion)
        val imageViewProfile = itemView.findViewById<ImageView>(R.id.playerDisplayImage)

        textViewPlayerName.text = resultViewModel.playerName
        textViewRightAnswer.text = resultViewModel.rightAnswers.toString()
        textViewWrongAnswer.text = resultViewModel.wrongAnswer.toString()
        textViewDropQuestion.text = resultViewModel.dropQuestion.toString()
//        imageViewProfile.setImageURI(resultViewModel.imageUri)


        val mgr = ImageManager.create(context)
//        mgr.loadImage(imageViewProfile, resultViewModel.imageUri)
        mgr.loadImage(imageViewProfile, resultViewModel.imageUri)


    }


}