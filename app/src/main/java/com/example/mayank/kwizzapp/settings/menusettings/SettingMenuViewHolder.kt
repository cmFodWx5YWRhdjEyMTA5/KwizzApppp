package com.example.mayank.kwizzapp.settings.menusettings

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.example.mayank.kwizzapp.R
import com.example.mayank.kwizzapp.databinding.SettingMenuBinding
import com.example.mayank.kwizzapp.viewmodels.SettingVm
import org.jetbrains.anko.find

class SettingMenuViewHolder (itemView : View) : RecyclerView.ViewHolder(itemView) {

    fun bindView(context : Context, settingMenuVm: SettingVm.SettingMenuVm){
        val textTitle = itemView.find<TextView>(R.id.setting_header_name)
        val imageIcon = itemView.find<ImageView>(R.id.setting_icon)

        textTitle.text = settingMenuVm.title
        imageIcon.setImageResource(settingMenuVm.imageSource)
    }
}