package com.example.mayank.kwizzapp.profile

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
import net.rmitsolutions.mfexpert.lms.helpers.SharedPrefKeys
import net.rmitsolutions.mfexpert.lms.helpers.getPref
import net.rmitsolutions.mfexpert.lms.helpers.logD

class ProfileFragment : Fragment() {
    private var listener: OnFragmentInteractionListener? = null

    private lateinit var recyclerView: RecyclerView
    val adapter: ProfileViewAdapter by lazy { ProfileViewAdapter() }
    lateinit var modelList: MutableList<ProfileVm>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        recyclerView = view.findViewById(R.id.profile_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.setHasFixedSize(true)
        recyclerView.addItemDecoration(DividerItemDecoration(activity, LinearLayoutManager.VERTICAL))
        recyclerView.adapter = adapter
        modelList = mutableListOf<ProfileVm>()
        setSettingsItem()
        return view
    }

    private fun setSettingsItem() {
        modelList.clear()
        val firstName = activity?.getPref(SharedPrefKeys.FIRST_NAME, "")!!
        logD("First Name - $firstName")
        modelList.add(ProfileVm("First Name", firstName))
        modelList.add(ProfileVm("Last Name", activity?.getPref(SharedPrefKeys.LAST_NAME, "")!!))
        modelList.add(ProfileVm("Mobile Number", activity?.getPref(SharedPrefKeys.MOBILE_NUMBER, "")!!))
        modelList.add(ProfileVm("Email", activity?.getPref(SharedPrefKeys.EMAIL, "")!!))
        setRecyclerViewAdapter(modelList)

    }


    private fun setRecyclerViewAdapter(list: List<ProfileVm>) {
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
