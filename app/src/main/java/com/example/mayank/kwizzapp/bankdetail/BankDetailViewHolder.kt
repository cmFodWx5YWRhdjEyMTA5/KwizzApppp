package com.example.mayank.kwizzapp.bankdetail

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.example.mayank.kwizzapp.R
import org.jetbrains.anko.find

class BankDetailViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
    fun bindView(bankDetailVm: BankDetailVm){
        val textLabel = itemView.find<TextView>(R.id.textLabelProfile)
        val textData = itemView.find<TextView>(R.id.textDataProfile)

        textLabel.text = bankDetailVm.textLabel
        textData.text = bankDetailVm.textData
    }
}