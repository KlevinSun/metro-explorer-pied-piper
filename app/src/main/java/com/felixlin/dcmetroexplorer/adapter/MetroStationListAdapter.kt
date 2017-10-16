package com.felixlin.dcmetroexplorer.adapter

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.felixlin.dcmetroexplorer.R
import com.felixlin.dcmetroexplorer.model.Metro
import com.felixlin.dcmetroexplorer.ui.LandmarkDetailActivity
import com.felixlin.dcmetroexplorer.ui.LandmarksActivity
import kotlinx.android.synthetic.main.row_station.view.*


class MetroStationListAdapter(private var context: Context, private var MetroData: ArrayList<Metro>) : RecyclerView.Adapter<MetroStationListAdapter.ViewHolder>(){

    private var TAG: String = "MetroStationAdapter"



    override fun getItemCount() = MetroData.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.row_station, parent, false)

        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val station = MetroData[position]

        holder.bindData(station)
    }



    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener{
        private var view: View = itemView
        private var station: String? = null
        private var zip: String? = null

        init {
            itemView.setOnClickListener(this)
        }
        override fun onClick(itemView: View) {
            // plesae turn to landmark Activity here
            var metro = MetroData[adapterPosition]

            var intent = Intent(context, LandmarksActivity::class.java)
            intent.putExtra("metroData", metro)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
            Toast.makeText(itemView.context, "station: $station , zipcode: $zip", Toast.LENGTH_LONG).show()
        }
        fun bindData(station: Metro){
            this.station = station.station
            this.zip = station.Zip
            view.stationName.text = this.station
        }

    }

    fun filterList(newstationList : ArrayList<Metro>){
        MetroData = newstationList

        notifyDataSetChanged()
    }
}