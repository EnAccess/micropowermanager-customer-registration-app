package com.inensus.android.add_customer.service

import android.os.AsyncTask
import com.inensus.android.customer_list.model.Customer
import com.inensus.android.database.CustomerDao
import com.inensus.android.util.SharedPreferenceWrapper

class AddCustomerRepository(
    private val customerDao: CustomerDao,
    private val service: AddCustomerService,
    private val preferences: SharedPreferenceWrapper,
) {
    fun addCustomerToDb(customer: Customer) {
        AddCustomerAsyncTask(customerDao).execute(customer)
    }

    fun getManufacturers() = service.getManufacturers(preferences.baseUrl + MANUFACTURERS_ENDPOINT).map { it.data }

    fun getMeterTypes() = service.getMeterTypes(preferences.baseUrl + METER_TYPES_ENDPOINT).map { it.data }

    fun getTariffs() = service.getTariffs(preferences.baseUrl + TARIFFS_ENDPOINT).map { it.data }

    fun getCities() = service.getCities(preferences.baseUrl + CITIES_ENDPOINT).map { it.data }

    fun getConnectionGroups() =
        service
            .getConnectionGroups(preferences.baseUrl + CONNECTION_GROUPS_ENDPOINT)
            .map { it.data }

    fun getConnectionTypes() = service.getConnectionTypes(preferences.baseUrl + CONNECTION_TYPES_ENDPOINT).map { it.data }

    fun getSubConnectionTypes() =
        service
            .getSubConnectionTypes(preferences.baseUrl + SUB_CONNECTION_TYPES_ENDPOINT)
            .map { it.data }

    private class AddCustomerAsyncTask internal constructor(
        private val mAsyncTaskDao: CustomerDao,
    ) : AsyncTask<Customer, Void, Void>() {
        override fun doInBackground(vararg params: Customer): Void? {
            mAsyncTaskDao.insertCustomer(params[0])

            return null
        }
    }

    companion object {
        private const val MANUFACTURERS_ENDPOINT = "customer-registration-app/manufacturers"
        private const val METER_TYPES_ENDPOINT = "customer-registration-app/meter-types"
        private const val TARIFFS_ENDPOINT = "customer-registration-app/tariffs"
        private const val CITIES_ENDPOINT = "customer-registration-app/cities"
        private const val CONNECTION_GROUPS_ENDPOINT = "customer-registration-app/connection-groups"
        private const val CONNECTION_TYPES_ENDPOINT = "customer-registration-app/connection-types"
        private const val SUB_CONNECTION_TYPES_ENDPOINT = "customer-registration-app/sub-connection-types"
    }
}
