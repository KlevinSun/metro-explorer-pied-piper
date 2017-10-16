package com.felixlin.dcmetroexplorer

import android.content.Context
import android.util.Log
import com.felixlin.dcmetroexplorer.model.Metro
import com.google.gson.JsonObject
import com.koushikdutta.async.future.FutureCallback
import com.koushikdutta.ion.Ion

class WmataStationSearchManager(val context: Context, val result: ArrayList<Metro>)
{
    private val TAG = "FetchMetroManager"
    var stationSearchCompletionListener: StationSearchCompletionListener?= null

    interface StationSearchCompletionListener
    {
        fun searchSucceed()
        fun searchFailed()
    }

    private fun parseStationNameFromJSON(jsonObject: JsonObject){
        val stationInfo = jsonObject.getAsJsonArray("Stations")
        if(stationInfo != null && stationInfo.size()>0){
            for (i in 0..stationInfo.size() - 1){
                val stationData = stationInfo.get(i).asJsonObject
                val stationCode = stationData.get("Code").asString
                val stationName = stationData.get("Name").asString
                val stationTogether = stationData.get("StationTogether1").asString
                val stationLatitude = stationData.get("Lat").asFloat
                val stationLongtitude = stationData.get("Lon").asFloat
                result.add(Metro(stationCode, stationName, stationLatitude, stationLongtitude, stationTogether))
            }
        }else{
            Log.e(TAG,"Loss some information during JSON parse")

        }

    }

    fun search(){
        Ion.with(context).load(Constants.WMATA_SEARCH_URL)
                .addHeader("api_key", Constants.WMATA_SEARCH_API_TOKEN)
                .asJsonObject()
                .setCallback(FutureCallback { error, result ->
                    error?.let {
                        Log.e(TAG, it.message)

                        stationSearchCompletionListener?.searchFailed()
                    }
                    result?.let{
                        parseStationNameFromJSON(it)

                        stationSearchCompletionListener?.searchSucceed()
                    }
                })
    }
}
