package com.inensus.android.customer_list.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.inensus.android.R
import com.inensus.android.customer_list.model.Customer
import com.inensus.android.databinding.ListItemCustomerBinding
import com.inensus.android.extensions.createInitialsDrawable
import com.inensus.android.extensions.gone
import com.inensus.android.extensions.show

class CustomerListAdapter : RecyclerView.Adapter<CustomerListAdapter.ViewHolder>() {

    lateinit var onItemClick: ((customer: Customer) -> Unit)
    var customers: List<Customer> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ListItemCustomerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(customers[position])
    }

    override fun getItemCount() = customers.size

    inner class ViewHolder(private val binding: ListItemCustomerBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(customer: Customer) {
            with(binding) {
                val context = root.context
                customerImage.setImageDrawable(
                    createInitialsDrawable(
                        context.getString(
                            R.string.customer_name_surname,
                            customer.name,
                            customer.surname
                        ),
                        ContextCompat.getColor(context, R.color.white),
                        ContextCompat.getColor(context, R.color.colorPrimary)
                    )
                )
                customerNameText.text = context.getString(
                    R.string.customer_name_surname,
                    customer.name,
                    customer.surname
                )

                if (customer.isLocal) {
                    customerPhoneNumberText.text = StringBuilder().append(customer.phone)
                    localeText.show()
                    localeImage.show()
                } else {
                    customerPhoneNumberText.text =
                        context.getString(R.string.customer_id, customer.id.toString())
                    localeText.gone()
                    localeImage.gone()
                }

                root.setOnClickListener {
                    if (customer.isLocal) {
                        onItemClick.invoke(customer)
                    }
                }
            }
        }
    }
}
