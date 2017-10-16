package com.felixlin.dcmetroexplorer.model

import java.io.Serializable


data class Metro(val code: String, val station: String, val Lat: Float, val Long: Float, val Zip: String, val stationtogether: String) : Serializable