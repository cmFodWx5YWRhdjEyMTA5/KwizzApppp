package com.example.mayank.kwizzapp.policies


import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.mayank.kwizzapp.R

class PoliciesFragment : android.support.v4.app.Fragment() {
    private var listener: OnFragmentInteractionListener? = null

    private lateinit var recyclerView: RecyclerView
    val adapter: PoliciesAdapter by lazy { PoliciesAdapter() }
    lateinit var modelList: MutableList<PoliciesVm>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view =inflater.inflate(R.layout.fragment_policies, container, false)
        recyclerView = view.findViewById(R.id.policies_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.setHasFixedSize(true)
        recyclerView.addItemDecoration(DividerItemDecoration(activity, LinearLayoutManager.VERTICAL))
        recyclerView.adapter = adapter
        modelList = mutableListOf<PoliciesVm>()
        setPoliciesItem()
        return view
    }

    private fun setPoliciesItem() {
        modelList.clear()
        modelList.add(PoliciesVm(R.drawable.ic_info_black_24dp, "About KwizzApp"))
        modelList.add(PoliciesVm(R.drawable.ic_assignment_black_24dp, "Privacy Policy"))
        modelList.add(PoliciesVm(R.drawable.ic_error_black_24dp, "Terms & Conditions"))
        modelList.add(PoliciesVm(R.drawable.ic_perm_device_information_black_24dp, "Open Source Licenses"))
        setRecyclerViewAdapter(modelList)
    }

    private fun setRecyclerViewAdapter(list: List<PoliciesVm>) {
        adapter.items = list
        adapter.notifyDataSetChanged()
    }

    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
    }
}
