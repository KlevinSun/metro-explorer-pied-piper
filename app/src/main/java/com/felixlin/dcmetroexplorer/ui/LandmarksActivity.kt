package com.felixlin.dcmetroexplorer.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.*
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.ResultReceiver
import android.support.annotation.RequiresApi
import android.support.v4.app.ActivityCompat
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.widget.Toast
import com.felixlin.dcmetroexplorer.Constants
import com.felixlin.dcmetroexplorer.FetchAddressIntentService
import com.felixlin.dcmetroexplorer.adapter.LandmarksListAdapter
import com.felixlin.dcmetroexplorer.R
import com.felixlin.dcmetroexplorer.YelpService
import com.felixlin.dcmetroexplorer.model.Landmark
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnSuccessListener
import kotlinx.android.synthetic.main.activity_landmarks.*
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException
import java.util.*

class LandmarksActivity : AppCompatActivity() {

    companion object {

        private val TAG = LandmarksActivity::class.java.simpleName

        private val REQUEST_PERMISSIONS_REQUEST_CODE = 34

        private val ADDRESS_REQUESTED_KEY = "address-request-pending"
        private val LOCATION_ADDRESS_KEY = "location-address"
    }

    private var mFusedLocationClient: FusedLocationProviderClient? = null
    private var mLastLocation: Location? = null
    private var mAddressRequested: Boolean = false
    private var mAddressOutput: String? = null
    private var mResultReceiver: AddressResultReceiver? = null
    private var mPostalCode: String? = null

    lateinit private var mAdapter: LandmarksListAdapter

