package com.example.mayank.kwizzapp.bankdetail

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*

import com.example.mayank.kwizzapp.R
import net.rmitsolutions.mfexpert.lms.helpers.SharedPrefKeys
import net.rmitsolutions.mfexpert.lms.helpers.getPref
import net.rmitsolutions.mfexpert.lms.helpers.switchToFragmentBackStack

class BankDetailFragment : Fragment() {
    private var listener: OnFragmentInteractionListener? = null
    private lateinit var recyclerView: RecyclerView
    val adapter: BankDetailAdapter by lazy { BankDetailAdapter() }
    lateinit var modelList: MutableList<BankDetailVm>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view =inflater.inflate(R.layout.fragment_bank_detail, container, false)
        recyclerView = view.findViewById(R.id.bank_detail_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.setHasFixedSize(true)
        recyclerView.addItemDecoration(DividerItemDecoration(activity, LinearLayoutManager.VERTICAL))
        recyclerView.adapter = adapter
        modelList = mutableListOf<BankDetailVm>()
        setBankDetailItems()
        return view
    }

    private fun setBankDetailItems() {
        modelList.clear()
        modelList.add(BankDetailVm("First Name", activity?.getPref(SharedPrefKeys.FIRST_NAME, "")!!))
        modelList.add(BankDetailVm("Last Name", activity?.getPref(SharedPrefKeys.LAST_NAME, "")!!))
        modelList.add(BankDetailVm("Account Number", activity?.getPref(SharedPrefKeys.ACCOUNT_NUMBER, "")!!))
        modelList.add(BankDetailVm("IFSC Code", activity?.getPref(SharedPrefKeys.IFSC_CODE, "")!!))
        setRecyclerViewAdapter(modelList)
    }
    private fun setRecyclerViewAdapter(list: List<BankDetailVm>) {
        adapter.items = list
        adapter.notifyDataSetChanged()
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        activity?.menuInflater?.inflate(R.menu.menu_profile, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.edit -> {
                val editBankDetailsFragment = EditBankDetailsFragment()
                switchToFragmentBackStack(editBankDetailsFragment)
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
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
