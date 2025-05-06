package com.inensus.android.customer_list.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.inensus.android.R
import com.inensus.android.customer_list.model.Customer
import com.inensus.android.extensions.createInitialsDrawable
import com.inensus.android.extensions.gone
import com.inensus.android.extensions.show
import kotlinx.android.synthetic.main.list_item_customer.view.customerImage
import kotlinx.android.synthetic.main.list_item_customer.view.customerNameText
import kotlinx.android.synthetic.main.list_item_customer.view.customerPhoneNumberText
import kotlinx.android.synthetic.main.list_item_customer.view.localeImage
import kotlinx.android.synthetic.main.list_item_customer.view.localeText

class CustomerListAdapter : RecyclerView.Adapter<CustomerListAdapter.ViewHolder>() {

    lateinit var onItemClick: ((customer: Customer) -> Unit)
    var customers: List<Customer> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.list_item_customer, parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(customers[position])
    }

    override fun getItemCount() = customers.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(customer: Customer) {
            with(itemView) {
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

                setOnClickListener {
                    if (customer.isLocal) {
                        onItemClick.invoke(customer)
                    }
                }
            }
        }
    }
}
