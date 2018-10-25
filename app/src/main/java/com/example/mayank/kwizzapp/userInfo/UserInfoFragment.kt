package com.example.mayank.kwizzapp.userInfo

import android.content.Context
import android.databinding.DataBindingUtil
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.mayank.kwizzapp.KwizzApp

import com.example.mayank.kwizzapp.R
import com.example.mayank.kwizzapp.dashboard.DashboardFragment
import com.example.mayank.kwizzapp.databinding.UserInfoBinding
import com.example.mayank.kwizzapp.dependency.components.DaggerInjectFragmentComponent
import com.example.mayank.kwizzapp.helpers.processRequest
import com.example.mayank.kwizzapp.network.IUser
import com.example.mayank.kwizzapp.viewmodels.Users
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_user_info.*
import net.rmitsolutions.mfexpert.lms.helpers.*
import net.rmitsolutions.mfexpert.lms.helpers.SharedPrefKeys.DISPLAY_NAME
import org.jetbrains.anko.find
import javax.inject.Inject
import android.util.Patterns
import android.text.TextUtils




class UserInfoFragment : Fragment(), View.OnClickListener {

    @Inject
    lateinit var userService: IUser
    private var listener: OnFragmentInteractionListener? = null
    private lateinit var dataBinding: UserInfoBinding
    private lateinit var userInfoVm: Users.UserInfo
    private lateinit var submitData: Button
    private lateinit var compositeDisposable: CompositeDisposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        compositeDisposable = CompositeDisposable()
        val depComponent = DaggerInjectFragmentComponent.builder()
                .applicationComponent(KwizzApp.applicationComponent)
                .build()
        depComponent.injectUserInfoFragment(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_info, container, false)
        val view = dataBinding.root
        userInfoVm = Users.UserInfo()
        dataBinding.userInfoVm = userInfoVm
        val firstName = activity?.getPref(SharedPrefKeys.FIRST_NAME, "")
        val lastName = activity?.getPref(SharedPrefKeys.LAST_NAME, "")
        when {
            firstName != "" || lastName != "" -> {
                dataBinding.userInfoVm!!.firstName.set(firstName)
                dataBinding.userInfoVm!!.lastName.set(lastName)
            }
        }
        submitData = view.find(R.id.buttonSubmitInfo)
        submitData.setOnClickListener(this)
        return view
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.buttonSubmitInfo -> {
                val playerId = activity?.getPref(SharedPrefKeys.PLAYER_ID, "")
                dataBinding.userInfoVm
                dataBinding.userInfoVm?.displayName = activity?.getPref(DISPLAY_NAME, "")
                if (validate()) {
                    compositeDisposable.add(userService.addDetails(dataBinding.userInfoVm?.firstName?.get()!!,
                            dataBinding.userInfoVm?.lastName?.get()!!, dataBinding.userInfoVm?.displayName!!, playerId!!,
                            dataBinding.userInfoVm?.mobileNumber!!, dataBinding.userInfoVm?.email!!).processRequest(
                            { response ->
                                if (response.isSuccess) {
                                    toast(response.message)
                                    switchToDashboard(dataBinding.userInfoVm!!)
                                } else {
                                    toast(response.message)
                                }
                            }, { err ->
                        logD("Error - ${err.toString()}")
                    }))
                }
            }
        }
    }

    private fun validate(): Boolean {
        when {
            dataBinding.userInfoVm?.firstName?.get().isNullOrBlank() -> {
                textInputLayoutFirstName.error = "Enter First Name."
                return false
            }
            else -> textInputLayoutFirstName.error = null
        }

        when {
            dataBinding.userInfoVm?.lastName?.get().isNullOrBlank() -> {
                textInputLayoutLastName.error = "Enter Last Name."
                return false
            }
            else -> textInputLayoutLastName.error = null
        }
        when {
            dataBinding.userInfoVm?.mobileNumber.isNullOrBlank() -> {
                textInputLayoutMobileNumber.error = "Enter Mobile Number."
                return false
            }
            else -> textInputLayoutMobileNumber.error = null
        }
        when {
            dataBinding.userInfoVm?.email.isNullOrBlank() -> {
                textInputLayoutEmail.error = "Enter Email"
                return false
            }
            else -> textInputLayoutEmail.error = null
        }

        when {
            !isValidEmail(dataBinding.userInfoVm?.email!!) -> {
                textInputLayoutEmail.error = "Enter valid Email Address"
                return false
            }
            else -> textInputLayoutEmail.error = null
        }

        return true
    }

    fun isValidEmail(target: CharSequence): Boolean {
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches()
    }

    private fun switchToDashboard(userInfoVm: Users.UserInfo) {
        activity?.putPref(SharedPrefKeys.FIRST_NAME, userInfoVm.firstName.get())
        activity?.putPref(SharedPrefKeys.LAST_NAME, userInfoVm.lastName.get())
        activity?.putPref(SharedPrefKeys.MOBILE_NUMBER, userInfoVm.mobileNumber)
        activity?.putPref(SharedPrefKeys.EMAIL, userInfoVm.email)
        val dashboardFrag = DashboardFragment()
        switchToFragment(dashboardFrag)
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).supportActionBar?.hide()
    }

    override fun onStop() {
        super.onStop()
        (activity as AppCompatActivity).supportActionBar?.show()
    }

    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        when (context) {
            is OnFragmentInteractionListener -> listener = context
            else -> throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
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
