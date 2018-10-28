package com.example.mayank.kwizzapp.profile

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.mayank.kwizzapp.R

class ProfileViewAdapter: RecyclerView.Adapter<ProfileViewHolder>() {

    var items: List<ProfileVm> = emptyList()
    private lateinit var context: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileViewHolder {
        context = parent.context
        val v = LayoutInflater.from(context).inflate(R.layout.profile_row, parent, false)
        return ProfileViewHolder(v)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ProfileViewHolder, position: Int) {
        val profileVm = items[position]
        holder.bindView(profileVm)
    }
}