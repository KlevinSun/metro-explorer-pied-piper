package com.felixlin.dcmetroexplorer

import android.app.IntentService
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.os.ResultReceiver
import android.text.TextUtils
import android.util.Log
import com.felixlin.dcmetroexplorer.Constants.FAILURE_RESULT
import com.felixlin.dcmetroexplorer.Constants.LOCATION_DATA_EXTRA
import com.felixlin.dcmetroexplorer.Constants.RECEIVER
import com.felixlin.dcmetroexplorer.Constants.RESULT_DATA_KEY
import com.felixlin.dcmetroexplorer.Constants.SUCCESS_RESULT

import java.io.IOException
import java.util.ArrayList
import java.util.Locale


class FetchAddressIntentService : IntentService(TAG) {

    private var mReceiver: ResultReceiver? = null

    override fun onHandleIntent(intent: Intent?) {
        var errorMessage = ""

        mReceiver = intent!!.getParcelableExtra(RECEIVER)

        // Check if receiver was properly registered.
        if (mReceiver == null) {
            Log.wtf(TAG, "No receiver received. There is nowhere to send the results.")
            return
        }

        // Get the location passed to this service through an extra.
        val location = intent.getParcelableExtra<Location>(LOCATION_DATA_EXTRA)

        // Make sure that the location data was really sent over through an extra. If it wasn't,
        // send an error error message and return.
        if (location == null) {
            errorMessage = getString(R.string.no_location_data_provided)
            Log.wtf(TAG, errorMessage)
            deliverResultToReceiver(FAILURE_RESULT, errorMessage)
            return
        }

        // The Geocoder used in this sample. The Geocoder's responses are localized for the given
        // Locale, which represents a specific geographical or linguistic region. Locales are used
        // to alter the presentation of information such as numbers or dates to suit the conventions
        // in the region they describe.
        val geocoder = Geocoder(this, Locale.getDefault())

        // Address found using the Geocoder.
        var addresses: List<Address>? = null

        try {
            // Using getFromLocation() returns an array of Addresses for the area immediately
            // surrounding the given latitude and longitude. The results are a best guess and are
            // not guaranteed to be accurate.
            addresses = geocoder.getFromLocation(
                    location.latitude,
                    location.longitude,
                    // In this sample, we get just a single address.
                    1)
        } catch (ioException: IOException) {
            // Catch network or other I/O problems.
            errorMessage = getString(R.string.service_not_available)
            Log.e(TAG, errorMessage, ioException)
        } catch (illegalArgumentException: IllegalArgumentException) {
            // Catch invalid latitude or longitude values.
            errorMessage = getString(R.string.invalid_lat_long_used)
            Log.e(TAG, errorMessage + ". " +
                    "Latitude = " + location.latitude +
                    ", Longitude = " + location.longitude, illegalArgumentException)
        }

        // Handle case where no address was found.
        if (addresses == null || addresses.size == 0) {
            if (errorMessage.isEmpty()) {
                errorMessage = getString(R.string.no_address_found)
                Log.e(TAG, errorMessage)
            }
            deliverResultToReceiver(FAILURE_RESULT, errorMessage)
        } else {
            val address = addresses[0]
            val addressFragments = ArrayList<String>()

            // Fetch the address lines using {@code getAddressLine},
            // join them, and send them to the thread. The {@link android.location.address}
            // class provides other options for fetching address details that you may prefer
            // to use. Here are some examples:
            // getLocality() ("Mountain View", for example)
            // getAdminArea() ("CA", for example)
            // getPostalCode() ("94043", for example)
            // getCountryCode() ("US", for example)
            // getCountryName() ("United States", for example)
            for (i in 0..address.maxAddressLineIndex) {
                addressFragments.add(address.postalCode)
            }
            Log.i(TAG, getString(R.string.address_found))
            deliverResultToReceiver(SUCCESS_RESULT,
                    TextUtils.join(System.getProperty("line.separator"), addressFragments))
        }
    }

    /**
     * Sends a resultCode and message to the receiver.
     */
    private fun deliverResultToReceiver(resultCode: Int, message: String) {
        val bundle = Bundle()
        bundle.putString(RESULT_DATA_KEY, message)
        mReceiver!!.send(resultCode, bundle)
    }

    companion object {

        private val TAG = "FetchAddressIS"
    }
}

