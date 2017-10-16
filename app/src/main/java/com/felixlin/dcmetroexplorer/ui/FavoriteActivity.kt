package com.felixlin.dcmetroexplorer.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.felixlin.dcmetroexplorer.R
import com.felixlin.dcmetroexplorer.adapter.FavoriteListAdapter
import com.felixlin.dcmetroexplorer.db.LandmarkDbTable
import kotlinx.android.synthetic.main.activity_favorite.*

class FavoriteActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)

        rv_fav.setHasFixedSize(true)
        rv_fav.layoutManager = LinearLayoutManager(this)
        rv_fav.adapter = FavoriteListAdapter(applicationContext, LandmarkDbTable(this).readAllHabits())
    }
}
