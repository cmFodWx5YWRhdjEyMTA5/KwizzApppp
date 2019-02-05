package com.example.mayank.kwizzapp.transactions

import android.content.Context
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.example.mayank.kwizzapp.Constants
import com.example.mayank.kwizzapp.R
import com.example.mayank.kwizzapp.helpers.Converters
import org.jetbrains.anko.find

class TransactionDetailViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

    fun bindView(context : Context, transactionDetailVm: TransactionDetailsVm){
        val textUserName = itemView.find<TextView>(R.id.text_user_name)
        val textAmount = itemView.find<TextView>(R.id.text_transaction_amount)
        val textTimeStamp = itemView.find<TextView>(R.id.text_time_stamp)
        val textDescription = itemView.find<TextView>(R.id.text_description)
        val imageUser = itemView.find<ImageView>(R.id.user_image)

        textUserName.text = transactionDetailVm.textUserName
        textTimeStamp.text = "${Constants.getDateFormat(Converters.fromTimestamp(transactionDetailVm.textTimeStamp?.toLong())!!)}"
        textDescription.text = transactionDetailVm.textDescription
        if (transactionDetailVm.transactionType.toString() == "Debited"){
            textAmount.text = "- ${transactionDetailVm.textAmount}"
            textAmount.setTextColor(Color.RED)
        }else{
            textAmount.text = "+ ${transactionDetailVm.textAmount}"
            textAmount.setTextColor(context.resources.getColor(R.color.colorGreen))
        }

    }
}