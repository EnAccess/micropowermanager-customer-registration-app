package com.inensus.android.main

import android.app.AlertDialog
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.inensus.android.R
import com.inensus.android.add_customer.view.AddCustomerFragment
import com.inensus.android.base.broadcast.ConnectivityBroadcastReceiver
import com.inensus.android.base.broadcast.SessionExpireBroadcastReceiver
import com.inensus.android.base.broadcast.SessionExpireBroadcastReceiver.Companion.SESSION_EXPIRE_INTENT_ACTION
import com.inensus.android.customer_list.view.CustomerListFragment
import com.inensus.android.login.view.LoginActivity
import com.inensus.android.util.ConnectionChecker
import com.inensus.android.util.SharedPreferenceWrapper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.activity_main.bottomNavigation
import kotlinx.android.synthetic.main.activity_main.toolbar
import org.koin.android.ext.android.inject
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    private val preferences: SharedPreferenceWrapper by inject()
    private var mBackPressed: Long = 0
    private val sessionExpireReceiver: SessionExpireBroadcastReceiver by inject()
    private val connectivityReceiver: ConnectivityBroadcastReceiver by inject()
    private val connectionChecker: ConnectionChecker by inject()

    private lateinit var customerListFragment: CustomerListFragment
    private lateinit var addCustomerFragment: AddCustomerFragment
    private lateinit var snackBar: Snackbar

    private val disposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)

        registerSessionExpireReceiver()
        registerNetworkReceiver()
        setupView()
    }

    private fun setupView() {
        setupFragments()
        setupBottomNavigationView()
        setupConnectivitySnackBar()
    }

    private fun setupFragments() {
        customerListFragment = CustomerListFragment.newInstance()
        addCustomerFragment = AddCustomerFragment.newInstance()

        supportFragmentManager.beginTransaction()
            .add(R.id.container, customerListFragment, "customerListFragment").commit()
        supportFragmentManager.beginTransaction()
            .add(R.id.container, addCustomerFragment, "addCustomerFragment")
            .hide(addCustomerFragment).commit()
    }

    private fun setupBottomNavigationView() {
        bottomNavigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.customerList -> {
                    supportFragmentManager.beginTransaction().hide(addCustomerFragment)
                        .show(customerListFragment).commitNow()
                    customerListFragment.getCustomers()
                }

                R.id.addCustomer -> {
                    supportFragmentManager.beginTransaction().hide(customerListFragment)
                        .show(addCustomerFragment).commitNow()
                }
            }

            invalidateOptionsMenu()

            true
        }
    }

    private fun setupConnectivitySnackBar() {
        snackBar = Snackbar.make(
            findViewById(android.R.id.content),
            getString(R.string.error_network_snackbar),
            Snackbar.LENGTH_LONG
        )
    }

    private fun registerSessionExpireReceiver() {
        registerReceiver(sessionExpireReceiver, IntentFilter(SESSION_EXPIRE_INTENT_ACTION))

        sessionExpireReceiver.event.observe(this, Observer {
            logout()
        })
    }

    private fun registerNetworkReceiver() {
        registerReceiver(connectivityReceiver, IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"))

        connectivityReceiver.observeNetworkStatusRelay()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::observeNetworkStatus, Timber::e)
            .addTo(disposable)
    }

    private fun observeNetworkStatus(status: ConnectionChecker.NetworkStatus) {

        when (status) {
            ConnectionChecker.NetworkStatus.Online -> handleNetworkStatus(true)
            ConnectionChecker.NetworkStatus.Offline -> handleNetworkStatus(false)
        }
    }

    private fun handleNetworkStatus(hasConnection: Boolean) {
        if (hasConnection) {
            if (snackBar.isShownOrQueued) {
                snackBar.dismiss()
            }
        } else {
            if (!snackBar.isShownOrQueued) {
                snackBar.show()
            }
        }
    }

    private fun logout() {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.warning))
            .setCancelable(false)
            .setMessage(getString(R.string.error_session_expired))
            .setPositiveButton(getString(R.string.ok)) { _, _ ->
                preferences.accessToken = ""
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
            .show()
    }

    override fun onBackPressed() {
        if (mBackPressed + 2000 > System.currentTimeMillis()) {
            super.onBackPressed()
            finish()
            return
        } else {
            Toast.makeText(baseContext, getString(R.string.exit), Toast.LENGTH_SHORT).show()
        }

        mBackPressed = System.currentTimeMillis()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)

        menu.findItem(R.id.actionSync).isVisible = customerListFragment.isVisible
        menu.findItem(R.id.actionCheck).isVisible = addCustomerFragment.isVisible

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) =
        when (item.itemId) {
            R.id.actionSync -> {
                handleNetworkStatus(connectionChecker.isOnline)
                customerListFragment.syncLocalStorage()
                true
            }

            R.id.actionCheck -> {
                addCustomerFragment.addCustomer()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }

    override fun onDestroy() {
        disposable.clear()
        unregisterReceiver(sessionExpireReceiver)
        unregisterReceiver(connectivityReceiver)

        super.onDestroy()
    }
}
