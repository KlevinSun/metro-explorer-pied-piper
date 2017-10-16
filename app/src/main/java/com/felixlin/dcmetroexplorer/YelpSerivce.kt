package com.felixlin.dcmetroexplorer

import android.util.Log
import com.felixlin.dcmetroexplorer.model.Landmark
import okhttp3.*
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.util.ArrayList

class YelpService
{
    fun processResults(response: Response): ArrayList<Landmark>
    {
        val landmarks = ArrayList<Landmark>()

        try {
            Log.v("At the TRY, response = ", response.toString())
            val jsonData = response.body()!!.string()
            if (response.isSuccessful)
            {
                val yelpJSON = JSONObject(jsonData)

                val businessesJSON = yelpJSON.getJSONArray("businesses")

                for (i in 0 .. businessesJSON.length())
                {
                    val landmarkJSON = businessesJSON.getJSONObject(i)

                    val name = landmarkJSON.getString("name")

                    val rating = landmarkJSON.getDouble("rating")

                    val imageUrl = landmarkJSON.getString("image_url")

                    val landmark = Landmark(name, rating, imageUrl)

                    landmarks.add(landmark)
                }
            }
        }
        catch (e: IOException)
        {
            e.printStackTrace()
        }
        catch (e: JSONException)
        {
            e.printStackTrace()
        }

        return landmarks
    }

        fun findLandmarks(location: String, callback: Callback)
        {
            val client = OkHttpClient()

            val urlBuilder = HttpUrl.parse(Constants.YELP_BASE_URL)?.newBuilder()
            urlBuilder?.addQueryParameter(Constants.YELP_LOCATION_QUERY_PARAMETER, location)
            val url = urlBuilder?.build().toString()

            // URL should end up looking like this: "https://api.yelp.com/v3/businesses/search?term=landmarks
            Log.v("The URL is: ", url)
            Log.v("The URL is: ", url)

            val request = Request.Builder()
                    .header("Authorization", "Bearer " + Constants.YELP_TOKEN)
                    .url(url)
                    .build()

            val call = client.newCall(request)
            call.enqueue(callback)
        }

}
