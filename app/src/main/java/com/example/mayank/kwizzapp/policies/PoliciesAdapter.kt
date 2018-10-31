package com.example.mayank.kwizzapp.policies

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.mayank.kwizzapp.R

class PoliciesAdapter: RecyclerView.Adapter<PoliciesViewHolder>() {

    var items: List<PoliciesVm> = emptyList()
    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PoliciesViewHolder {
        context = parent.context
        val v = LayoutInflater.from(context).inflate(R.layout.policies_row, parent, false)
        return PoliciesViewHolder(v)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: PoliciesViewHolder, position: Int) {
        val policies = items[position]
        holder.bindView(context,policies, position)
    }
}