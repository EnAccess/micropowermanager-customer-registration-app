package com.inensus.android.customer_list.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.inensus.android.R
import com.inensus.android.base.view.BaseFragment
import com.inensus.android.customer_list.model.Customer
import com.inensus.android.customer_list.viewmodel.CustomerListViewModel
import com.inensus.android.databinding.FragmentCustomerListBinding
import com.inensus.android.extensions.gone
import com.inensus.android.extensions.show
import org.koin.androidx.viewmodel.ext.android.viewModel

class CustomerListFragment : BaseFragment() {
    private val viewModel: CustomerListViewModel by viewModel()

    @Suppress("ktlint:standard:backing-property-naming")
    private var _binding: FragmentCustomerListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentCustomerListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
        observeUiState()
    }

    private fun setupView() {
        setupRecyclerView()
        setupSwipeRefreshLayout()
    }

    private fun observeUiState() {
        viewModel.uiState.observe(
            viewLifecycleOwner,
            Observer {
                when (it) {
                    is CustomerListUiState.Success -> {
                        handleSuccessState(it.customers)
                    }

                    CustomerListUiState.Empty -> {
                        handleEmptyState()
                    }

                    CustomerListUiState.AddCustomerSuccess -> {
                        getCustomers()
                    }

                    is CustomerListUiState.AddCustomerError -> {
                        showCustomerAlertDialog(it.throwable, it.customer) {
                            viewModel.deleteCustomerFromLocalStorage(it.customer) {
                                viewModel.getCustomers()
                            }
                        }
                    }
                }
            },
        )
    }

    private fun handleSuccessState(customers: List<Customer>) {
        binding.rvCustomerList.adapter.apply {
            (this as CustomerListAdapter).customers = customers
            notifyDataSetChanged()
        }

        stopSwipeRefresh()
        binding.llEmpty.gone()
    }

    private fun handleEmptyState() {
        stopSwipeRefresh()
        binding.llEmpty.show()
    }

    private fun setupRecyclerView() {
        binding.rvCustomerList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter =
                CustomerListAdapter().apply {
                    onItemClick = {
                        showWarningDialog(getString(R.string.delete_customer)) {
                            viewModel.deleteCustomerFromLocalStorage(it) {
                                viewModel.getCustomers()
                            }
                        }
                    }
                }
        }
    }

    private fun setupSwipeRefreshLayout() {
        binding.swipeRefreshLayout.setColorSchemeColors(
            ContextCompat.getColor(
                activity as Context,
                R.color.colorPrimaryDark,
            ),
        )
        binding.swipeRefreshLayout.setOnRefreshListener { viewModel.getCustomers() }
    }

    fun getCustomers() {
        viewModel.getCustomers()
    }

    fun syncLocalStorage() {
        viewModel.syncLocalStorage()
    }

    private fun stopSwipeRefresh() {
        binding.swipeRefreshLayout.isRefreshing = false
    }

    override fun provideViewModel() = viewModel

    companion object {
        fun newInstance() = CustomerListFragment().apply {}
    }
}
