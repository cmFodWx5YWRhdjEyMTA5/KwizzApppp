package com.example.mayank.kwizzapp.transactions

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.example.mayank.kwizzapp.R
import com.example.mayank.kwizzapp.bankdetail.BankDetailVm
import org.jetbrains.anko.find

class TransactionDetailViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

    fun bindView(transactionDetailVm: TransactionDetailsVm){
        val textUserName = itemView.find<TextView>(R.id.text_user_name)
        val textAmount = itemView.find<TextView>(R.id.text_transaction_amount)
        val textTimeStamp = itemView.find<TextView>(R.id.text_time_stamp)
        val textDescription = itemView.find<TextView>(R.id.text_description)
        val imageUser = itemView.find<ImageView>(R.id.user_image)

        textUserName.text = transactionDetailVm.textUserName
        textAmount.text = transactionDetailVm.textAmount
        textTimeStamp.text = transactionDetailVm.textTimeStamp
        textDescription.text = transactionDetailVm.textDescription
    }
}