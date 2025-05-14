package com.inensus.android.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.inensus.android.customer_list.model.Customer
import io.reactivex.Single

@Dao
interface CustomerDao {
    @Insert
    fun insertCustomer(customer: Customer)

    @Update
    fun updateCustomer(customer: Customer)

    @Delete
    fun deleteCustomer(customer: Customer)

    @Query("DELETE from Customers")
    fun deleteAll()

    @Query("SELECT * from Customers ORDER BY name ASC")
    fun getCustomerList(): Single<List<Customer>>
}
