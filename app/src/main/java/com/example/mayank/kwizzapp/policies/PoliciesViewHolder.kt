package com.example.mayank.kwizzapp.policies

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.example.mayank.kwizzapp.R
import org.jetbrains.anko.find
import org.jetbrains.anko.toast

class PoliciesViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

    fun bindView(context: Context, policiesVm: PoliciesVm, position: Int){
        val textLabel = itemView.find<TextView>(R.id.policies_label)
        val imageIcon = itemView.find<ImageView>(R.id.policies_icon)

        textLabel.text = policiesVm.textLabel
        imageIcon.setImageResource(policiesVm.policiesIcon)

        itemView.setOnClickListener{
            when(position){
                0 ->{
                    context.toast("Coming soon...")
                }
                1 ->{
                    context.toast("Coming soon...")
                }
                2 ->{
                    context.toast("Coming soon...")
                }
                3 ->{
                    context.toast("Coming soon...")
                }
            }
        }
    }
}