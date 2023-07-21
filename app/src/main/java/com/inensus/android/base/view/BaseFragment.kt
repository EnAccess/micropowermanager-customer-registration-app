package com.inensus.android.base.view

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.inensus.android.BuildConfig
import com.inensus.android.R
import com.inensus.android.base.viewmodel.BaseViewModel
import com.inensus.android.customer_list.model.Customer
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.HttpException
import java.net.UnknownHostException

abstract class BaseFragment : Fragment() {
    abstract fun provideViewModel(): BaseViewModel
    private var progressDialog: AlertDialog? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeLoading()
        observeError()
    }

    private fun observeLoading() {
        provideViewModel().loading.observe(viewLifecycleOwner, Observer {
            if (it) {
                showLoading()
            } else {
                hideLoading()
            }
        })
    }

    private fun observeError() {
        provideViewModel().error.observe(viewLifecycleOwner, Observer {
            AlertDialog.Builder(requireContext())
                    .setTitle(getString(R.string.error))
                    .setMessage(it.error?.message ?: "An error occurred. Please try again later")
                    .setPositiveButton(getString(R.string.ok)) { _, _ -> }
                    .show()
        })
    }

    fun showWarningDialog(message: String?, callback: (() -> Unit)? = null): AlertDialog? =
            context?.let {
                AlertDialog.Builder(requireContext())
                        .setTitle(getString(R.string.error))
                        .setMessage(message)
                        .setPositiveButton(getString(R.string.yes)) { _, _ -> callback?.invoke() }
                        .setNegativeButton(getString(R.string.no)) { _, _ -> }
                        .show()
            }

    fun showCustomerAlertDialog(throwable: Throwable?, customer: Customer? = null, callback: (() -> Unit)? = null) {
        if (context != null) {
            var errorMessage = ""

            if (customer != null) {
                errorMessage = "Customer Information:\n- Name Surname: " + customer.name + " " + customer.surname + "\n- Serial Number: " + customer.serialNumber + "\n\n"
            }

            try {
                if (throwable is UnknownHostException) {
                    errorMessage = "Server Url is not valid. Please configure and restart the Application."
                } else {
                    val error = (throwable as HttpException).response()
                    val json = JSONObject(error?.errorBody()?.string())
                    val errorMessages = json.getJSONObject("errors")
                    val keys = errorMessages.keys()

                    errorMessage += "Error Details:\n"

                    while (keys.hasNext()) {
                        val key = keys.next() as String

                        if (errorMessages.get(key) is JSONArray) {
                            errorMessage = errorMessage + "- " + (errorMessages.get(key) as JSONArray).get(0) + "\n"
                        }
                    }
                }
            } catch (e: Exception) {
                errorMessage = if (BuildConfig.DEBUG && e.message != null) e.message!! else "Internal Server Error"
            }

            val alert = android.app.AlertDialog.Builder(requireContext())
                    .setTitle(getString(R.string.error_message_title))
                    .setMessage(errorMessage)
                    .setPositiveButton("OK") { _, _ ->
                        callback?.invoke()
                    }
                    .setCancelable(false)
                    .create()

            alert.show()
        }
    }

    private fun showLoading() {
        if (!requireActivity().isFinishing) {
            if (progressDialog == null) {
                initializeLoading(requireActivity())
            }
            progressDialog?.show()
        }
    }

    private fun hideLoading() {
        if (!requireActivity().isFinishing) {
            progressDialog?.cancel()
            progressDialog = null
        }
    }

    private fun initializeLoading(activity: Activity) {
        if (!activity.isFinishing) {
            val builder = AlertDialog.Builder(activity)
            builder.setCancelable(false)
            builder.setView(R.layout.loading_dialog)
            progressDialog = builder.create()
        }
    }
}