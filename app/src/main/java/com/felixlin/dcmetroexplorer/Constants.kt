package com.felixlin.dcmetroexplorer

object Constants
{
    // YELP
    val YELP_CLIENT_ID = BuildConfig.YELP_CLIENT_ID
    val YELP_CLIENT_SECRET = BuildConfig.YELP_CLIENT_SECRET
    val YELP_TOKEN = BuildConfig.YELP_TOKEN
    val YELP_TYPE = BuildConfig.YELP_TYPE
    val YELP_BASE_URL = "https://api.yelp.com/v3/businesses/search?term=favorits"
    val YELP_LOCATION_QUERY_PARAMETER = "location"
    val TEAM_NAME = "Piper"

    // WMATA
    val WMATA_SEARCH_URL = "https://api.wmata.com/Rail.svc/json/jStations"
    val WMATA_SEARCH_API_TOKEN = "7b9cd6b7aaac4ea99cc974c415ab2f1a"
    val NO_MATCH = "No Station Found"


    // Location
    val SUCCESS_RESULT = 0

    val FAILURE_RESULT = 1

    private val PACKAGE_NAME = "com.felixlin.dcmetroexplorer"

    val RECEIVER = PACKAGE_NAME + ".RECEIVER"

    val RESULT_DATA_KEY = PACKAGE_NAME + ".RESULT_DATA_KEY"

    val LOCATION_DATA_EXTRA = PACKAGE_NAME + ".LOCATION_DATA_EXTRA"
}
