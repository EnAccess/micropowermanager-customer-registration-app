package com.inensus.android.add_customer.viewmodel

import com.inensus.android.add_customer.service.AddCustomerRepository
import com.inensus.android.base.viewmodel.BaseViewModel
import com.inensus.android.customer_list.model.Customer
import com.inensus.android.util.SharedPreferenceWrapper

class AddCustomerViewModel(private val repository: AddCustomerRepository, private val preferences: SharedPreferenceWrapper) : BaseViewModel() {

    fun addCustomerToDb(customer: Customer) {
        repository.addCustomerToDb(customer)
    }

    fun getManufacturers() = repository.getManufacturers()

    fun getMeterTypes() = repository.getMeterTypes()

    fun getTariffs() = repository.getTariffs()

    fun getCities() = repository.getCities()

    fun getConnectionGroups() = repository.getConnectionGroups()

    fun getConnectionTypes() = repository.getConnectionTypes()

    fun getSubConnectionTypes() = repository.getSubConnectionTypes()

    fun findManufacturerByValue(value: String) = preferences.getManufacturers().first { it.name == value }

    fun findMeterTypeByValue(value: String) = preferences.getMeterTypes().first { it.toString() == value }

    fun findTariffByName(value: String) = preferences.getTariffs().first { it.name == value }

    fun findCityByName(value: String) = preferences.getCities().first { it.name == value }

    fun findConnectionGroupByValue(value: String) = preferences.getConnectionGroups().first { it.name == value }

    fun findConnectionTypeByValue(value: String) = preferences.getConnectionTypes().first { it.name == value }

    fun findSubConnectionTypeByValue(value: String) = preferences.getSubConnectionTypes().first { it.name == value }
}