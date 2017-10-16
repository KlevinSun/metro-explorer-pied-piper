package com.felixlin.dcmetroexplorer.ui

import android.Manifest
import android.content.pm.PackageManager
import android.location.*
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.app.ActivityCompat
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.widget.Toast
import com.felixlin.dcmetroexplorer.adapter.LandmarksListAdapter
import com.felixlin.dcmetroexplorer.R
import com.felixlin.dcmetroexplorer.YelpService
import com.felixlin.dcmetroexplorer.model.Landmark
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GooglePlayServicesUtil
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.activity_landmarks.*
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException
import java.util.*

class LandmarksActivity : AppCompatActivity(), GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    val PERMISSION_REQUEST_CODE = 1001
    val PLAY_SERVICE_RESOLUTION_REQUEST = 1000
    var mGoogleApiClient: GoogleApiClient? = null
    var mLocationRequest:LocationRequest?=null

    lateinit private var mAdapter: LandmarksListAdapter

    var mLandmarks = ArrayList<Landmark>()

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landmarks)

        requestPermission()
        if(checkPlayService())
        {
            buildGoogleApiClient()
        }

        getNearby("20878")
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


    @RequiresApi(Build.VERSION_CODES.M)
    private fun requestPermission()
    {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), PERMISSION_REQUEST_CODE)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray)
    {
        when(requestCode)
        {
            PERMISSION_REQUEST_CODE ->
            {
                if(grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    if(checkPlayService())
                    {
                        buildGoogleApiClient()
                    }
                }
            }
        }
    }

    private fun buildGoogleApiClient()
    {
        mGoogleApiClient = GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build()
    }

    private fun checkPlayService(): Boolean
    {
        var resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this)
        if(resultCode != ConnectionResult.SUCCESS)
        {
            if(GooglePlayServicesUtil.isUserRecoverableError(resultCode))
            {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this, PLAY_SERVICE_RESOLUTION_REQUEST).show()
            }
            else
            {
                Toast.makeText(applicationContext, "This device is not supported", Toast.LENGTH_SHORT).show()
                finish()
            }
            return false
        }
        return true
    }

    override fun onConnected(p0: Bundle?)
    {
        createLocationRequest()
    }

    private fun createLocationRequest()
    {
        mLocationRequest = LocationRequest()
        mLocationRequest!!.interval = 1000
        mLocationRequest!!.fastestInterval = 5000
        mLocationRequest!!.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            return
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this)
    }

    override fun onConnectionFailed(p0: ConnectionResult)
    {
        Log.i("ERROR", "Connection failed " + p0.errorCode)
    }

    override fun onLocationChanged(location: Location?)
    {
        Log.i("CURRENT" , "${location!!.latitude} - ${location!!.longitude}")
    }

    override fun onConnectionSuspended(p0: Int)
    {
        mGoogleApiClient!!.connect()
    }

    override fun onStart() {
        super.onStart()
        if(mGoogleApiClient != null)
        {
            mGoogleApiClient!!.connect()
        }
    }

    override fun onDestroy() {
        mGoogleApiClient!!.disconnect()
        super.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        checkPlayService()
    }
}