    var mLandmarks = ArrayList<Landmark>()

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landmarks)

        mResultReceiver = AddressResultReceiver(Handler())
        mAddressRequested = false
        mAddressOutput = ""
        updateValuesFromBundle(savedInstanceState)

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        fetchAddress()

        Toast.makeText(this, getCode(), Toast.LENGTH_LONG).show()

        getNearby("20850")
    }

    fun getNearby(location: String)
    {
        val yelpService = YelpService()

        yelpService.findLandmarks(location, object : Callback {

            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                mLandmarks = yelpService.processResults(response)

                this@LandmarksActivity.runOnUiThread(java.lang.Runnable {
                    mAdapter = LandmarksListAdapter(applicationContext, mLandmarks)
                    rv.setAdapter(mAdapter)
                    rv.layoutManager = LinearLayoutManager(this@LandmarksActivity)
                    rv.setHasFixedSize(true)
                })
            }
        })
    }

    private fun requestPermissions()
    {
        val shouldProvideRationale = ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.ACCESS_FINE_LOCATION)

        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.")

        } else {
            Log.i(TAG, "Requesting permission")
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the user denied the permission
            // previously and checked "Never ask again".
            ActivityCompat.requestPermissions(this@LandmarksActivity,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    REQUEST_PERMISSIONS_REQUEST_CODE)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray)
    {
//        when(requestCode)
//        {
//            PERMISSION_REQUEST_CODE ->
//            {
//                if(grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
//                {
//                    if(checkPlayService())
//                    {
//                        buildGoogleApiClient()
//                    }
//                }
//            }
//        }

        Log.i(TAG, "onRequestPermissionResult")
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.size <= 0) {
                // If user interaction was interrupted, the permission request is cancelled and you
                // receive empty arrays.
                Log.i(TAG, "User interaction was cancelled.")
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted.
                getAddress()
            } else {
                // Permission denied.

                // Notify the user via a SnackBar that they have rejected a core permission for the
                // app, which makes the Activity useless. In a real app, core permissions would
                // typically be best requested during a welcome-screen flow.

                // Additionally, it is important to remember that a permission might have been
                // rejected without asking the user for permission (device policy or "Never ask
                // again" prompts). Therefore, a user interface affordance is typically implemented
                // when permissions are denied. Otherwise, your app could appear unresponsive to
                // touches or interactions which have required permissions.

            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onStart() {
        super.onStart()

        if (!checkPermissions()) {
            requestPermissions()
        } else {
            getAddress()
        }
    }

    private fun setCode(s: String?)
    {
        this.mPostalCode = s
    }

    private fun getCode(): String?
    {
        return mPostalCode
    }

    private inner class AddressResultReceiver internal constructor(handler: Handler) : ResultReceiver(handler) {

        override fun onReceiveResult(resultCode: Int, resultData: Bundle) {

            // Display the address string or an error message sent from the intent service.
            mAddressOutput = resultData.getString(Constants.RESULT_DATA_KEY)
            setCode(s = mAddressOutput)

            // Show a toast message if an address was found.
            if (resultCode == Constants.SUCCESS_RESULT) {
                showToast(getString(R.string.address_found))
            }

            // Reset. Enable the Fetch Address button and stop showing the progress bar.
            mAddressRequested = false
        }
    }

    private fun showToast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    private fun checkPermissions(): Boolean {
        val permissionState = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
        return permissionState == PackageManager.PERMISSION_GRANTED
    }

    @SuppressLint("MissingPermission")
    private fun getAddress() {
        mFusedLocationClient!!.lastLocation
                .addOnSuccessListener(this, OnSuccessListener { location ->
                    if (location == null) {
                        Log.w(TAG, "onSuccess:null")
                        return@OnSuccessListener
                    }

                    mLastLocation = location

                    // Determine whether a Geocoder is available.
                    if (!Geocoder.isPresent()) {
                        return@OnSuccessListener
                    }

                    // If the user pressed the fetch address button before we had the location,
                    // this will be set to true indicating that we should kick off the intent
                    // service after fetching the location.
                    if (mAddressRequested) {
                        startIntentService()
                    }
                })
                .addOnFailureListener(this) { e -> Log.w(TAG, "getLastLocation:onFailure", e) }
    }

    private fun startIntentService() {
        // Create an intent for passing to the intent service responsible for fetching the address.
        val intent = Intent(this, FetchAddressIntentService::class.java)

        // Pass the result receiver as an extra to the service.
        intent.putExtra(Constants.RECEIVER, mResultReceiver)

        // Pass the location data as an extra to the service.
        intent.putExtra(Constants.LOCATION_DATA_EXTRA, mLastLocation)

        // Start the service. If the service isn't already running, it is instantiated and started
        // (creating a process for it if needed); if it is running then it remains running. The
        // service kills itself automatically once all intents are processed.
        startService(intent)
    }

    private fun updateValuesFromBundle(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            // Check savedInstanceState to see if the address was previously requested.
            if (savedInstanceState.keySet().contains(ADDRESS_REQUESTED_KEY)) {
                mAddressRequested = savedInstanceState.getBoolean(ADDRESS_REQUESTED_KEY)
            }
            // Check savedInstanceState to see if the location address string was previously found
            // and stored in the Bundle. If it was found, display the address string in the UI.
            if (savedInstanceState.keySet().contains(LOCATION_ADDRESS_KEY)) {
                mAddressOutput = savedInstanceState.getString(LOCATION_ADDRESS_KEY)
                setCode(mAddressOutput)
            }
        }
    }

    public override fun onSaveInstanceState(savedInstanceState: Bundle?) {
        // Save whether the address has been requested.
        savedInstanceState!!.putBoolean(ADDRESS_REQUESTED_KEY, mAddressRequested)

        // Save the address string.
        savedInstanceState.putString(LOCATION_ADDRESS_KEY, mAddressOutput)
        super.onSaveInstanceState(savedInstanceState)
    }

    fun fetchAddress() {
        if (mLastLocation != null) {
            startIntentService()
            return
        }

        // If we have not yet retrieved the user location, we process the user's request by setting
        // mAddressRequested to true. As far as the user is concerned, pressing the Fetch Address button
        // immediately kicks off the process of getting the address.
        mAddressRequested = true
    }

}

