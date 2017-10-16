package com.felixlin.dcmetroexplorer.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.View
import com.felixlin.dcmetroexplorer.R
import com.felixlin.dcmetroexplorer.model.Landmark
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import com.felixlin.dcmetroexplorer.db.LandmarkDbTable
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_landmark_detail.*
import kotlinx.android.synthetic.main.content_landmark_detail.*
import kotlinx.android.synthetic.main.list_item.view.*


class LandmarkDetailActivity : AppCompatActivity() {

    lateinit var mLandmark: Landmark

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landmark_detail)

        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        val fab = findViewById<View>(R.id.fab) as FloatingActionButton

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val extras = intent.extras
        if(extras != null) {
            mLandmark = extras.getSerializable("LANDMARK") as Landmark
            this.title = mLandmark.name
            tv_rating.text = "Rating: " + mLandmark.rating + "/5"

            if(mLandmark.imageUrl.isEmpty())
            {
                imageBackdrop.setImageDrawable(R.drawable.jefferson_memorial)
            }
            else{
                Picasso.with(this).load(mLandmark.imageUrl).into(imageBackdrop)
            }
        }
    }

    fun storeLandmark(v: View)
    {
        val name = mLandmark.name
        val rating = mLandmark.rating
        val image = mLandmark.imageUrl
        val landmark = Landmark(name, rating, image)

        val id = LandmarkDbTable(this).store(landmark)
        if (id == -1L) {
            Toast.makeText(this, "Habit could not be stored...let's not make this a habit", Toast.LENGTH_SHORT)
        }

        Snackbar.make(v, "Landmark saved as favorite", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();

        Log.d("Test", "Information store")
    }
}

private fun ImageView.setImageDrawable(jefferson_memorial: Int) {}


