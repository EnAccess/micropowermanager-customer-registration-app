package com.inensus.android.add_customer.view

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.gson.Gson
import com.inensus.android.add_customer.model.*
import com.inensus.android.add_customer.viewmodel.AddCustomerViewModel
import com.inensus.android.base.view.BaseFragment
import com.inensus.android.customer_list.model.Customer
import com.inensus.android.databinding.FragmentAddCustomerBinding
import com.inensus.android.util.ConnectionChecker
import com.inensus.android.util.SharedPreferenceWrapper
import io.reactivex.android.schedulers.AndroidSchedulers
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddCustomerFragment : BaseFragment(), com.google.android.gms.location.LocationListener {

    private val viewModel: AddCustomerViewModel by viewModel()
    private var _binding: FragmentAddCustomerBinding? = null
    private val binding get() = _binding!!

    private val preferences: SharedPreferenceWrapper by inject()
    private val connectionChecker: ConnectionChecker by inject()

    private var geoPoints = ""

    private var mGoogleApiClient: GoogleApiClient? = null
    private var mLocation: Location? = null
    private var mLocationRequest: LocationRequest? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddCustomerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (isAdded && connectionChecker.isOnline) {
            getManufacturers()
            getMeterTypes()
            getTariffs()
            getCities()
            getConnectionGroups()
            getConnectionTypes()
            getSubConnectionTypes()
        } else {
            invalidateManufacturerSpinner()
            invalidateMeterTypeSpinner()
            invalidateTariffSpinner()
            invalidateCitySpinner()
            invalidateConnectionGroupSpinner()
            invalidateConnectionTypeSpinner()
            invalidateSubConnectionTypeSpinner()
        }

        with(binding.serialNumber.getMainTextView()) {
            inputType = InputType.TYPE_CLASS_NUMBER
        }

        binding.connectionTypeDropdown.onValueChanged = {
            invalidateSubConnectionTypeSpinner()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        buildGoogleApiClient()
    }

    @SuppressLint("CheckResult")
    fun addCustomer() {
        if (!checkGPSEnabled()) {
            return
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                getLocation()
            } else {
                checkLocationPermission()
            }
        } else {
            getLocation()
        }

        if (isFormValid()) {
            viewModel.addCustomerToDb(
                Customer(
                    name = binding.name.getText(),
                    surname = binding.surname.getText(),
                    phone = binding.phoneNumber.getText(),
                    serialNumber = binding.serialNumber.getText(),
                    manufacturer = viewModel.findManufacturerByValue(binding.manufacturerDropdown.value).id,
                    meterType = viewModel.findMeterTypeByValue(binding.meterTypeDropdown.value).id,
                    tariff = viewModel.findTariffByName(binding.tariffDropdown.value).id,
                    city = viewModel.findCityByName(binding.cityDropdown.value).id,
                    connectionGroup = viewModel.findConnectionGroupByValue(binding.connectionGroupDropdown.value).id,
                    connectionType = viewModel.findConnectionTypeByValue(binding.connectionTypeDropdown.value).id,
                    subConnectionType = viewModel.findSubConnectionTypeByValue(
                        binding.subConnectionTypeDropdown.value
                    ).id,
                    geoPoints = geoPoints,
                    isLocal = true
                )
            )

            resetForm()

            Toast.makeText(activity, "Data has been inserted into Local Storage", Toast.LENGTH_LONG)
                .show()
        }
    }

    @SuppressLint("CheckResult")
    fun getManufacturers() {
        viewModel.getManufacturers()
            .subscribeOn(AndroidSchedulers.mainThread())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { showDialog() }
            .subscribe(
                {
                    hideDialog()
                    if (it.isNotEmpty()) {
                        preferences.manufacturers = Gson().toJson(it)
                        invalidateManufacturerSpinner(it)
                    } else {
                        invalidateManufacturerSpinner()
                    }
                },
                {
                    hideDialog()
                    invalidateManufacturerSpinner()
                }
            )
    }

    @SuppressLint("CheckResult")
    fun getMeterTypes() {
        viewModel.getMeterTypes()
            .subscribeOn(AndroidSchedulers.mainThread())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { showDialog() }
            .subscribe(
                {
                    hideDialog()
                    if (it.isNotEmpty()) {
                        preferences.meterTypes = Gson().toJson(it)
                        invalidateMeterTypeSpinner(it)
                    } else {
                        invalidateMeterTypeSpinner()
                    }
                },
                {
                    hideDialog()
                    invalidateMeterTypeSpinner()
                }
            )
    }

    @SuppressLint("CheckResult")
    fun getTariffs() {
        viewModel.getTariffs()
            .subscribeOn(AndroidSchedulers.mainThread())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { showDialog() }
            .subscribe(
                {
                    hideDialog()
                    if (it.isNotEmpty()) {
                        preferences.tariffs = Gson().toJson(it)
                        invalidateTariffSpinner(it)
                    } else {
                        invalidateTariffSpinner()
                    }
                },
                {
                    hideDialog()
                    invalidateTariffSpinner()
                }
            )
    }

    @SuppressLint("CheckResult")
    fun getCities() {
        viewModel.getCities()
            .subscribeOn(AndroidSchedulers.mainThread())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { showDialog() }
            .subscribe(
                {
                    hideDialog()
                    if (it.isNotEmpty()) {
                        preferences.cities = Gson().toJson(it)
                        invalidateCitySpinner(it)
                    } else {
                        invalidateCitySpinner()
                    }
                },
                {
                    hideDialog()
                    invalidateCitySpinner()
                }
            )
    }

    @SuppressLint("CheckResult")
    fun getConnectionGroups() {
        viewModel.getConnectionGroups()
            .subscribeOn(AndroidSchedulers.mainThread())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { showDialog() }
            .subscribe(
                {
                    hideDialog()
                    if (it.isNotEmpty()) {
                        preferences.connectionGroups = Gson().toJson(it)
                        invalidateConnectionGroupSpinner(it)
                    } else {
                        invalidateConnectionGroupSpinner()
                    }
                },
                {
                    hideDialog()
                    invalidateConnectionGroupSpinner()
                }
            )
    }

    @SuppressLint("CheckResult")
    fun getConnectionTypes() {
        viewModel.getConnectionTypes()
            .subscribeOn(AndroidSchedulers.mainThread())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { showDialog() }
            .subscribe(
                {
                    hideDialog()
                    if (it.isNotEmpty()) {
                        preferences.connectionTypes = Gson().toJson(it)
                        invalidateConnectionTypeSpinner(it)
                    } else {
                        invalidateConnectionTypeSpinner()
                    }
                },
                {
                    hideDialog()
                    invalidateConnectionTypeSpinner()
                }
            )
    }

    @SuppressLint("CheckResult")
    fun getSubConnectionTypes() {
        viewModel.getSubConnectionTypes()
            .subscribeOn(AndroidSchedulers.mainThread())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { showDialog() }
            .subscribe(
                {
                    hideDialog()
                    if (it.isNotEmpty()) {
                        preferences.subConnectionTypes = Gson().toJson(it)
                        invalidateSubConnectionTypeSpinner(it)
                    } else {
                        invalidateSubConnectionTypeSpinner()
                    }
                },
                {
                    hideDialog()
                    invalidateSubConnectionTypeSpinner()
                }
            )
    }

    private fun invalidateManufacturerSpinner(manufacturers: List<Manufacturer> = listOf()) {
        if (isAdded) {
            val mutableManufacturers: MutableList<Manufacturer> = if (manufacturers.isEmpty()) {
                preferences.getManufacturers()
            } else {
                manufacturers.toMutableList()
            }

            binding.manufacturerDropdown.bindData(mutableManufacturers.map { it.name ?: "" })
        }
    }

    private fun invalidateMeterTypeSpinner(meterTypes: List<MeterType> = listOf()) {
        if (isAdded) {
            val mutableMeterTypes: MutableList<MeterType> = if (meterTypes.isEmpty()) {
                preferences.getMeterTypes()
            } else {
                meterTypes.toMutableList()
            }

            if (mutableMeterTypes.isNotEmpty()) {
                binding.meterTypeDropdown.bindData(mutableMeterTypes.map { it.toString() })
            }
        }
    }

    private fun invalidateTariffSpinner(tariffs: List<Tariff> = listOf()) {
        if (isAdded) {
            val mutableTariffs: MutableList<Tariff> = if (tariffs.isEmpty()) {
                preferences.getTariffs()
            } else {
                tariffs.toMutableList()
            }

            binding.tariffDropdown.bindData(mutableTariffs.map { it.name ?: "" })
        }
    }

    private fun invalidateCitySpinner(cities: List<City> = listOf()) {
        if (isAdded) {
            val mutableCities: MutableList<City> = if (cities.isEmpty()) {
                preferences.getCities()
            } else {
                cities.toMutableList()
            }

            binding.cityDropdown.bindData(mutableCities.map { it.name ?: "" })
        }
    }

    private fun invalidateConnectionGroupSpinner(connectionGroups: List<ConnectionGroup> = listOf()) {
        if (isAdded) {
            val mutableConnectionGroups: MutableList<ConnectionGroup> =
                if (connectionGroups.isEmpty()) {
                    preferences.getConnectionGroups()
                } else {
                    connectionGroups.toMutableList()
                }

            binding.connectionGroupDropdown.bindData(mutableConnectionGroups.map { it.name ?: "" })
        }
    }

    private fun invalidateConnectionTypeSpinner(connectionTypes: List<ConnectionType> = listOf()) {
        if (isAdded) {
            val mutableConnectionTypes: MutableList<ConnectionType> =
                if (connectionTypes.isEmpty()) {
                    preferences.getConnectionTypes()
                } else {
                    connectionTypes.toMutableList()
                }

            binding.connectionTypeDropdown.bindData(mutableConnectionTypes.map { it.name ?: "" })
        }
    }

    private fun invalidateSubConnectionTypeSpinner(subConnectionTypes: List<SubConnectionType> = listOf()) {
        if (isAdded) {
            val mutableSubConnectionTypes: MutableList<SubConnectionType> =
                if (subConnectionTypes.isEmpty()) {
                    preferences.getSubConnectionTypes()
                } else {
                    subConnectionTypes.toMutableList()
                }

            if (binding.connectionTypeDropdown.value.isNotEmpty()) {
                val filteredSubConnectionTypes = mutableSubConnectionTypes.filter {
                    it.connectionTypeId == viewModel.findConnectionTypeByValue(
                        binding.connectionTypeDropdown.value
                    ).id
                }.map {
                    it.name ?: ""
                }

                binding.subConnectionTypeDropdown.bindData(filteredSubConnectionTypes)
            }
        }
    }

    private fun showDialog() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideDialog() {
        binding.progressBar.visibility = View.GONE
    }

    private fun isFormValid(): Boolean {
        var isValid = true

        if (binding.name.getText().isEmpty()) {
            binding.name.setErrorState(true)
            isValid = false
        }

        if (binding.surname.getText().isEmpty()) {
            binding.surname.setErrorState(true)
            isValid = false
        }

        if (binding.phoneNumber.getText().isEmpty()) {
            binding.phoneNumber.setErrorState(true)
            isValid = false
        }

        if (binding.serialNumber.getText().isEmpty()) {
            binding.serialNumber.setErrorState(true)
            isValid = false
        }

        if (binding.manufacturerDropdown.value.isEmpty()) {
            binding.manufacturerDropdown.setErrorState(true)
            isValid = false
        }

        if (binding.meterTypeDropdown.value.isEmpty()) {
            binding.meterTypeDropdown.setErrorState(true)
            isValid = false
        }

        if (binding.tariffDropdown.value.isEmpty()) {
            binding.tariffDropdown.setErrorState(true)
            isValid = false
        }

        if (binding.cityDropdown.value.isEmpty()) {
            binding.cityDropdown.setErrorState(true)
            isValid = false
        }

        if (binding.connectionGroupDropdown.value.isEmpty()) {
            binding.connectionGroupDropdown.setErrorState(true)
            isValid = false
        }

        if (binding.connectionTypeDropdown.value.isEmpty()) {
            binding.connectionTypeDropdown.setErrorState(true)
            isValid = false
        }

        if (binding.subConnectionTypeDropdown.value.isEmpty()) {
            binding.subConnectionTypeDropdown.setErrorState(true)
            isValid = false
        }

        if (geoPoints.isEmpty()) {
            Toast.makeText(requireContext(), "Location not Detected", Toast.LENGTH_LONG).show()
            isValid = false
        }

        return isValid
    }

    private fun resetForm() {
        binding.name.setText("")
        binding.surname.setText("")
        binding.phoneNumber.setText("")
        binding.serialNumber.setText("")

        binding.manufacturerDropdown.value =
            preferences.getManufacturers().firstOrNull()?.name ?: ""
        binding.meterTypeDropdown.value =
            preferences.getMeterTypes().firstOrNull()?.toString() ?: ""
        binding.tariffDropdown.value = preferences.getTariffs().firstOrNull()?.name ?: ""
        binding.cityDropdown.value = preferences.getCities().firstOrNull()?.name ?: ""
        binding.connectionGroupDropdown.value =
            preferences.getConnectionGroups().firstOrNull()?.name ?: ""
        binding.connectionTypeDropdown.value =
            preferences.getConnectionTypes().firstOrNull()?.name ?: ""
        binding.subConnectionTypeDropdown.value =
            preferences.getSubConnectionTypes().firstOrNull()?.name
                ?: ""

        invalidateManufacturerSpinner()
        invalidateMeterTypeSpinner()
        invalidateTariffSpinner()
        invalidateCitySpinner()
        invalidateConnectionGroupSpinner()
        invalidateConnectionTypeSpinner()
        invalidateSubConnectionTypeSpinner()
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient)

        if (mLocation == null) {
            startLocationUpdates()
        }
        if (mLocation != null) {
            geoPoints = mLocation!!.latitude.toString() + "," + mLocation!!.longitude.toString()
        } else {
            Toast.makeText(requireContext(), "Location not Detected", Toast.LENGTH_SHORT).show()
        }
    }

    private fun startLocationUpdates() {
        mLocationRequest = LocationRequest.create()
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            .setInterval(UPDATE_INTERVAL)
            .setFastestInterval(FASTEST_INTERVAL)

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(
            mGoogleApiClient,
            mLocationRequest,
            this
        )
    }

    override fun onLocationChanged(p0: Location?) {
        geoPoints = p0?.latitude.toString() + "," + p0?.longitude.toString()
    }

    @Synchronized
    private fun buildGoogleApiClient() {
        mGoogleApiClient = GoogleApiClient.Builder(requireContext())
            .addApi(LocationServices.API)
            .build()

        mGoogleApiClient!!.connect()
    }

    private fun checkGPSEnabled(): Boolean {
        if (!isLocationEnabled())
            showAlert()
        return isLocationEnabled()
    }

    private fun showAlert() {
        val dialog = AlertDialog.Builder(requireContext())
        dialog.setTitle("Enable Location")
            .setMessage("Your Locations Settings is set to 'Off'.\nPlease Enable Location to " + "use this app")
            .setPositiveButton("Location Settings") { _, _ ->
                val myIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(myIntent)
            }
            .setNegativeButton("Cancel") { _, _ -> }
        dialog.show()
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager =
            requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    requireActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                AlertDialog.Builder(requireContext())
                    .setTitle("Location Permission Needed")
                    .setMessage("This app needs the Location permission, please accept to use location functionality")
                    .setPositiveButton("OK") { _, _ ->
                        ActivityCompat.requestPermissions(
                            requireActivity(),
                            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                            REQUEST_LOCATION_CODE
                        )
                    }
                    .create()
                    .show()

            } else ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_LOCATION_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_LOCATION_CODE -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(
                            requireContext(),
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        Toast.makeText(requireContext(), "permission granted", Toast.LENGTH_LONG)
                            .show()
                    }
                } else {
                    // permission denied, boo! Disable the functionality that depends on this permission.
                    Toast.makeText(requireContext(), "permission denied", Toast.LENGTH_LONG).show()
                }
                return
            }
        }
    }

    override fun onStart() {
        super.onStart()
        mGoogleApiClient?.connect()
    }

    override fun provideViewModel() = viewModel

    companion object {
        @JvmStatic
        fun newInstance() = AddCustomerFragment().apply {}

        private const val REQUEST_LOCATION_CODE = 101
        private const val UPDATE_INTERVAL = (2 * 1000).toLong()
        private const val FASTEST_INTERVAL: Long = 2000
    }
}
