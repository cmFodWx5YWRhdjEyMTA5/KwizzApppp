package com.example.mayank.kwizzapp.sample

import android.content.Context
import android.widget.ArrayAdapter
import com.example.mayank.kwizzapp.models.SpinnerData
import com.example.mayank.kwizzapp.viewmodels.SettingVm

class CustomSpinnerAdapter(context: Context, resource: Int, spinnerData: List<SpinnerData>) : ArrayAdapter<SpinnerData>(context, resource, spinnerData) {

    var items: List<SpinnerData> = emptyList()
}