package com.inensus.android.customer_list.service

import android.os.AsyncTask
import com.inensus.android.customer_list.model.Customer
import com.inensus.android.database.CustomerDao
import com.inensus.android.util.ConnectionChecker
import com.inensus.android.util.SharedPreferenceWrapper
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.BiFunction

class CustomerListRepository(
    private val customerDao: CustomerDao,
    private val service: CustomerListService,
    private val preferences: SharedPreferenceWrapper,
    private val connectionChecker: ConnectionChecker
) {

    var localStorageCustomers: List<Customer> = emptyList()

    fun addCustomer(customer: Customer): Observable<Customer> =
        service.addCustomer(preferences.baseUrl + ADD_CUSTOMER_ENDPOINT, customer).map { it.data }

    fun getCustomers(): Single<List<Customer>> {
        return if (connectionChecker.isOnline) {
            Single.zip(
                getCustomersFromLocalStorage(),
                getCustomersFromApi(),
                BiFunction<List<Customer>, List<Customer>, List<Customer>> { dbResult, apiResult ->
                    combineLists(dbResult, apiResult)
                }
            )
        } else {
            getCustomersFromLocalStorage()
        }
    }

    private fun combineLists(dbList: List<Customer>, apiList: List<Customer>): List<Customer> {
        val combinedList = ArrayList<Customer>(dbList)
        combinedList.addAll(apiList)

        return combinedList
    }

    private fun getCustomersFromApi() =
        service.getCustomerList(preferences.baseUrl + CUSTOMERS_ENDPOINT).map { it.data }

    private fun getCustomersFromLocalStorage() =
        customerDao.getCustomerList().doOnSuccess { localStorageCustomers = it }

    fun deleteCustomerFromLocalStorage(customer: Customer, callback: (() -> Unit)? = null) {
        DeleteCustomerAsyncTask(customerDao, callback).execute(customer)
    }

    private class DeleteCustomerAsyncTask internal constructor(
        private val mAsyncTaskDao: CustomerDao,
        private val callback: (() -> Unit)? = null
    ) : AsyncTask<Customer, Void, Void>() {

        override fun doInBackground(vararg params: Customer): Void? {
            mAsyncTaskDao.deleteCustomer(params[0])

            return null
        }

        override fun onPostExecute(result: Void?) {
            super.onPostExecute(result)

            callback?.invoke()
        }
    }

    companion object {
        private const val ADD_CUSTOMER_ENDPOINT = "androidApp"
        private const val CUSTOMERS_ENDPOINT = "people/all"
    }
}
