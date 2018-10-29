package com.example.mayank.kwizzapp.profile

import android.content.Context
import android.databinding.DataBindingUtil
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.mayank.kwizzapp.KwizzApp

import com.example.mayank.kwizzapp.R
import com.example.mayank.kwizzapp.databinding.EditProfileBinding
import com.example.mayank.kwizzapp.dependency.components.DaggerInjectFragmentComponent
import com.example.mayank.kwizzapp.helpers.processRequest
import com.example.mayank.kwizzapp.network.IUser
import com.example.mayank.kwizzapp.settings.SettingsActivity
import com.example.mayank.kwizzapp.viewmodels.Users
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_user_info.*
import net.rmitsolutions.mfexpert.lms.helpers.SharedPrefKeys
import net.rmitsolutions.mfexpert.lms.helpers.getPref
import net.rmitsolutions.mfexpert.lms.helpers.showDialog
import net.rmitsolutions.mfexpert.lms.helpers.toast
import org.jetbrains.anko.find
import org.jetbrains.anko.support.v4.startActivity
import javax.inject.Inject

class EditProfileFragment : Fragment(), View.OnClickListener {

    private var listener: OnFragmentInteractionListener? = null
    @Inject
    lateinit var userService: IUser
    private lateinit var dataBinding: EditProfileBinding
    private lateinit var userInfoVm: Users.UserInfo
    private lateinit var updateData: Button
    private lateinit var compositeDisposable: CompositeDisposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val depComponent = DaggerInjectFragmentComponent.builder()
                .applicationComponent(KwizzApp.applicationComponent)
                .build()
        depComponent.injectEditProfileFragment(this)
        compositeDisposable = CompositeDisposable()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_profile, container, false)
        val view = dataBinding.root
        userInfoVm = Users.UserInfo()
        dataBinding.userInfoVm = userInfoVm
        dataBinding.editTextMobileNumber.keyListener = null
        dataBinding.editTextMobileNumber.setOnClickListener {
            toast("Sorry! Mobile number cannot be change.")
        }
        val firstName = activity?.getPref(SharedPrefKeys.FIRST_NAME, "")
        val lastName = activity?.getPref(SharedPrefKeys.LAST_NAME, "")
        val mobileNumber = activity?.getPref(SharedPrefKeys.MOBILE_NUMBER, "")
        val email = activity?.getPref(SharedPrefKeys.EMAIL, "")
        when {
            firstName != "" || lastName != "" || mobileNumber != "" || email != "" -> {
                dataBinding.userInfoVm!!.firstName.set(firstName)
                dataBinding.userInfoVm!!.lastName.set(lastName)
                dataBinding.userInfoVm?.mobileNumber = mobileNumber
                dataBinding.userInfoVm?.email = email
            }
        }

        updateData = view.find(R.id.buttonUpdateInfo)
        updateData.setOnClickListener(this)
        return view
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.buttonUpdateInfo -> updateInfo()
        }
    }

    private fun updateInfo() {
        if (validate()) {
            compositeDisposable.add(userService.updateProfileInfo(dataBinding.userInfoVm?.firstName?.get()!!,
                    dataBinding.userInfoVm?.lastName?.get()!!, dataBinding.userInfoVm?.mobileNumber!!, dataBinding.userInfoVm?.email!!)
                    .processRequest(
                            { response ->
                                if (response.isSuccess) {
                                    toast(response.message)
                                    startActivity<SettingsActivity>()
                                } else {
                                    toast(response.message)
                                }
                            },
                            { err ->
                                showDialog(activity!!, "Error", err.toString())
                            }
                    ))
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

    private fun isValidEmail(target: CharSequence): Boolean {
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches()
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
