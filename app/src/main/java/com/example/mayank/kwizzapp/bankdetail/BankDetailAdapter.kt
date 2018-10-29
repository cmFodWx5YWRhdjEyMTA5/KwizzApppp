package com.example.mayank.kwizzapp.bankdetail

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.mayank.kwizzapp.R

class BankDetailAdapter: RecyclerView.Adapter<BankDetailViewHolder>() {

    var items: List<BankDetailVm> = emptyList()
    private lateinit var context: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BankDetailViewHolder {
        context = parent.context
        val v = LayoutInflater.from(context).inflate(R.layout.profile_row, parent, false)
        return BankDetailViewHolder(v)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: BankDetailViewHolder, position: Int) {
        val profileVm = items[position]
        holder.bindView(profileVm)
    }
}