package com.example.felixlin.metroexplorer.activity.model


import java.io.Serializable

class Landmark(var id: String, var title: String, var overview: String, var voteAverage: Float, var voteCount: Float, internal var posterPath: String, internal var backdropPath: String) : Serializable {

    fun getPosterPath(): String {
        return String.format("https://s3-media4.fl.yelpcdn.com/bphoto%s", posterPath)
    }

    fun setPosterPath(posterPath: String) {
        this.posterPath = posterPath
    }

    fun getBackdropPath(): String {
        return String.format("https://s3-media4.fl.yelpcdn.com/bphoto%s", backdropPath)
    }

    fun setBackdropPath(backdropPath: String) {
        this.backdropPath = backdropPath
    }

}
