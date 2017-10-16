package com.felixlin.dcmetroexplorer.ui

import android.content.Intent
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.view.View
import com.felixlin.dcmetroexplorer.R

class MainActivity : AppCompatActivity() {

    private val REQUEST_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestPermissionIfNecessary()
    }

    fun switchToLandmark(v: View) {
        val intent = Intent(this, LandmarksActivity::class.java)
        startActivity(intent)
    }

    fun switchToFavorite(v: View) {
        val intent = Intent(this, FavoriteActivity::class.java)
        startActivity(intent)
    }

    fun switchToMetroStation(v: View)
    {
        val intent = Intent(this@MainActivity, MetroStationActivity::class.java)
        startActivity(intent)
    }

    fun requestPermissionIfNecessary(){
        val selfPermission = ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
        if(selfPermission != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_CODE)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_CODE){
            if(grantResults.isNotEmpty() && grantResults.first() != PackageManager.PERMISSION_GRANTED){
                requestPermissionIfNecessary()
            }
        }
    }
}
