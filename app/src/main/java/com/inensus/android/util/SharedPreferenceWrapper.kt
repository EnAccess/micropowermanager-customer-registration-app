package com.inensus.android.util

import android.content.Context
import androidx.preference.PreferenceManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.inensus.android.add_customer.model.City
import com.inensus.android.add_customer.model.ConnectionGroup
import com.inensus.android.add_customer.model.ConnectionType
import com.inensus.android.add_customer.model.Manufacturer
import com.inensus.android.add_customer.model.MeterType
import com.inensus.android.add_customer.model.SubConnectionType
import com.inensus.android.add_customer.model.Tariff

/**
 * Keeps a reference to the SharedPreference
 * Acts as a Singleton class
 */
class SharedPreferenceWrapper(
    context: Context,
) {
    private val preferences = PreferenceManager.getDefaultSharedPreferences(context)

    var accessToken: String
        get() = preferences.getString(KEY_ACCESS_TOKEN, "") ?: ""
        set(value) {
            preferences.edit().putString(KEY_ACCESS_TOKEN, value).apply()
        }

    var baseUrl: String?
        get() = preferences.getString(KEY_BASE_URL, DEFAULT_BASE_URL)
        set(url) {
            preferences.edit().putString(KEY_BASE_URL, url + BASE_URL_SUFFIX).apply()
        }

    var manufacturers: String
        get() = preferences.getString(KEY_MANUFACTURERS, "")!!
        set(manufacturers) {
            preferences.edit().putString(KEY_MANUFACTURERS, manufacturers).apply()
        }

    var meterTypes: String
        get() = preferences.getString(KEY_METER_TYPES, "")!!
        set(manufacturers) {
            preferences.edit().putString(KEY_METER_TYPES, manufacturers).apply()
        }

    var tariffs: String
        get() = preferences.getString(KEY_TARIFFS, "")!!
        set(manufacturers) {
            preferences.edit().putString(KEY_TARIFFS, manufacturers).apply()
        }

    var cities: String
        get() = preferences.getString(KEY_CITIES, "")!!
        set(cities) {
            preferences.edit().putString(KEY_CITIES, cities).apply()
        }

    var connectionGroups: String
        get() = preferences.getString(KEY_CONNECTION_GROUPS, "")!!
        set(cities) {
            preferences.edit().putString(KEY_CONNECTION_GROUPS, cities).apply()
        }

    var connectionTypes: String
        get() = preferences.getString(KEY_CONNECTION_TYPES, "")!!
        set(cities) {
            preferences.edit().putString(KEY_CONNECTION_TYPES, cities).apply()
        }

    var subConnectionTypes: String
        get() = preferences.getString(KEY_SUB_CONNECTION_TYPES, "")!!
        set(cities) {
            preferences.edit().putString(KEY_SUB_CONNECTION_TYPES, cities).apply()
        }

    fun getManufacturers(): MutableList<Manufacturer> {
        val type = object : TypeToken<List<Manufacturer>>() {}.type

        if (manufacturers.isEmpty()) {
            return ArrayList<Manufacturer>().toMutableList()
        }

        return Gson().fromJson<List<Manufacturer>>(manufacturers, type).toMutableList()
    }

    fun getMeterTypes(): MutableList<MeterType> {
        val type = object : TypeToken<List<MeterType>>() {}.type

        if (meterTypes.isEmpty()) {
            return ArrayList<MeterType>().toMutableList()
        }

        return Gson().fromJson<List<MeterType>>(meterTypes, type).toMutableList()
    }

    fun getTariffs(): MutableList<Tariff> {
        val type = object : TypeToken<List<Tariff>>() {}.type

        if (tariffs.isEmpty()) {
            return ArrayList<Tariff>().toMutableList()
        }

        return Gson().fromJson<List<Tariff>>(tariffs, type).toMutableList()
    }

    fun getCities(): MutableList<City> {
        val type = object : TypeToken<List<City>>() {}.type

        if (cities.isEmpty()) {
            return ArrayList<City>().toMutableList()
        }

        return Gson().fromJson<List<City>>(cities, type).toMutableList()
    }

    fun getConnectionGroups(): MutableList<ConnectionGroup> {
        val type = object : TypeToken<List<ConnectionGroup>>() {}.type

        if (connectionGroups.isEmpty()) {
            return ArrayList<ConnectionGroup>().toMutableList()
        }

        return Gson().fromJson<List<ConnectionGroup>>(connectionGroups, type).toMutableList()
    }

    fun getConnectionTypes(): MutableList<ConnectionType> {
        val type = object : TypeToken<List<ConnectionType>>() {}.type

        if (connectionTypes.isEmpty()) {
            return ArrayList<ConnectionType>().toMutableList()
        }

        return Gson().fromJson<List<ConnectionType>>(connectionTypes, type).toMutableList()
    }

    fun getSubConnectionTypes(): MutableList<SubConnectionType> {
        val type = object : TypeToken<List<SubConnectionType>>() {}.type

        if (subConnectionTypes.isEmpty()) {
            return ArrayList<SubConnectionType>().toMutableList()
        }

        return Gson().fromJson<List<SubConnectionType>>(subConnectionTypes, type).toMutableList()
    }

    companion object {
        private const val DEFAULT_BASE_URL = ""
        private const val BASE_URL_SUFFIX = "/api/"

        private const val KEY_ACCESS_TOKEN = "accessToken"
        private const val KEY_BASE_URL = "baseUrl"
        private const val KEY_NETWORK_STATE = "networkState"
        private const val KEY_MANUFACTURERS = "manufacturers"
        private const val KEY_METER_TYPES = "meterTypes"
        private const val KEY_TARIFFS = "tariffs"
        private const val KEY_CITIES = "cities"
        private const val KEY_CONNECTION_GROUPS = "connectionGroups"
        private const val KEY_CONNECTION_TYPES = "connectionTypes"
        private const val KEY_SUB_CONNECTION_TYPES = "subConnectionTypes"
    }
}
