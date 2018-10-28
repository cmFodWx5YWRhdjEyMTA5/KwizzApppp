package com.example.mayank.kwizzapp.profile

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.example.mayank.kwizzapp.R
import org.jetbrains.anko.find

class ProfileViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
    fun bindView(profileVm: ProfileVm){
        val textLabel = itemView.find<TextView>(R.id.textLabelProfile)
        val textData = itemView.find<TextView>(R.id.textDataProfile)

        textLabel.text = profileVm.textLabel
        textData.text = profileVm.textData
    }
}