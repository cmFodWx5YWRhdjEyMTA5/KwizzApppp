package com.example.mayank.kwizzapp.settings.menusettings

import android.content.Context
import android.support.v7.widget.RecyclerView
import com.example.mayank.kwizzapp.databinding.SettingMenuBinding
import com.example.mayank.kwizzapp.viewmodels.SettingVm

class SettingMenuViewHolder (val dataBinding: SettingMenuBinding) : RecyclerView.ViewHolder(dataBinding.root) {

    fun bindView(context : Context, settingMenuVm: SettingVm.SettingMenuVm){
        dataBinding.settingMenuVm = settingMenuVm
    }
}