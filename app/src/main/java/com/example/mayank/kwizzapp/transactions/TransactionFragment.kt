package com.example.mayank.kwizzapp.transactions

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mayank.kwizzapp.Constants.getFormatDate

import com.example.mayank.kwizzapp.R
import org.jetbrains.anko.find
import java.util.*
import android.support.v7.app.AppCompatActivity
import com.example.mayank.googleplaygame.network.wallet.Transactions
import com.example.mayank.kwizzapp.Constants
import com.example.mayank.kwizzapp.KwizzApp
import com.example.mayank.kwizzapp.dependency.components.DaggerInjectFragmentComponent
import com.example.mayank.kwizzapp.helpers.processRequest
import com.example.mayank.kwizzapp.network.ITransaction
import io.reactivex.disposables.CompositeDisposable
import net.rmitsolutions.mfexpert.lms.helpers.*
import javax.inject.Inject


class TransactionFragment : Fragment() {
    private var listener: OnFragmentInteractionListener? = null
    private lateinit var recyclerView: RecyclerView
    val adapter: TransactionAdapter by lazy { TransactionAdapter() }
    lateinit var modelList: MutableList<TransactionDetailsVm>
    private lateinit var toolBar : Toolbar

    @Inject
    lateinit var transactionService: ITransaction
    private lateinit var compositeDisposable: CompositeDisposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val depComponent = DaggerInjectFragmentComponent.builder()
                .applicationComponent(KwizzApp.applicationComponent)
                .build()
        depComponent.injectTransactionFragment(this)
        compositeDisposable = CompositeDisposable()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view =inflater.inflate(R.layout.fragment_transaction, container, false)

        toolBar = view.find(R.id.toolbar)
        (activity as AppCompatActivity).setSupportActionBar(toolBar)
        recyclerView = view.findViewById(R.id.transactions_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.setHasFixedSize(true)
        recyclerView.addItemDecoration(DividerItemDecoration(activity, LinearLayoutManager.VERTICAL))
        recyclerView.adapter = adapter
        modelList = mutableListOf<TransactionDetailsVm>()
        setTransactionsItems()
        return view
    }

    private fun setTransactionsItems() {
        modelList.clear()
        val mobileNumber = activity?.getPref(SharedPrefKeys.MOBILE_NUMBER, "")
        if (mobileNumber != ""){
            compositeDisposable.add(transactionService.fetchTransactions(mobileNumber!!)
                    .processRequest(
                            { response ->
                                if (response.isSuccess){
                                    logD("${response.status}")
//                                    val transactions = TransactionDetailsVm()
//                                    if (response.transactionType!=null){
//                                        if (response.transactionType == Constants.TRANSACTION_TYPE_DEBITED){
//                                            transactions.textUserName = response.transferTo
//                                        }else{
//                                            transactions.textUserName = response.receivedFrom
//                                        }
//                                        transactions.textAmount = response.amount.toString()
//                                        transactions.textTimeStamp = response.createdOn
//                                        transactions.textDescription = response.status
//                                        modelList.add(transactions)
//                                    }
                                }else{
                                    toast(response.message)
                                }
                            },
                            { err ->
                                showDialog(activity!!, "Error", err.toString())
                            }
                    ))
            setRecyclerViewAdapter(modelList)
        }else{
            toast("Enter mobile number in settings")
        }

    }
    private fun setRecyclerViewAdapter(list: List<TransactionDetailsVm>) {
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
